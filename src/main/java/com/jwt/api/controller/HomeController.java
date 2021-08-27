package com.jwt.api.controller;

import com.jwt.api.config.CustomUserDetailService;
import com.jwt.api.config.JwtTokenUtil;
import com.jwt.api.models.JwtRequest;
import com.jwt.api.models.JwtResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("")
    public String hello() {
        return "Hello";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody JwtRequest jwtRequest) {

        try {
            authenticationManager.authenticate (
                new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password", e);
        }

        final UserDetails userDetails = 
                customUserDetailService.loadUserByUsername(jwtRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);  

        return new ResponseEntity<>(new JwtResponse(token), HttpStatus.CREATED);
    }

}
