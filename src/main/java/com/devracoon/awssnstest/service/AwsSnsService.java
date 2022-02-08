package com.devracoon.awssnstest.service;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.devracoon.awssnstest.dto.SnsMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.javafx.geom.transform.Identity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class AwsSnsService {

    public final static String TEST_SNS_ARN="arn:aws:sns:ap-northeast-2:385423560848:DevRacoon-SNS-Test";
    @Autowired
    private final AmazonSNSClient amazonSNSClient;

    @Autowired
    private ObjectMapper mapper;

    public void testSnsPublish(String sendMessage) {

        SnsMessage snsMessage = SnsMessage.builder().messageId(UUID.randomUUID().toString())
                .messageText(sendMessage)
                .eventTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")))
                .build();

        try{
            String message = mapper.writeValueAsString(snsMessage);
            amazonSNSClient.publish(TEST_SNS_ARN ,  message);

        }catch (Exception e){
            log.error("AWS SNS publish error" , e);
        }
    }

}
