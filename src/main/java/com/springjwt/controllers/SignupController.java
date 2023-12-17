package com.springjwt.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springjwt.dto.AuthenticationDTO;
import com.springjwt.dto.SignupDTO;
import com.springjwt.dto.UserDTO;
import com.springjwt.services.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignupController {

    @Autowired
    private AuthService authService;
    @Autowired
    private KafkaTemplate<String, AuthenticationDTO> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signupUser(@RequestBody SignupDTO signupDTO) throws JsonProcessingException {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
       UserDTO createdUser = authService.createUser(signupDTO);
       authenticationDTO.setEmail(signupDTO.getEmail());
       authenticationDTO.setPassword(signupDTO.getPassword());
       kafkaTemplate.send("user", authenticationDTO);
       if (createdUser == null){
           return new ResponseEntity<>("User not created, come again later!", HttpStatus.BAD_REQUEST);
       }
       return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

}
