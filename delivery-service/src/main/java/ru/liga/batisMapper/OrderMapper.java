package ru.liga.batisMapper;

import org.apache.ibatis.annotations.*;
import ru.liga.model.Courier;
import ru.liga.model.Customer;
import ru.liga.model.Order;
import ru.liga.model.Restaurant;
import ru.liga.model.enums.OrderStatus;

import java.util.List;

public interface OrderMapper {

    @Select(value = "select o.* from orders as o " +
            "left join restaurants r on o.restaurant_id = r.id " +
            "left join customers c on o.customer_id = c.id " +
            "left join couriers co on o.courier_id = co.id " +
            "where o.status = #{status} " +
            "limit #{page} ")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "status", column = "status"),
            @Result(property = "timestamp", column = "time_stamp"),
            @Result(property = "customer", column = "customer_id", one = @One(select = "getCustomer")),
            @Result(property = "restaurant", column = "restaurant_id", one = @One(select = "getRestaurant")),
            @Result(property = "courier", column = "courier_id", one = @One(select = "getCourier"))
    })
    List<Order> getOrderByStatus(@Param("status") OrderStatus status, @Param("page") int page);

    @Select("select r.* from restaurants as r where r.id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "address", column = "address"),
            @Result(property = "status", column = "status")
    })
    Restaurant getRestaurant(Long id);

    @Select("select c.* from customers as c where c.id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "email", column = "email"),
            @Result(property = "address", column = "address")
    })
    Customer getCustomer(Long id);

    @Select("select c.* from couriers as c where c.id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "phone", column = "phone"),
            @Result(property = "status", column = "status"),
            @Result(property = "coordinates", column = "coordinates")
    })
    Courier getCourier(Long id);

    @Update(value = "update orders set status = #{status} where id = #{id}")
    void updateOrderStatus(OrderStatus status, Long id);
}
