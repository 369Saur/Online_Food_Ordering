package com.saurabh.controller;

import com.saurabh.model.Food;
import com.saurabh.model.Restaurant;
import com.saurabh.model.User;
import com.saurabh.request.CreateFoodRequest;
import com.saurabh.service.FoodService;
import com.saurabh.service.RestaurantService;
import com.saurabh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {

    @Autowired
    private FoodService foodService ;

    @Autowired
    private UserService userService ;

    @Autowired
    private RestaurantService restaurantService ;

    @GetMapping("/search")
    public ResponseEntity<List<Food>> searchFood(@RequestParam String name,
                                           @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt) ;

        List<Food> foods = foodService.searchFood(name) ;

        return new ResponseEntity<>(foods, HttpStatus.OK) ;

    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Food>> getRestaurantFood(
            @RequestParam boolean vegetarian,
            @RequestParam boolean seasonal,
            @RequestParam boolean nonVeg,
            @RequestParam(required = false) String foodCategory,
            @PathVariable Long restaurantId,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt) ;

        List<Food> foods = foodService.getRestaurantsFood(restaurantId,vegetarian,nonVeg,seasonal,foodCategory) ;

        return new ResponseEntity<>(foods, HttpStatus.OK) ;

    }

}
