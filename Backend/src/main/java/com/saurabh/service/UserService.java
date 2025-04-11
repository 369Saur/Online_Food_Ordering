package com.saurabh.service;

import com.saurabh.model.User;

public interface UserService {

    public User findUserByJwtToken(String jwt) throws Exception ;

    public User findUserByEmail(String email) throws Exception ;

}
