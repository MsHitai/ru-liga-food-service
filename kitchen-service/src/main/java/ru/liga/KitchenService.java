package ru.liga;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients(basePackages = "ru.liga.clients")
public class KitchenService {

    public static void main(String[] args) {
        SpringApplication.run(KitchenService.class, args);
    }
}
