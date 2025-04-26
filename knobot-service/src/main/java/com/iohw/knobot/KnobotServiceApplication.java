package com.iohw.knobot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.iohw.knobot.*.mapper")
public class KnobotServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(KnobotServiceApplication.class, args);
    }

}
