package com.yg.dto;

import com.yg.pojo.Order;
import com.yg.pojo.OrderDetail;

import java.util.List;

public class OrderDto extends Order {

    private List<OrderDetail> orderDetails;

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                "orderDetails=" + orderDetails +
                '}';
    }
}
