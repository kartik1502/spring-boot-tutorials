package com.example.springschedular.configuration;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class Schedular {

    @Scheduled(fixedRate = 1000)
    public void startSchedular() throws InterruptedException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy HH:mm:ss");
        System.out.println(dateFormat.format(new java.util.Date()));
        Thread.sleep(1000);
    }

}
