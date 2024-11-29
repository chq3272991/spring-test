package com.example.springevent;

import com.example.springevent.event.publisher.MyEventPublisher;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringEventApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringEventApplication.class, args);
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://www.baidu.com");
    }

}
