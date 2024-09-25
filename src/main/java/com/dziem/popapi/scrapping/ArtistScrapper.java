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

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ArtistScrapper {

    public static void getAndSaveArtistDataToFile() {
        // Set up WebDriver (assuming ChromeDriver is used)
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        WebDriver driver = new ChromeDriver(options);

        try {
            // Navigate to the page
            String artistUrl = System.getenv("artistUrl");
            driver.get(artistUrl);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Wait for the consent pop-up and close it if it appears
            try {
                WebElement consentPopup = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.cmpboxbtn.cmbboxbnyes.cmptxt_btn_yes")));
                consentPopup.click();
            } catch (Exception e) {
                System.out.println("No consent pop-up found or unable to click it.");
            }

            // Open the dropdown menu
            WebElement dropdownButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.bootstrap-select.length_menu button.dropdown-toggle.btn-default")));
//            String title = dropdownButton.getAttribute("title");
//            System.out.println(title);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdownButton);


            WebElement optionsList = dropdownButton.findElement(By.xpath("following-sibling::div//ul"));
            List<WebElement> allOptions = optionsList.findElements(By.tagName("li"));
            for (WebElement option : allOptions) {
                String optionText = option.getText();
                System.out.println("Option found: " + optionText);

                // If the option is "All", click it
                if (optionText.equals("All")) {
                    // Option located, click it using JavaScript
                    WebElement optionAnchor = option.findElement(By.tagName("a"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", optionAnchor);
                    break;
                }
            }

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
//            for (ArtistFromWeb artistFromWeb : artistFromWebList) {
//                System.out.println(artistFromWeb.rank + " " + artistFromWeb.artistName + " " + artistFromWeb.leadStreams);
//            }
            try (FileWriter myWriter = new FileWriter("src/main/resources/data/artist.txt", false)){
                myWriter.write(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).concat("\n"));
                for(int i = 0; i < artistFromWebList.size(); i++) {
                    if(i==200) break;
                    ArtistFromWeb artistFromWeb = artistFromWebList.get(i);
                    myWriter.write(artistFromWeb.rank + " " + artistFromWeb.artistName + " " + artistFromWeb.leadStreams + "\n");
                }
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
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

//1 Taylor Swift 89,922,327,805
//2 Drake 79,098,827,063