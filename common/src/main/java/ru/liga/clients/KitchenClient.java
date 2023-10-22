package ru.liga.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.liga.dto.OrderActionDto;

@FeignClient(name = "kitchen-client", url = "http://localhost:8081")
public interface KitchenClient {

    @PostMapping(value = "/deliveries")
    void updateOrderStatus(@RequestBody OrderActionDto dto);
}
