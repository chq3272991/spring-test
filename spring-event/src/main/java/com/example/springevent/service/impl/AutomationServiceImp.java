package com.example.springevent.service.impl;

import com.example.springevent.service.AutomationService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author CHQ
 * @version 1.0
 * @date 2024/11/15
 */
@Service
@Slf4j
public class AutomationServiceImp implements AutomationService {
    private final WebDriver webDriver;

    public AutomationServiceImp(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public void performTest() {
        webDriver.get("https://jd.com");

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(120L));

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("nickname")));
            Thread.sleep(2000);
            // 已登录，执行后续步骤
            log.info("已登录，开始执行后续自动化步骤。");
            search("macbook pro m4");
        }catch (Exception exception) {
            log.warn("京东没登录，请登录再重试：{}", exception.getMessage());
        }

    }

    private void search(String shopName) {
        WebElement input = webDriver.findElement(By.xpath("//div[@role='serachbox']/input[1]"));
        WebElement searchBtn = webDriver.findElement(By.xpath("//div[@role='serachbox']/button[1]"));

        input.sendKeys(shopName);

        searchBtn.click();

    }
}
