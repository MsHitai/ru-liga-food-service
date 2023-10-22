package ru.liga.clients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "courier-client")
public interface CourierClient {
}
