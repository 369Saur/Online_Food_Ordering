package com.saurabh.service;

import com.saurabh.dto.RestaurantDto;
import com.saurabh.model.Address;
import com.saurabh.model.Restaurant;
import com.saurabh.model.User;
import com.saurabh.repository.AddressRepository;
import com.saurabh.repository.RestaurantRepository;
import com.saurabh.repository.UserRepository;
import com.saurabh.request.CreateRestaurantRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImplementation implements RestaurantService{

    @Autowired
    private RestaurantRepository restaurantRepository ;

    @Autowired
    private AddressRepository addressRepository ;

    @Autowired
    private UserRepository userRepository ;

    @Override
    public Restaurant createRestaurant(CreateRestaurantRequest request, User user) {

        Address address = addressRepository.save(request.getAddress()) ;

        Restaurant restaurant = new Restaurant() ;
        restaurant.setAddress(address) ;
        restaurant.setContactInformation(request.getContactInformation()) ;
        restaurant.setCuisineType(request.getCuisineType()) ;
        restaurant.setDescription(request.getDescription()) ;
        restaurant.setImages(request.getImages()) ;
        restaurant.setName(request.getName()) ;
        restaurant.setOpeningHours(request.getOpeningHours()) ;
        restaurant.setRegistrationDate(LocalDateTime.now()) ;
        restaurant.setOwner(user) ;

        return restaurantRepository.save(restaurant) ;
    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception {

        Restaurant restaurant = findRestaurantById(restaurantId) ;
        if(restaurant.getCuisineType() != null){
            restaurant.setCuisineType(updatedRestaurant.getCuisineType()) ;
        }
        if(restaurant.getDescription() != null){
            restaurant.setDescription(updatedRestaurant.getDescription()) ;
        }
        if(restaurant.getName() != null){
            restaurant.setName(updatedRestaurant.getName()) ;
        }

        return restaurantRepository.save(restaurant) ;
    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws Exception {

        Restaurant restaurant = findRestaurantById(restaurantId) ;
        restaurantRepository.delete(restaurant) ;

    }

    @Override
    public List<Restaurant> getAllRestaurant() {
        return restaurantRepository.findAll() ;
    }

    @Override
    public List<Restaurant> searchRestaurant(String keyword) {
        return restaurantRepository.findBySearchQuery(keyword) ;
    }

    @Override
    public Restaurant findRestaurantById(Long id) throws Exception {
        Optional<Restaurant> optional = restaurantRepository.findById(id) ;
        if(optional.isEmpty()){
            throw new Exception("Restaurant not found with id" + id) ;
        }
        return optional.get() ;
    }

    @Override
    public Restaurant getRestaurantByUserId(Long userId) throws Exception {
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId) ;
        if(restaurant == null){
            throw new Exception("Restaurant not found with owner id" + userId) ;
        }
        return restaurant ;
    }

    @Override
    public RestaurantDto addToFavourites(Long restaurantId, User user) throws Exception {

        Restaurant restaurant = findRestaurantById(restaurantId) ;

        RestaurantDto dto = new RestaurantDto() ;
        dto.setDescription(restaurant.getDescription()) ;
        dto.setImages(restaurant.getImages()) ;
        dto.setTitle(restaurant.getName()) ;
        dto.setId(restaurantId) ;

        boolean isFavourited = false ;
        List<RestaurantDto> favourites = user.getFavourites() ;
        for(RestaurantDto favourite : favourites){
            if(favourite.getId().equals(restaurantId)){
                isFavourited = true ;
                break ;
            }
        }
        if(isFavourited){
            favourites.removeIf(favourite->favourite.getId().equals(restaurantId)) ;
        }
        else{
            favourites.add(dto) ;
        }

        userRepository.save(user) ;

        return dto ;

    }

    @Override
    public Restaurant updateRestaurantStatus(Long id) throws Exception {

        Restaurant restaurant = findRestaurantById(id) ;
        restaurant.setOpen(!restaurant.isOpen()) ;

        return restaurantRepository.save(restaurant) ;

    }
}
