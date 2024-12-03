package website;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.time.Duration;

public class Mouthshut {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.verboseLogging", "C:\\Users\\Dell\\Downloads\\chromedriver_win32\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the target URL: ");
        String targetUrl = scanner.nextLine();
        scanner.close();
        
        try {
            // Open the target review page
            driver.get(targetUrl);

            // Scroll down to load dynamic content if necessary
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            Thread.sleep(3000); // Wait to allow dynamic content to load

            // JavaScript executor to fetch all review texts
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            List<WebElement> reviews = driver.findElements(By.cssSelector(".review-text")); // Adjust selector if necessary
            System.out.println("Number of reviews found: " + reviews.size());

            // Generate a unique filename with a timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "Reviews_" + timestamp + ".txt";
            FileWriter writer = new FileWriter(fileName);

            for (WebElement review : reviews) {
                String reviewText = (String) jsExecutor.executeScript("return arguments[0].textContent;", review);
                if (reviewText != null && !reviewText.isEmpty()) { // Write only non-empty reviews
                    writer.write("Review: " + reviewText.trim() + "\n");
                    System.out.println("Review written: " + reviewText.trim()); // Debugging output
                }
            }
            writer.close();
            System.out.println("Data scraped successfully. Saved to " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to file.");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("An error occurred during scraping.");
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}