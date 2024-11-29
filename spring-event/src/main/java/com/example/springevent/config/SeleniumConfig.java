package com.example.springevent.config;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author CHQ
 * @version 1.0
 * @date 2024/11/15
 */
@Configuration
public class SeleniumConfig {

    @Bean
    public ChromeOptions getOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        // 解决webdriver.get打开某些网页，可能等待太久的问题，缺点需要操作dom之前，加sleep时间或者判断元素是否已存在
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
        // 取消Chrome正受到自动测试软件的控制提示语
        chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        List<String> arguments = new ArrayList<>();
        arguments.add("--window-size=" + 1600 + "," + 900);
        //chromeOptions.addArguments("--disable-features=VizDisplayCompositor");
        arguments.add("--start-maximized");  // 最大化，防止失去焦点
        arguments.add("--allow-running-insecure-content"); // 消除安全校验 可以直接无提示访问http网站
        arguments.add("--no-sandbox");
        arguments.add("lang=zh-CN.UTF-8");
        arguments.add("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.5735.90 Safari/537.36");


        // 获取当前用户的临时目录路径
        String tempDirectory = System.getProperty("java.io.tmpdir");
        // 指定用户数据目录为临时目录下的特定文件夹
        arguments.add("--user-data-dir=" + tempDirectory + "\\chrome_profile");
//        if(alisa.equals("prod") || (isHeadless && !alisa.equals("prod"))) {
//            //关闭gpu图片渲染
//            arguments.add("--disable-gpu");
//            arguments.add("--no-sandbox");
//            arguments.add("--disable-dev-shm-usage");
//            arguments.add("--headless");
//        }
        chromeOptions.addArguments(arguments);
        // 设置文件下载路径
        Map<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("javascript.enabled","true");   // 设置启用javascript，避免有些selenium-java会默认关闭
        chromePrefs.put("networkConnection.enabled","true");

        // 减少图片加载
        chromePrefs.put("profile.managed_default_content_settings.images", 2);
        chromePrefs.put("profile.managed_default_content_settings.notifications", 2);
        chromePrefs.put("intl.accept_languages", "zh-CN,zh;q=0.9,en;q=0.5");


        // 2、关闭保存密码弹窗
//        chromePrefs.put("credentials_enable_service", false);
//        chromePrefs.put("profile.password_manager_enabled", false);

//        if(downloadPath != null && !downloadPath.equals("")) {
//            chromePrefs.put("download.default_directory", downloadPath);
//            chromePrefs.put("download.prompt_for_download", "false");
//        }
        chromeOptions.setExperimentalOption("prefs", chromePrefs);
        return chromeOptions;
    }

    @Bean
    public WebDriver webDriver() {
        ChromeOptions options = getOptions();
        // 设置ChromeDriver的路径
//        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe");
        return new ChromeDriver(options);
    }
}
