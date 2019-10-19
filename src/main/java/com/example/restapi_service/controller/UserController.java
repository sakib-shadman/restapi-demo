package com.example.restapi_service.controller;

import com.example.restapi_service.dto.UserDto;
import com.example.restapi_service.errorhandler.UserExists;
import com.example.restapi_service.model.User;
import com.example.restapi_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin

public class UserController {

    @Autowired
    UserRepository userRepository;


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@Valid @RequestBody User userDto) throws UserExists {

        List<User> users = userRepository.findAll();
        if(!users.isEmpty()){
            for (int i = 0; i<users.size(); i++){
                if(userDto.getUserEmail().equalsIgnoreCase(users.get(i).getUserEmail())){
                    throw new UserExists("User Already exists");
                }
            }
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // Strength set as 12
        String userPassword = encoder.encode(userDto.getUserPassword());
        userRepository.save(new User(userDto.getUserName(),userPassword,userDto.getUserEmail(),
                userDto.getUserGender(),userDto.getUserAddress()));
        return  ResponseEntity.ok("Successful");
    }


}
