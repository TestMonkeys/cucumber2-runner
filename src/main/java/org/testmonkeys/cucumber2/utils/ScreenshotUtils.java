package org.testmonkeys.cucumber2.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testmonkeys.cucumber2.appender.CucumberScenarioContext;
import org.testmonkeys.cucumber2.formatter.TestLogHelper;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.screentaker.ViewportPastingStrategy;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {

    public static String folderPath;
    private static String currentLogName = "";
    private static int screenshotIndex = 1;

    public static void highLightElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid red'", new WebElement[]{element});
    }

    public static void unhighLightElement(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.removeProperty('border')", new WebElement[]{element});
    }

    public static void makeScreenShot(WebDriver driver) throws IOException {
        makeScreenShot(driver,null,null);
    }

    public static void makeScreenShot(WebDriver driver, String directory, String filename) throws IOException {
        updateScreenshotIndexIfNewTest();

        String imageFormat = "PNG";
        String imageFileExtension = ".png";

        byte[] imageBytes;

        final Screenshot screenshot = new AShot()
                .shootingStrategy(new ViewportPastingStrategy(500))
                .takeScreenshot(driver);

        final BufferedImage image = screenshot.getImage();
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        ImageIO.write(image, imageFormat, array);
        array.flush();
        imageBytes = array.toByteArray();

        CucumberScenarioContext.getInstance().attachScreenshot(imageBytes);

        if (directory == null) return;
        if (filename == null) filename = "screenshot_" + System.currentTimeMillis();
        Files.createDirectories(Paths.get(directory));
        File file = new File(directory + String.format("%03d", screenshotIndex++) + filename + imageFileExtension);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imageBytes);
        }
    }

    private static void updateScreenshotIndexIfNewTest() {
        if (!currentLogName.equals(TestLogHelper.getCurrentLogName())) {
            currentLogName = TestLogHelper.getCurrentLogName();
            screenshotIndex = 1;
        }
    }

    public static String getCurrentScreenshotPath() {
        String logFilePath = TestLogHelper.getCurrentLogName();
        String logfFolderPath = logFilePath.equals("test") ? "test" : logFilePath;
        return String.format(folderPath, logfFolderPath);
    }

    public static String getCurrentTimestamp() {
        DateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return format.format(new Date());
    }

    //target/logs/%s/Screenshots
    public void setFolderPath() {
        folderPath = System.getProperty("folder.path.screenshot");
    }
}
