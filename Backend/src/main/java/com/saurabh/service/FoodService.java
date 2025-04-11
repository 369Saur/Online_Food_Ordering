package com.saurabh.service;

import com.saurabh.model.Category;
import com.saurabh.model.Food;
import com.saurabh.model.Restaurant;
import com.saurabh.request.CreateFoodRequest;

import java.util.List;

public interface FoodService {

    public Food createFood(CreateFoodRequest request, Category category, Restaurant restaurant);

    void deleteFood(Long foodId) throws Exception;

    public List<Food> getRestaurantsFood(Long restaurantId,
                                         boolean isVeg,
                                         boolean isNonVeg,
                                         boolean isSeasonal,
                                         String foodCategory
    );

    public List<Food> searchFood(String keyword) ;

    public Food findFoodById(Long foodId) throws Exception ;

    public Food updateAvailabilityStatus(Long foodId) throws Exception ;



}