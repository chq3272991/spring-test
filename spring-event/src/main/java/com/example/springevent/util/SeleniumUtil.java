package com.example.springevent.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author CHQ
 * @version 1.0
 * @date 2024/11/15
 */
public class SeleniumUtil {
    /**
     * 检查元素是否存在于页面上。
     *
     * @param driver WebDriver 实例
     * @param by     元素定位器
     * @return 如果元素存在返回true，否则返回false
     */
    public static boolean isElementPresent(WebDriver driver, By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }


}
