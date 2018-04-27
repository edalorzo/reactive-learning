package com.snap.reactive.demo;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.ExecutorServiceAdapter;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;

@SpringBootApplication
public class ReactiveDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveDemoApplication.class, args);
//        RestTemplate restTemplate = new RestTemplate();
//        long start = System.currentTimeMillis();
//        ResponseEntity<Order> response = restTemplate.getForEntity("http://localhost:4040/orders/12345", Order.class);
//        long end = System.currentTimeMillis();
//
//        System.out.printf("Obtained response in %d ms: %s", (end-start), response.getBody());

    }

    @Configuration
    static class ApplicationBeans {

        @Bean
        RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean
        AsyncListenableTaskExecutor taskExecutor() {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(10);
//            executor.setMaxPoolSize(20);
//            executor.setKeepAliveSeconds(15);
//            executor.setQueueCapacity(10);
            return executor;
        }

        @Bean
        ExecutorService executorService() {
            return new ExecutorServiceAdapter(taskExecutor());
        }

        @Bean
        AsyncRestTemplate asyncRestTemplate() {
            Netty4ClientHttpRequestFactory requestFactory = new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1));
            return new AsyncRestTemplate(requestFactory);
        }
    }
}
