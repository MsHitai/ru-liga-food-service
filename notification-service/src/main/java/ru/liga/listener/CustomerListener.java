package ru.liga.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.liga.dto.CustomerDeliveryDto;
import ru.liga.exception.DataNotFoundException;
import ru.liga.model.Customer;
import ru.liga.repository.CustomerRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerListener {

    private final CustomerRepository customerRepository;

    /**
     * Method imitates sending notification to the customer about the delivery
     *
     * @param dto has the id of the customer and the status of the order
     */
    @RabbitListener(queues = "customer")
    public void notifyCustomer(CustomerDeliveryDto dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId()).orElseThrow(() ->
                new DataNotFoundException(String.format("The customer by the id=%d is not found in the database",
                        dto.getCustomerId())));
        log.info("Sending notification to the customer by id {} about the delivery status {}", customer.getId(),
                dto.getStatus());
    }
}
