package com.example.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequestMapping("/test")
@RestController
public class TestController {
    @Autowired
    reptileTxt reptile;


    @GetMapping("/test")
    public Object test() {

       return reptile.getCatalogueByPT();
    }

}
