package ru.liga.mapper;

import lombok.experimental.UtilityClass;
import ru.liga.dto.CustomerDto;

@UtilityClass
public class CustomerMapper {

    public CustomerDto mapToDto(String address, Double distance) {
        return new CustomerDto(address, distance);
    }
}
