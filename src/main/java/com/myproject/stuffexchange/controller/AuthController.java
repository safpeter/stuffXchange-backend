package com.myproject.stuffexchange.controller;


import com.myproject.stuffexchange.data.UserRepository;
import com.myproject.stuffexchange.model.NewUser;
import com.myproject.stuffexchange.model.AppUser;
import com.myproject.stuffexchange.model.UserCredentials;
import com.myproject.stuffexchange.security.JwtTokenServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenServices jwtTokenServices;


    public AuthController(AuthenticationManager authenticationManager, JwtTokenServices jwtTokenServices) {
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        this.authenticationManager = authenticationManager;
        this.jwtTokenServices = jwtTokenServices;
    }

    @PostMapping("/signup")
    public String newSignUp(@RequestBody NewUser newUser) {
        String name = newUser.getName();
        String password = newUser.getPassword();
        String email = newUser.getEmail();
        String country = newUser.getCountry();
        if (userRepository.existsByEmail(email)) {
            return "Existing e-mail address! Please find another one!";
        } else if (userRepository.existsByName(name)) {
            return "Existing username! Please find another one!";
        } else {
            AppUser user = AppUser.builder()
                    .name(name)
                    .country(country)
                    .password(passwordEncoder.encode(password))
                    .roles(Collections.singletonList("user"))
                    .dateOfSignUp(LocalDate.now())
                    .email(email)
                    .build();
            userRepository.saveAndFlush(user);
            System.out.println("TEEEE");
            return "You signed up successfully! Now you can log in with your new account!";
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserCredentials credentials) {
        try {
            System.out.println(credentials);
            String username = credentials.getUsername();
            String password = credentials.getPassword();
            // authenticationManager.authenticate calls loadUserByUsername in CustomUserDetailsService
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            System.out.println(roles);

            String token = jwtTokenServices.createToken(username, roles);

            Map<Object, Object> tokenMap = new HashMap<>();
            tokenMap.put("token", token);
            System.out.println("TEEEE");
            return ResponseEntity.ok((tokenMap));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

}