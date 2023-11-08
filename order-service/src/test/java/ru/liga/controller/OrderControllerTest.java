package ru.liga.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.liga.dto.*;
import ru.liga.exception.DataNotFoundException;
import ru.liga.model.enums.OrderStatus;
import ru.liga.service.OrderService;
import ru.liga.util.JwtTokenUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private OrderService orderService;

    @MockBean
    private JwtTokenUtils jwtTokenUtils;

    private OrderDto dto;

    private Long customerId;
    private UUID orderId;

    private final LocalDateTime created =
            LocalDateTime.of(2023, Month.NOVEMBER, 2, 9, 10, 1);
    private final String timestamp = created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        orderId = UUID.fromString("c8c9a1c0-45ae-4c4e-ad6c-d3129bf05d34");

        dto = OrderDto.builder()
                .id(orderId)
                .timestamp(created)
                .restaurant(new RestaurantDto("Super Tasty"))
                .status(OrderStatus.CUSTOMER_CREATED)
                .items(List.of(new ItemDto()))
                .build();

        customerId = 1L;
    }

    @Test
    void contextLoads() {
        assertThat(orderService).isNotNull();
    }

    @Test
    @WithMockUser
    void testFindAllOrders_OkWhenValid() throws Exception {
        when(orderService.findAllOrders(anyInt(), anyInt(), any(OrderStatus.class)))
                .thenReturn(List.of(dto));

        mvc.perform(get("/orders")
                        .param("pageIndex", "0")
                        .param("pageCount", "20")
                        .param("status", "CUSTOMER_CREATED")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(dto.getId().toString())))
                .andExpect(jsonPath("$[0].restaurant.name", is(dto.getRestaurant().getName())))
                .andExpect(jsonPath("$[0].timestamp", is(timestamp)))
                .andExpect(jsonPath("$[0].items", hasSize(1)));
    }

    @Test
    @WithMockUser
    void testFindOrderById_OkWhenValid() throws Exception {
        when(orderService.findOrderById(dto.getId()))
                .thenReturn(dto);

        mvc.perform(get("/orders/" + dto.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId().toString())))
                .andExpect(jsonPath("$.restaurant.name", is(dto.getRestaurant().getName())))
                .andExpect(jsonPath("$.timestamp", is(timestamp)))
                .andExpect(jsonPath("$.items", hasSize(1)));
    }

    @Test
    void testFindOrderById_404WhenWrongId() throws Exception {
        UUID wrongId = UUID.fromString("a55e3f5b-6d23-4a95-aa6b-ef747ca9dbe1");
        when(orderService.findOrderById(wrongId))
                .thenThrow(DataNotFoundException.class);

        mvc.perform(get("/orders/" + wrongId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void testAddOrder_OkWhenValid() throws Exception {
        OrderToDeliverDto deliverDto = new OrderToDeliverDto(dto.getId(), null, created.plusHours(1));
        String formattedArrival = deliverDto.getEstimatedArrival()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        NewOrderDto newOrderDto = new NewOrderDto(1L, List.of(new MenuItem(2, 1L)));

        when(orderService.addOrder(any(NewOrderDto.class), anyLong()))
                .thenReturn(deliverDto);

        mvc.perform(post("/orders")
                        .content(mapper.writeValueAsString(newOrderDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("customerId", String.valueOf(customerId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dto.getId().toString())))
                .andExpect(jsonPath("$.estimatedArrival", is(formattedArrival)));
    }

    @Test
    @WithMockUser
    void testUpdateOrderStatus_OkWhenValid() throws Exception {
        OrderActionDto actionDto = new OrderActionDto(orderId, OrderStatus.CUSTOMER_PAID);

        mvc.perform(put("/orders/" + orderId)
                        .content(mapper.writeValueAsString(actionDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(orderService, times(1))
                .updateOrderStatus(actionDto, orderId);
    }
}