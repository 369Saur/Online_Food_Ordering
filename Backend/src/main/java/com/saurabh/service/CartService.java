package com.saurabh.service;

import com.saurabh.model.Cart;
import com.saurabh.model.CartItem;
import com.saurabh.model.User;
import com.saurabh.request.AddCartItemRequest;

public interface CartService {

    public CartItem addItemToCart(AddCartItemRequest request, String jwt) throws Exception ;

    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception ;

    public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception ;

    public Long calculateCartTotals(Cart cart) throws Exception ;

    public Cart fndCartById(Long id) throws Exception ;

    public Cart findCartByUserId(Long userId) throws Exception ;

    public Cart clearCart(Long userId) throws Exception ;

}
