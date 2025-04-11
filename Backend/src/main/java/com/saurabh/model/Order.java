package com.saurabh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    @ManyToOne
    private User customer ;

    @JsonIgnore
    @ManyToOne // Many Order -> One Restaurant
    private Restaurant restaurant ;

    private Long totalAmount ;

    private String orderStatus ;

    private Date createdAt ;

    @ManyToOne // Many Orders -> One Address
    private Address deliveryAddress ;

    @OneToMany // One Order -> Many Items
    private List<OrderItem> items ;

    private int totalItem ;

    private Long totalPrice ;


}
