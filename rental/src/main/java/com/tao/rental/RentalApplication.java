package com.tao.rental;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@MapperScan("com.tao.rental.mapper")
public class RentalApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentalApplication.class, args);
    }

}
