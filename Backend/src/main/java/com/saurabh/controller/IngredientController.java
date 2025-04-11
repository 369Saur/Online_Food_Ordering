package com.saurabh.controller;

import com.saurabh.model.IngredientCategory;
import com.saurabh.model.IngredientsItem;
import com.saurabh.request.IngredientCategoryRequest;
import com.saurabh.request.IngredientRequest;
import com.saurabh.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ingredients")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService ;

    @PostMapping("/category")
    public ResponseEntity<IngredientCategory> createIngredientCategory(
            @RequestBody IngredientCategoryRequest request
            ) throws Exception {

        IngredientCategory category = ingredientService.createIngredientCategory(request.getName(),request.getRestaurantId()) ;

        return new ResponseEntity<>(category, HttpStatus.CREATED) ;

    }

    @PostMapping
    public ResponseEntity<IngredientsItem> createIngredientItem(
            @RequestBody IngredientRequest request
    ) throws Exception {

        IngredientsItem item = ingredientService.createIngredientItem(request.getRestaurantId(),request.getName(), request.getCategoryId()) ;

        return new ResponseEntity<>(item, HttpStatus.CREATED) ;

    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<IngredientsItem> updateIngredientStock(
            @PathVariable Long id
    ) throws Exception {

        IngredientsItem item = ingredientService.updateStock(id) ;

        return new ResponseEntity<>(item, HttpStatus.OK) ;

    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<IngredientsItem>> getRestaurantIngredients(
            @PathVariable Long id
    ) throws Exception {

        List<IngredientsItem> items = ingredientService.findRestaurantsIngredients(id) ;

        return new ResponseEntity<>(items, HttpStatus.CREATED) ;

    }

    @GetMapping("/restaurant/{id}/category")
    public ResponseEntity<List<IngredientCategory>> getRestaurantIngredientsCategory(
            @PathVariable Long id
    ) throws Exception {

        List<IngredientCategory> categories = ingredientService.findIngredientCategoryByRestaurantId(id) ;

        return new ResponseEntity<>(categories, HttpStatus.CREATED) ;

    }

}
