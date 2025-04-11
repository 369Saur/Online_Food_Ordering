package com.saurabh.service;

import com.saurabh.model.Cart;
import com.saurabh.model.CartItem;
import com.saurabh.model.Food;
import com.saurabh.model.User;
import com.saurabh.repository.CartItemRepository;
import com.saurabh.repository.CartRepository;
import com.saurabh.repository.FoodRepository;
import com.saurabh.request.AddCartItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImplementation implements CartService {

    @Autowired
    private CartRepository cartRepository ;

    @Autowired
    private CartItemRepository cartItemRepository ;

    @Autowired
    private UserService userService ;

    @Autowired
    private FoodService foodService ;


    @Override
    public CartItem addItemToCart(AddCartItemRequest request, String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt) ;

        Food food = foodService.findFoodById(request.getFoodId()) ;

        Cart cart = cartRepository.findByCustomerId(user.getId()) ;

        for(CartItem cartItem : cart.getItem()){

            if(cartItem.getFood().equals(food)){
                int newQuantity = cartItem.getQuantity() + request.getQuantity() ;
                return updateCartItemQuantity(cartItem.getId(),newQuantity) ;
            }

        }

        CartItem newCartItem = new CartItem() ;
        newCartItem.setFood(food) ;
        newCartItem.setCart(cart) ;
        newCartItem.setQuantity(request.getQuantity()) ;
        newCartItem.setIngredients(request.getIngredients()) ;
        newCartItem.setTotalPrice(request.getQuantity()*food.getPrice()) ;

        CartItem savedCartItem = cartItemRepository.save(newCartItem) ;

        cart.getItem().add(savedCartItem) ;

        return savedCartItem ;
    }

    @Override
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception {

        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId) ;

        if(cartItemOptional.isEmpty()){
            throw new Exception("cart item does not exist") ;
        }

        CartItem item = cartItemOptional.get() ;
        item.setQuantity(quantity) ;
        item.setTotalPrice(item.getFood().getPrice()*quantity) ;

        return cartItemRepository.save(item) ;
    }

    @Override
    public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt) ;

        Cart cart = cartRepository.findByCustomerId(user.getId()) ;

        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId) ;

        if(cartItemOptional.isEmpty()){
            throw new Exception("cart item does not exist") ;
        }

        CartItem item = cartItemOptional.get() ;
        cart.getItem().remove(item) ;

        return cartRepository.save(cart) ;
    }

    @Override
    public Long calculateCartTotals(Cart cart) throws Exception {

        Long total = 0L ;

        for(CartItem cartItem : cart.getItem()){
            total += cartItem.getFood().getPrice()*cartItem.getQuantity() ;
        }

        return total ;
    }

    @Override
    public Cart fndCartById(Long id) throws Exception {

        Optional<Cart> optionalCart = cartRepository.findById(id) ;

        if(optionalCart.isEmpty()){
            throw new Exception("cart does not exist") ;
        }

        return optionalCart.get() ;
    }

    @Override
    public Cart findCartByUserId(Long userId) throws Exception {

//        User user = userService.findUserByJwtToken(jwt) ;

        Cart cart = cartRepository.findByCustomerId(userId) ;
        cart.setTotal(calculateCartTotals(cart)) ;

        return cart ;
    }

    @Override
    public Cart clearCart(Long userId) throws Exception {

        Cart cart = findCartByUserId(userId) ;
        cart.getItem().clear() ;

        return cartRepository.save(cart) ;
    }
}
