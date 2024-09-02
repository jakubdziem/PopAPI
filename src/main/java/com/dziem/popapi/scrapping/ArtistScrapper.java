package com.dziem.popapi.scrapping;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ArtistScrapper {

    public static void main(String[] args) {
        // Set up WebDriver (assuming ChromeDriver is used)
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        WebDriver driver = new ChromeDriver(options);

        try {
            // Navigate to the page
            driver.get("https://chartmasters.org/most-streamed-artists-ever-on-spotify/");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

            // Wait for the consent pop-up and close it if it appears
            try {
                WebElement consentPopup = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.cmpboxbtn.cmbboxbnyes.cmptxt_btn_yes")));
                consentPopup.click();
            } catch (Exception e) {
                System.out.println("No consent pop-up found or unable to click it.");
            }

            // Open the dropdown menu
            WebElement dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.bootstrap-select button.dropdown-toggle")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdownButton);

            // Select the "All" option
            WebElement allOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//li[@data-original-index='1']/a")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", allOption);

            // Wait for the table to update
            waitForTableToUpdate(wait);

            // Extract data from the table
            WebElement table = driver.findElement(By.cssSelector("table#table_1 tbody"));
            List<WebElement> rows = table.findElements(By.cssSelector("tr"));

            List<ArtistFromWeb> artistFromWebList = new ArrayList<>();
            for (WebElement row : rows) {
                String rank = row.findElement(By.cssSelector("td.expand.numdata.integer.rankings.column-rank")).getText();
                String artistName = row.findElement(By.cssSelector("td.column-artist a.styledLink")).getText();
                String leadStreams = row.findElement(By.cssSelector("td.numdata.integer.numbersMain.column-lead-streams")).getText();
                ArtistFromWeb artistFromWeb = new ArtistFromWeb(Integer.parseInt(rank), artistName, leadStreams);
                artistFromWebList.add(artistFromWeb);
            }
            for (ArtistFromWeb artistFromWeb : artistFromWebList) {
                System.out.println(artistFromWeb.rank + " " + artistFromWeb.artistName + " " + artistFromWeb.leadStreams);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the driver
            driver.quit();
        }
    }

    private static void waitForTableToUpdate(WebDriverWait wait) {
        try {
            // Wait for the table to have more rows
            wait.until(driver -> {
                WebElement table = driver.findElement(By.cssSelector("table#table_1 tbody"));
                List<WebElement> rows = table.findElements(By.cssSelector("tr"));
                return rows.size() > 25;  // Adjust the threshold based on expected number of rows
            });
        } catch (Exception e) {
            // Handle the case where the table does not update as expected
            System.out.println("Table did not update with more than 25 rows.");
        }
    }

    record ArtistFromWeb(Integer rank, String artistName, String leadStreams) {}
}
