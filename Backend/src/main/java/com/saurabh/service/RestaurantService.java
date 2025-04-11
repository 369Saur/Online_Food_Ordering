package com.saurabh.service;

import com.saurabh.dto.RestaurantDto;
import com.saurabh.model.Restaurant;
import com.saurabh.model.User;
import com.saurabh.request.CreateRestaurantRequest;

import java.util.List;

public interface RestaurantService {

    public Restaurant createRestaurant(CreateRestaurantRequest request, User user) ;

    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception ;

    public void deleteRestaurant(Long restaurantId) throws Exception ;

    public List<Restaurant> getAllRestaurant() ;

    public List<Restaurant> searchRestaurant(String keyword) ;

    public Restaurant findRestaurantById(Long id) throws Exception ;

    public Restaurant getRestaurantByUserId(Long userId) throws Exception ;

    public RestaurantDto addToFavourites(Long restaurantId, User user) throws Exception ;

    public Restaurant updateRestaurantStatus(Long id) throws Exception ;

}
