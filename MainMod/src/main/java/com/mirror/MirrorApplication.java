package com.mirror;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MirrorApplication {
    public static void main(String[] args) {
        SpringApplication.run(MirrorApplication.class, args);
    }

}
