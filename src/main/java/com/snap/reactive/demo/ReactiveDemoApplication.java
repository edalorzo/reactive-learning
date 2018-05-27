package com.snap.reactive.demo;

import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class ReactiveDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveDemoApplication.class, args);
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
            return executor;
        }

        @Bean
        AsyncRestTemplate asyncRestTemplate() {
            Netty4ClientHttpRequestFactory requestFactory = new Netty4ClientHttpRequestFactory(new NioEventLoopGroup(1));
            return new AsyncRestTemplate(requestFactory);
        }

        @Bean
        WebClient webClient() {
            return WebClient.builder()
                            .baseUrl("http://localhost:4040")
                            .clientConnector(new ReactorClientHttpConnector(opts -> opts.eventLoopGroup(new NioEventLoopGroup(1))))
                            .build();
        }

    }
}
