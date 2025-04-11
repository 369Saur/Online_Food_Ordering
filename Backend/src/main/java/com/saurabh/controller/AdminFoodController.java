package com.saurabh.controller;

import com.saurabh.model.Food;
import com.saurabh.model.Restaurant;
import com.saurabh.model.User;
import com.saurabh.request.CreateFoodRequest;
import com.saurabh.response.MessageResponse;
import com.saurabh.service.FoodService;
import com.saurabh.service.RestaurantService;
import com.saurabh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/food")
public class AdminFoodController {

    @Autowired
    private FoodService foodService ;

    @Autowired
    private UserService userService ;

    @Autowired
    private RestaurantService restaurantService ;

    @PostMapping
    public ResponseEntity<Food> createFood(@RequestBody CreateFoodRequest request,
                                           @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt) ;
        Restaurant restaurant = restaurantService.findRestaurantById(request.getRestaurantId()) ;

        Food food = foodService.createFood(request,request.getCategory(),restaurant) ;

        return new ResponseEntity<>(food, HttpStatus.CREATED) ;

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteFood(@PathVariable Long id,
                                                      @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt) ;

        foodService.deleteFood(id) ;

        MessageResponse response = new MessageResponse() ;
        response.setMessage("food deleted successfully...") ;

        return new ResponseEntity<>(response,HttpStatus.OK) ;

    }

    @PutMapping("/{id}")
    public ResponseEntity<Food> updateFoodAvailabilityStatus(@PathVariable Long id,
                                           @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt) ;

        Food food = foodService.updateAvailabilityStatus(id) ;

        return new ResponseEntity<>(food, HttpStatus.CREATED) ;

    }

}
