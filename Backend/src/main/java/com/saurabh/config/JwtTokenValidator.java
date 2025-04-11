package com.saurabh.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Client se aane wale request ke header me JWT token ko extract kar raha hai
        String jwt = request.getHeader(JwtConstant.JWT_HEADER) ;

        if(jwt != null){

            // Token "Bearer " se start hota hai, isliye sirf actual token extract kar rahe hain
            jwt = jwt.substring(7) ;

            try{

                // JWT ko verify aur parse karne ke liye secret key use kar raha hai
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes()) ;
                Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody() ;

                // Token se "email" aur "authorities" (roles) extract kar raha hai
                String email = String.valueOf(claims.get("email")) ;
                String authorities = String.valueOf(claims.get("authorities")) ;

                // Authorities ko list me convert kar raha hai taki authentication ke liye use ho sake
                List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities) ;

                // Authentication object create kar raha hai (email + roles ke sath)
                Authentication authentication = new UsernamePasswordAuthenticationToken(email,null,auth) ;

                // SecurityContext me authentication set kar raha hai taki request authorize ho sake
                SecurityContextHolder.getContext().setAuthentication(authentication) ;

            } catch (Exception e) {

                throw new BadCredentialsException("invalid token") ;
            }

        }

        //  Request ko next filter ya controller tak forward kar raha hai
        filterChain.doFilter(request, response) ;

    }
}
