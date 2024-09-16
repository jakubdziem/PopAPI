package com.dziem.popapi.scrapping;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class SongScrapper {
    public static void main(String[] args) {
        // Set up WebDriver (assuming ChromeDriver is used)
//        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        WebDriver driver = new ChromeDriver(options);

        try {
            // Navigate to the page
            String songUrl = System.getenv("songUrl");
            driver.get(songUrl);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Wait for the consent pop-up and close it if it appears
            try {
                WebElement consentPopup = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.cmpboxbtn.cmbboxbnyes.cmptxt_btn_yes")));
                consentPopup.click();
            } catch (Exception e) {
                System.out.println("No consent pop-up found or unable to click it.");
            }
            List<SongFromWeb> songFromWebList = new ArrayList<>();
            for (int i = 1; i <= 2; i++) { //you need 8 pages to retrieve 200 records
                // Wait for the table to load
                WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table#table_1 tbody")));
                List<WebElement> rows = table.findElements(By.cssSelector("tr"));

                // Retry if the table rows are empty
                while (rows.get(0).getText().isEmpty()) {
                    System.out.println("Retrying");
                    sleep(10000);
                    driver.navigate().refresh(); // Instead of creating a new driver, refresh the current one
                    table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table#table_1 tbody")));
                    rows = table.findElements(By.cssSelector("tr"));
                    System.out.println("Done");
                }

                sleep(10000);
                System.out.println("Retrieving data");
                for (WebElement row : rows) {
                    WebElement element = row.findElement(By.cssSelector("td.expand.numdata.integer.rankings.column-rank"));
                    String rank = element.getText();
                    WebElement element1 = row.findElement(By.cssSelector("td.column-song a.styledLink"));
                    String artistName = element1.getText();
                    WebElement element2 = row.findElement(By.cssSelector("td.column-song i"));
                    String songName = element2.getText();
                    String genre = "General";
                    WebElement element3 = row.findElement(By.cssSelector("td.numdata.integer.numbersMain.column-playcount"));
                    String leadStreams = element3.getText();
                    SongFromWeb songFromWeb = new SongFromWeb(Integer.parseInt(rank), artistName, songName, genre, leadStreams);
                    songFromWebList.add(songFromWeb);
                    System.out.println((songFromWeb.rank + " " + songFromWeb.artistName + " " + songFromWeb.songName + " " + songFromWeb.leadStreams + " " + songFromWeb.genre + "\n"));

                }
                WebElement paginationParent = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.dataTables_paginate.paging_full_numbers a.paginate_button.next#table_1_next")));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", paginationParent);
                int expectedRank = i * 25 + 1;
                // Wait for the next page to load fully before proceeding
                WebElement newTable = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table#table_1 tbody")));
                wait.until(webDriver -> {
                    List<WebElement> newRows = newTable.findElements(By.cssSelector("tr"));
                    return !newRows.isEmpty() && newRows.get(0).findElement(By.cssSelector("td.expand.numdata.integer.rankings.column-rank")).getText().equals(String.valueOf(expectedRank + 25));
                });
            }
            try (FileWriter myWriter = new FileWriter("src/main/resources/data/songGeneral.txt", false)) {
                myWriter.write(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).concat("\n"));
                for (int i = 0; i < songFromWebList.size(); i++) {
                    if (i == 200) break;
                    SongFromWeb songFromWeb = songFromWebList.get(i);
                    myWriter.write(songFromWeb.rank + " " + songFromWeb.artistName + " " + songFromWeb.songName + " " + songFromWeb.leadStreams + " " + songFromWeb.genre + "\n");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
//            for (SongFromWeb songFromWeb : songFromWebList) {
//                System.out.println(songFromWeb.rank + ";" + songFromWeb.artistName + ";" + songFromWeb.leadStreams + ";" + songFromWeb.songName + ";" + songFromWeb.genre);
//            }
    record SongFromWeb(Integer rank, String artistName, String songName, String genre, String leadStreams) {}
}

