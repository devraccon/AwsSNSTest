package com.devracoon.awssnstest;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.buffered.AmazonSQSBufferedAsyncClient;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AwsConfiguration {

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @Primary
    @Bean
    public AmazonSNSClient amazonSNSClient(Environment environment ) {
        if(StringUtils.equals(activeProfile , "")){
            return (AmazonSNSClient) AmazonSNSClientBuilder
                    .standard()
                    .withRegion(region)
                    .withCredentials(InstanceProfileCredentialsProvider.getInstance())
                    .build();
        }else{
            return (AmazonSNSClient) AmazonSNSClientBuilder
                    .standard()
                    .withRegion(region)
                    .withCredentials(new AWSStaticCredentialsProvider(
                            new BasicAWSCredentials(environment.getProperty("cloud.aws.credentials.access-key"), environment.getProperty("cloud.aws.credentials.secret-key"))))
                    .build();
        }

    }


//    @Primary
//    @Bean
//    public AmazonSQSBufferedAsyncClient amazonSQSAws(Environment environment) {
//        AmazonSQSAsync amazonSQSAsync = AmazonSQSAsyncClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(
//                new BasicAWSCredentials(environment.getProperty("cloud.aws.credentials.access-key"), environment.getProperty("cloud.aws.credentials.secret-key"))))
//                .withRegion(region)
//                .build();
//
//        return new AmazonSQSBufferedAsyncClient(amazonSQSAsync);
//    }

    // 2-10 ?????? ????????? ????????????.
//    private int maxNumberOfMessage = 10;
//
//    @Bean
//    public AsyncTaskExecutor asyncTaskExecutor() {
//        ThreadPoolTaskExecutor asyncTaskExecutor = new ThreadPoolTaskExecutor();
//        asyncTaskExecutor.setCorePoolSize(10);
//        asyncTaskExecutor.setMaxPoolSize(40);
//        asyncTaskExecutor.setQueueCapacity(20);
//        asyncTaskExecutor.setThreadNamePrefix("threadPoolExecutor-SimpleMessageListenerContainer-");
//        asyncTaskExecutor.initialize();
//        return asyncTaskExecutor;
//    }
//
//
//    /**
//     * ???????????? ????????? ??????:
//     *      message??? ?????? queue??? ?????? ??????, org.springframework.core.task.TaskRejectedException ????????? ????????????
//     *
//     * ???????????? maxNumberOfMessage ??? ?????? ???????????? ?????? ?????????????????? ????????????.
//     * ??? ????????? ?????? ????????? ???????????? ?????? AWS SQS ??? ????????? ??? ????????? ????????????.(????????? 30???)
//     *  (????????? ?????? ???) ????????? ?????? -> ????????? ?????? -> ?????? ?????? ?????? ?????? ?????? ?????? ??????
//     * @return
//     */
//    @Bean
//    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(Environment environment) {
//        SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
//        factory.setAmazonSqs(amazonSQSAws(environment));
//        factory.setMaxNumberOfMessages(maxNumberOfMessage);
//        factory.setTaskExecutor(asyncTaskExecutor());
//        factory.setWaitTimeOut(10);
//        return factory;
//    }

}
