package ru.liga.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderToDeliverDto {

    private Long id;
    @JsonProperty(namespace = "secret_payment_url")
    private String url;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(namespace = "estimated_time_of_arrival")
    private LocalDateTime estimatedArrival;
}
