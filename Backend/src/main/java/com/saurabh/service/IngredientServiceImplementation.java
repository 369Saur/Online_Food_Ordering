package com.saurabh.service;

import com.saurabh.model.IngredientCategory;
import com.saurabh.model.IngredientsItem;
import com.saurabh.model.Restaurant;
import com.saurabh.repository.IngredientCategoryRepository;
import com.saurabh.repository.IngredientItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredientServiceImplementation implements IngredientService {

    @Autowired
    private IngredientItemRepository ingredientItemRepository ;

    @Autowired
    private IngredientCategoryRepository ingredientCategoryRepository ;

    @Autowired
    private RestaurantService restaurantService ;

    @Override
    public IngredientCategory createIngredientCategory(String name, Long restaurantId) throws Exception {

        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId) ;

        IngredientCategory category = new IngredientCategory() ;
        category.setRestaurant(restaurant) ;
        category.setName(name) ;

        return ingredientCategoryRepository.save(category) ;
    }

    @Override
    public IngredientCategory findIngredientCategoryById(Long id) throws Exception {

        Optional<IngredientCategory> optional = ingredientCategoryRepository.findById(id) ;

        if(optional.isEmpty()){
            throw new Exception("ingredient category is not found...") ;
        }

        return optional.get() ;
    }

    @Override
    public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long id) throws Exception {
        restaurantService.findRestaurantById(id) ;
        return ingredientCategoryRepository.findByRestaurantId(id) ;
    }

    @Override
    public IngredientsItem createIngredientItem(Long restaurantId, String ingredientName, Long categoryId) throws Exception {

        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId) ;
        IngredientCategory category = findIngredientCategoryById(categoryId) ;

        IngredientsItem item = new IngredientsItem() ;
        item.setName(ingredientName) ;
        item.setRestaurant(restaurant) ;
        item.setCategory(category) ;

        IngredientsItem ingredient = ingredientItemRepository.save(item) ;
        category.getIngredients().add(ingredient) ;

        return ingredient ;

    }

    @Override
    public List<IngredientsItem> findRestaurantsIngredients(Long restaurantId) {
        return ingredientItemRepository.findByRestaurantId(restaurantId) ;
    }

    @Override
    public IngredientsItem updateStock(Long id) throws Exception {

        Optional<IngredientsItem> optional = ingredientItemRepository.findById(id) ;

        if(optional.isEmpty()){
            throw new Exception("ingredient not found") ;
        }

        IngredientsItem ingredientsItem = optional.get() ;
        ingredientsItem.setInStock(!ingredientsItem.isInStock()) ;

        return ingredientItemRepository.save(ingredientsItem) ;
        
    }
}
