package ru.liga.batisMapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.liga.model.Order;
import ru.liga.model.OrderStatus;

import java.util.List;

@Mapper
@Repository
public interface OrderMapper {

    @Select(value = "select o.* from orders as o " +
            "left join restaurants r on o.restaurant_id = r.id " +
            "left join customers c on o.customer_id = c.id " +
            "left join couriers co on o.courier_id = co.id " +
            "where o.status = #{status} " +
            "limit #{page}")
    List<Order> getOrderByStatus(@Param("status") OrderStatus status, @Param("page") Pageable page);

    @Update(value = "update orders set status = #{status} where id = #{id}")
    void updateOrderStatus(@Param("status") OrderStatus status, @Param("id") Long id);
}
