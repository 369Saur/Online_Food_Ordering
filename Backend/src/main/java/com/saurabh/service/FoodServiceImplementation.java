package com.saurabh.service;

import com.saurabh.model.Category;
import com.saurabh.model.Food;
import com.saurabh.model.Restaurant;
import com.saurabh.repository.FoodRepository;
import com.saurabh.request.CreateFoodRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImplementation implements FoodService{

    @Autowired
    private FoodRepository foodRepository ;

    @Override
    public Food createFood(CreateFoodRequest request, Category category, Restaurant restaurant) {

        Food food = new Food() ;
        food.setFoodCategory(category) ;
        food.setRestaurant(restaurant) ;
        food.setDescription(request.getDescription()) ;
        food.setImages(request.getImages()) ;
        food.setName(request.getName()) ;
        food.setPrice(request.getPrice()) ;
        food.setIngredients(request.getIngredients()) ;
        food.setSeasonal(request.isSeasonal()) ;
        food.setVegetarian(request.isVegetarian()) ;

        Food savedFood = foodRepository.save(food) ;

        restaurant.getFoods().add(savedFood) ;

        return savedFood ;
    }

    @Override
    public void deleteFood(Long foodId) throws Exception {

        Food food = findFoodById(foodId) ;
        food.setRestaurant(null) ;

        foodRepository.save(food) ;

    }

    @Override
    public List<Food> getRestaurantsFood(Long restaurantId,
                                         boolean isVeg,
                                         boolean isNonVeg,
                                         boolean isSeasonal,
                                         String foodCategory) {

        List<Food> foods = foodRepository.findByRestaurantId(restaurantId) ;

        if(isVeg){
            foods = filterByIsVegetarian(foods,isVeg) ;
        }
        if(isNonVeg){
            foods = filterByIsNonVegetarian(foods,isNonVeg) ;
        }
        if(isSeasonal){
            foods = filterByIsSeasonal(foods,isSeasonal) ;
        }
        if(foodCategory != null &&  !foodCategory.equals("")){
            foods = filterByCategory(foods,foodCategory) ;
        }

        return foods ;
    }

    private List<Food> filterByCategory(List<Food> foods, String foodCategory) {
        return foods.stream().filter(food -> {
            if(foodCategory != null){
                return food.getFoodCategory().getName().equals(foodCategory) ;
            }
            return false ;
        }).collect(Collectors.toList()) ;
    }

    private List<Food> filterByIsSeasonal(List<Food> foods, boolean isSeasonal) {
        return foods.stream().filter(food -> food.isSeasonal() == isSeasonal).collect(Collectors.toList()) ;
    }

    private List<Food> filterByIsNonVegetarian(List<Food> foods, boolean isNonVeg) {
        return foods.stream().filter(food -> food.isVegetarian() == false).collect(Collectors.toList()) ;
    }

    private List<Food> filterByIsVegetarian(List<Food> foods, boolean isVeg) {
        return foods.stream().filter(food->food.isVegetarian() == isVeg).collect(Collectors.toList()) ;
    }

    @Override
    public List<Food> searchFood(String keyword) {
        return foodRepository.searchFood(keyword) ;
    }

    @Override
    public Food findFoodById(Long foodId) throws Exception {
        Optional<Food> optionalFood = foodRepository.findById(foodId) ;

        if(optionalFood.isEmpty()){
            throw new Exception("food does not exsist...") ;
        }

        return optionalFood.get() ;
    }

    @Override
    public Food updateAvailabilityStatus(Long foodId) throws Exception {

        Food food = findFoodById(foodId) ;

        food.setAvailable(!food.isAvailable()) ;

        return foodRepository.save(food) ;
    }
}
