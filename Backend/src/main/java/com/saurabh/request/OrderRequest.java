package com.saurabh.request;

import com.saurabh.model.Address;
import lombok.Data;

@Data
public class OrderRequest {

    private Long restaurantId ;
    private Address deliveryAddress ;

}
