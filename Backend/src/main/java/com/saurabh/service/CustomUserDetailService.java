package com.saurabh.service;

import com.saurabh.model.USER_ROLE;
import com.saurabh.model.User;
import com.saurabh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service // This class is marked as a service that contains business logic
public class CustomUserDetailService implements UserDetailsService {

    @Autowired  // Spring automatically injects the dependency
    private UserRepository userRepository ;

    // This method is called by Spring Security during login to load user details
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Look up the user from the database using the provided username (email)
        User user = userRepository.findByEmail(username) ;

        // If user is not found, throw an exception to stop the login process
        if(user == null){
            throw new UsernameNotFoundException("user not found with email" + username) ;
        }

        // Get the user's role from the User entity
        USER_ROLE role = user.getRole() ;

        // Create a list to hold authorities (roles/permissions)
        List<GrantedAuthority> authorities = new ArrayList<>() ;

        // Add the user's role as a SimpleGrantedAuthority
        authorities.add(new SimpleGrantedAuthority(role.toString())) ;

        // Return a Spring Security User object with email, password, and authorities
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        ) ;

    }

}
