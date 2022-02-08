package com.devracoon.awssnstest.web;

import com.devracoon.awssnstest.service.AwsSnsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class SnsTestController {

    @Autowired
    private AwsSnsService awsSnsService;

    @PostMapping("/test/sns/publish")
    public String snsPublish(String message){
        awsSnsService.testSnsPublish(message);
        return "ok";
    }
}
