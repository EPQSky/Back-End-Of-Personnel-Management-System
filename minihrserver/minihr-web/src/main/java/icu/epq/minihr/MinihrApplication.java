package icu.epq.minihr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages = "icu.epq.minihr.mapper")
@EnableScheduling
public class MinihrApplication {

    public static void main(String[] args) {
        SpringApplication.run(MinihrApplication.class, args);
    }

}
