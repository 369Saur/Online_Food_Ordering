package com.saurabh.controller;

import com.saurabh.model.CartItem;
import com.saurabh.model.Order;
import com.saurabh.model.User;
import com.saurabh.request.AddCartItemRequest;
import com.saurabh.request.OrderRequest;
import com.saurabh.service.OrderService;
import com.saurabh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService ;

    @Autowired
    private UserService userService ;

    @PostMapping("/order")
    public ResponseEntity<Order> createOrder(
            @RequestBody OrderRequest request,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        User user = userService.findUserByJwtToken(jwt) ;

        Order order = orderService.createOrder(request,user) ;

        return new ResponseEntity<>(order, HttpStatus.OK) ;

    }

    @GetMapping("/order/user")
    public ResponseEntity<List<Order>> getOrderHistory(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        User user = userService.findUserByJwtToken(jwt) ;

        List<Order> orders = orderService.getUsersOrder(user.getId()) ;

        return new ResponseEntity<>(orders, HttpStatus.OK) ;

    }

}
