package com.devracoon.awssnstest;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.buffered.AmazonSQSBufferedAsyncClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler;
import org.springframework.cloud.aws.messaging.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@RequiredArgsConstructor
@Configuration
public class AwsSqsListnerConfig {

    @Value("${cloud.aws.region.static}")
    private String region;


    @Primary
    @Bean
    public AmazonSQSBufferedAsyncClient amazonSQSAws(Environment environment) {
        AmazonSQSAsync amazonSQSAsync = AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(environment.getProperty("cloud.aws.credentials.access-key"), environment.getProperty("cloud.aws.credentials.secret-key"))))
                .withRegion(region)
                .build();

        return new AmazonSQSBufferedAsyncClient(amazonSQSAsync);
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(Environment environment) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
        simpleMessageListenerContainer.setAmazonSqs(amazonSQSAws(environment));
        simpleMessageListenerContainer.setMessageHandler(queueMessageHandler(environment));
        simpleMessageListenerContainer.setMaxNumberOfMessages(10); // 최대값이 10
        simpleMessageListenerContainer.setTaskExecutor(threadPoolTaskExecutor());
        return simpleMessageListenerContainer;
    }

    @Bean
    public QueueMessageHandler queueMessageHandler(Environment environment) {
        QueueMessageHandlerFactory queueMessageHandlerFactory = new QueueMessageHandlerFactory();
        queueMessageHandlerFactory.setAmazonSqs(amazonSQSAws(environment));
        return queueMessageHandlerFactory.createQueueMessageHandler();
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50);
        executor.setMaxPoolSize(50);
        executor.setThreadNamePrefix("sqs-test-listener-");
        executor.initialize();
        return executor;
    }
}