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
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        WebDriver driver = new ChromeDriver(options);
        List<SongFromWeb> songFromWebList = new ArrayList<>();
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
            System.out.println("Retrieving pop");
            songFromWebList.addAll(getSongsByGenre(wait, driver, "Pop"));
            System.out.println("Retrieving hiphop");
            songFromWebList.addAll(getSongsByGenre(wait, driver, "HipHop"));
            System.out.println("Retrieving rock");
            songFromWebList.addAll(getSongsByGenre(wait, driver, "Rock"));
            System.out.println("Retrieving general");
            songFromWebList.addAll(getSongsByGenre(wait, driver, "General"));
            System.out.println("Retrieved all");


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
        try (FileWriter myWriter = new FileWriter("src/main/resources/data/songs.txt", false)) {
            myWriter.write(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).concat("\n"));
            for (SongFromWeb songFromWeb : songFromWebList) {
                myWriter.write(songFromWeb.rank + ";" + songFromWeb.artistName + ";" + songFromWeb.songName + ";" + songFromWeb.leadStreams + ";" + songFromWeb.genre + "\n");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static List<SongFromWeb> getSongsByGenre(WebDriverWait wait, WebDriver driver, String genre) throws InterruptedException {
        List<SongFromWeb> songFromWebList = new ArrayList<>();
        driver.navigate().refresh();
        if(!genre.equals("General")) {
            // Wait for the dropdown button to be present first
            WebElement dropdownButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class, 'btn-group')]//button[contains(@class, 'dropdown-toggle')]")));
            // Scroll to the element to bring it into view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdownButton);
            // Wait for the button to be clickable
            dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(dropdownButton));
            // Click the dropdown button using JavaScript
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdownButton);
            // Wait for the options list to be visible
            WebElement optionsList = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'dropdown-menu')]//ul[contains(@class, 'inner')]")));
            // Retrieve all options inside the dropdown
            List<WebElement> allOptions = optionsList.findElements(By.tagName("li"));

            for (WebElement option : allOptions) {
                System.out.println(option.getText());
            }

            switch(genre) {
                case "Pop" -> {
                    for (WebElement option : allOptions) {
                        String optionText = option.getText();
                        System.out.println("Option found: " + optionText);

                        // If the option is "All", click it
                        if (optionText.equals("Pop")) {
                            // Option located, click it using JavaScript
                            WebElement optionAnchor = option.findElement(By.tagName("a"));
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", optionAnchor);
                            sleep(10000);
                            break;
                        }

                    }
                }
                case "HipHop" -> {
                    for (WebElement option : allOptions) {
                        String optionText = option.getText();
                        System.out.println("Option found: " + optionText);

                        // If the option is "All", click it
                        if (optionText.equals("Hip-hop")) {
                            // Option located, click it using JavaScript
                            WebElement optionAnchor = option.findElement(By.tagName("a"));
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", optionAnchor);
                            sleep(10000);
                            break;
                        }

                    }
                }
                case "Rock" -> {
                    for (WebElement option : allOptions) {
                        String optionText = option.getText();
                        System.out.println("Option found: " + optionText);

                        // If the option is "All", click it
                        if (optionText.equals("Rock") || optionText.equals("Hard Rock / Metal")) {
                            // Option located, click it using JavaScript
                            WebElement optionAnchor = option.findElement(By.tagName("a"));
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", optionAnchor);
                            sleep(10000);
                        }
                    }
                }
        }

        }
        for (int i = 1; i <= 8; i++) { //you need 8 pages to retrieve 200 records
            // Wait for the table to load
            sleep(20000);
            WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table#table_1 tbody")));
            List<WebElement> rows = table.findElements(By.cssSelector("tr"));

//                 Retry if the table rows are empty
            while (rows.get(0).getText().isBlank() || rows.get(0).getText().isEmpty()) {
                for(WebElement row : rows) {
                    System.out.println(row.getText().isEmpty() ? "Empty" : row.getText());
                }
                System.out.println("Retrying");
                sleep(20000);
                driver.navigate().refresh(); // Instead of creating a new driver, refresh the current one
//                table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table#table_1 tbody")));
                table = driver.findElement(By.cssSelector("table#table_1 tbody"));
                rows = table.findElements(By.cssSelector("tr"));
                System.out.println("Done");
            }
//                sleep(10000);
            System.out.println("Retrieving data");
            for (WebElement row : rows) {
                WebElement element = row.findElement(By.cssSelector("td.expand.numdata.integer.rankings.column-rank"));
                String rank = element.getText();
                WebElement element1 = row.findElement(By.cssSelector("td.column-song a.styledLink"));
                String artistName = element1.getText();
                WebElement element2 = row.findElement(By.cssSelector("td.column-song i"));
                String songName = element2.getText();
                WebElement element3 = row.findElement(By.cssSelector("td.numdata.integer.numbersMain.column-playcount"));
                String leadStreams = element3.getText();
                SongFromWeb songFromWeb = new SongFromWeb(Integer.parseInt(rank), artistName, songName, genre, leadStreams);
                songFromWebList.add(songFromWeb);
                System.out.println((songFromWeb.rank + ";" + songFromWeb.artistName + ";" + songFromWeb.songName + ";" + songFromWeb.leadStreams + ";" + songFromWeb.genre + "\n"));

            }
            WebElement paginationParent = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.dataTables_paginate.paging_full_numbers a.paginate_button.next#table_1_next")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", paginationParent);
        }
        return songFromWebList;
    }
    record SongFromWeb(Integer rank, String artistName, String songName, String genre, String leadStreams) {}
}

