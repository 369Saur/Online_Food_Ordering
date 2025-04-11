package com.saurabh.service;

import com.saurabh.model.Order;
import com.saurabh.model.User;
import com.saurabh.request.OrderRequest;

import java.util.List;

public interface OrderService {
    
    public Order createOrder(OrderRequest order, User user) throws Exception;

    public Order updateOrder(Long orderId, String orderStatus) throws Exception ;

    public void cancelOrder(Long orderId) throws Exception ;

    public List<Order> getUsersOrder(Long userId) throws Exception ;

    public List<Order> getRestaurantsOrder(Long restaurantId, String orderStatus) throws Exception ;

    public Order findOrderById(Long orderId) throws Exception ;
    
}
