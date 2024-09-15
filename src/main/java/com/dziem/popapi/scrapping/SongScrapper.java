package com.dziem.popapi.scrapping;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SongScrapper {
    public static void main(String[] args) {
        // Set up WebDriver (assuming ChromeDriver is used)
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        WebDriver driver = new ChromeDriver(options);

        try {
            // Navigate to the page
            String songUrl = System.getenv("songUrl");
            driver.get(songUrl);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // Wait for the consent pop-up and close it if it appears
            try {
                WebElement consentPopup = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.cmpboxbtn.cmbboxbnyes.cmptxt_btn_yes")));
                consentPopup.click();
            } catch (Exception e) {
                System.out.println("No consent pop-up found or unable to click it.");
            }


            // Extract data from the table
            WebElement table = driver.findElement(By.cssSelector("table#table_1 tbody"));
            List<WebElement> rows = table.findElements(By.cssSelector("tr"));
            if(rows.get(0).getText().isEmpty()) {
                System.out.println("Waiting");
                wait.wait();
            }
            System.out.println("Retrieving data");
            List<SongFromWeb> songFromWebList = new ArrayList<>();
            for (WebElement row : rows) {
                String rank = row.findElement(By.cssSelector("td.expand.numdata.integer.rankings.column-rank")).getText();
                String artistName = row.findElement(By.cssSelector("td.column-song a.styledLink")).getText();
                String songName = row.findElement(By.cssSelector("td.column-song i")).getText();
                String genre = "General";
                String leadStreams = row.findElement(By.cssSelector("td.numdata.integer.numbersMain.column-playcount")).getText();
                SongFromWeb song = new SongFromWeb(Integer.parseInt(rank), artistName, songName, genre, leadStreams);
                songFromWebList.add(song);
            }
            for (SongFromWeb songFromWeb : songFromWebList) {
                System.out.println(songFromWeb.rank + ";" + songFromWeb.artistName + ";" + songFromWeb.leadStreams + ";" + songFromWeb.songName + ";" + songFromWeb.genre);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }


    record SongFromWeb(Integer rank, String artistName, String songName, String genre, String leadStreams) {}
}

