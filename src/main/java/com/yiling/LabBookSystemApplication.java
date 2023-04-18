package com.yiling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableTransactionManagement
public class LabBookSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabBookSystemApplication.class, args);
    }

}
