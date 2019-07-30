package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Application {

    public static void main(String[] args) {
       	//System.setProperty("spring.hazelcast.config", "classpath:/config/hazelcast-client.xml");
    	//System.setProperty("hazelcast.client.config", "classpath:config/hazelcast.xml");

        SpringApplication.run(Application.class, args);
    }
}
