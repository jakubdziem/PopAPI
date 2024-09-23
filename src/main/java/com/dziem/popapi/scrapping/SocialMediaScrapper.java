package com.dziem.popapi.scrapping;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

public class SocialMediaScrapper {
    public List<String> getSocialMediaData() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://en.wikipedia.org/wiki/List_of_most-followed_Instagram_accounts");
        WebElement tbodyInstagram = driver.findElement(By.cssSelector("table.sortable.wikitable.sticky-header.static-row-numbers.sort-under.col4center.jquery-tablesorter tbody"));
        List<String> resultArray = getSocialMediaEntities(tbodyInstagram, "Instagram");


        driver.get("https://en.wikipedia.org/wiki/List_of_most-followed_TikTok_accounts");
        String currentUrl = driver.getCurrentUrl();
        WebElement tbodyTikTok = driver.findElement(By.cssSelector("div#mw-content-text.mw-body-content table.wikitable.sortable.jquery-tablesorter"));
        resultArray.addAll(getSocialMediaEntities(tbodyTikTok, "Tik tok"));

        driver.get("https://en.wikipedia.org/wiki/List_of_most-subscribed_YouTube_channels");
        WebElement tbodyYoutube = driver.findElement(By.cssSelector("table.sortable.wikitable.sticky-header.static-row-numbers.sort-under.col3center.col4center.jquery-tablesorter"));
        resultArray.addAll(getSocialMediaEntities(tbodyYoutube, "Youtube"));

        driver.get("https://en.wikipedia.org/wiki/List_of_most-followed_Twitter_accounts");
        WebElement tbodyTwitter = driver.findElement(By.cssSelector("table.wikitable.sortable.jquery-tablesorter"));
        resultArray.addAll(getSocialMediaEntities(tbodyTwitter, "Twitter"));

        return resultArray;
    }

    private static List<String> getSocialMediaEntities(WebElement tbody, String type) {
        List<WebElement> rows = tbody.findElements(By.cssSelector("tr"));
        List<String> resultArray = new ArrayList<>();
        int indexOfFollowersColumn = type.equals("Twitter") ? 2 : 3;
        for(int i = 0; i < rows.size(); i++) {
            List<WebElement> columns = rows.get(i).findElements(By.cssSelector("td"));
            if(!columns.isEmpty()) {
                String userName = columns.get(0).getText();
                String followers = columns.get(indexOfFollowersColumn).getText();
                String tier = i < 15 ? "1" : "2";
                String imageUrl = "/images/social_media/" + userName + ".png";
                resultArray.add(String.format("%s;%s;%s;%s;%s", userName, followers, tier, imageUrl, type));
            }
        }
        return resultArray;
    }
}
