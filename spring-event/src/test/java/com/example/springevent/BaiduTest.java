package com.example.springevent;

import com.example.springevent.service.AutomationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author CHQ
 * @version 1.0
 * @date 2024/11/15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaiduTest {
    @Autowired
    private AutomationService automationService;

    @Test
    public void testBaidu() throws InterruptedException {
        automationService.performTest();
        Thread.sleep(1000 * 2000);
    }
}
