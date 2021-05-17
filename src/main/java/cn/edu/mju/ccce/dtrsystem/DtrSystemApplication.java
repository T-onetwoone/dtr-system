package cn.edu.mju.ccce.dtrsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("cn.edu.mju.ccce.dtrsystem.dao")

public class DtrSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(DtrSystemApplication.class, args);
    }

}
