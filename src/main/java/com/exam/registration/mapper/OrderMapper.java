package com.exam.registration.mapper;

import com.exam.registration.model.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OrderMapper {
    long countOrders();

    int deleteOrderByPrimaryKey(Long id);

    int insertOrder(Order record);

    int insertOrderSelective(Order record);

    Order getOrderByPrimaryKey(Long id);

    List<Order> listOrders();

    int updateOrderByPrimaryKeySelective(Order record);

    int updateOrderByPrimaryKey(Order record);
}