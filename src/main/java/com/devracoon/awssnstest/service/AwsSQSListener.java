package com.devracoon.awssnstest.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AwsSQSListener {

    @SqsListener(value="DevRacoon-SQS-Test" , deletionPolicy = SqsMessageDeletionPolicy.NEVER)
    public void testSqsListener(String snsMessage){
        log.info("sns message : {}" , snsMessage);
//        log.info(" headers : {}" , headers);
//        log.info(" ack :{}" , acknowledgment);
    }
}
