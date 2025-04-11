package com.saurabh.controller;

import com.saurabh.config.JwtProvider;
import com.saurabh.model.Cart;
import com.saurabh.model.USER_ROLE;
import com.saurabh.model.User;
import com.saurabh.repository.CartRepository;
import com.saurabh.repository.UserRepository;
import com.saurabh.request.LoginRequest;
import com.saurabh.response.AuthResponse;
import com.saurabh.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController // This class is a REST controller that handles HTTP requests and returns JSON responses
@RequestMapping("/auth") // All routes in this controller will start with "/auth"
public class AuthController {

    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private PasswordEncoder passwordEncoder ;

    @Autowired
    private JwtProvider jwtProvider ;

    @Autowired
    private CustomUserDetailService customUserDetailService ;

    @Autowired
    private CartRepository cartRepository ;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {

        // Check if a user with the given email already exists in the database
        User isEmailExist = userRepository.findByEmail(user.getEmail()) ;

        // If user already exists, throw an exception to stop registration
        if(isEmailExist != null){
            throw new Exception("Email is already used with another account") ;
        }

        // Create a new user object and set user details
        User createUser = new User() ;
        createUser.setEmail(user.getEmail()) ;
        createUser.setFullName(user.getFullName()) ;
        createUser.setRole(user.getRole()) ;
        // Encode the password before saving it (for security)
        createUser.setPassword(passwordEncoder.encode(user.getPassword())) ;

        // Save the new user to the database
        User savedUser = userRepository.save(createUser) ;

        // Create a new cart and assign it to the newly registered user
        Cart cart = new Cart() ;
        cart.setCustomer(savedUser) ;
        cartRepository.save(cart) ;

        // Authenticate the user using Spring Security
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword()) ;
        SecurityContextHolder.getContext().setAuthentication(authentication) ;

        // Generate a JWT token for the authenticated user
        String jwt = jwtProvider.generateToken(authentication) ;

        // Create an authentication response object to send back to the user
        AuthResponse authResponse = new AuthResponse() ;
        authResponse.setJwt(jwt) ;
        authResponse.setMessage("Register Success") ;
        authResponse.setRole(savedUser.getRole()) ;

        // Return the response with status code 201 (CREATED)
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED) ;

    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signing(@RequestBody LoginRequest request){

        String username = request.getEmail() ;
        String password = request.getPassword() ;

        Authentication authentication = authenticate(username,password) ;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities() ;

        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority() ;

        String jwt = jwtProvider.generateToken(authentication) ;

        AuthResponse authResponse = new AuthResponse() ;
        authResponse.setJwt(jwt) ;
        authResponse.setMessage("Login Success") ;
        authResponse.setRole(USER_ROLE.valueOf(role)) ;


        return new ResponseEntity<>(authResponse, HttpStatus.OK) ;

    }

    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = customUserDetailService.loadUserByUsername(username) ;
        if(userDetails == null) {

            throw new BadCredentialsException("Invalid username....") ;

        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){

            throw new BadCredentialsException("Invalid password....") ;

        }

        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities()) ;

    }

}
