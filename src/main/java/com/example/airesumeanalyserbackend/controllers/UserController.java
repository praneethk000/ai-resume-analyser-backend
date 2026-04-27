package com.example.airesumeanalyserbackend.controllers;

import com.example.airesumeanalyserbackend.dto.response.UserResponseDto;
import com.example.airesumeanalyserbackend.services.user_service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/web/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // @PostMapping("/v1/createUser")
    // public ResponseEntity<String> createUser(@RequestBody CreateUserDto
    // createUserDto){
    // userService.createUser(createUserDto);
    // return ResponseEntity.ok("User created");
    // }

    @GetMapping("/v1/displayUser")
    public ResponseEntity<UserResponseDto> displayUser(@RequestParam("userId") String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/v1/displayAllUsers")
    public ResponseEntity<List<UserResponseDto>> displayAllUsers() {
        List<UserResponseDto> userList = userService.getAllUsers();
        return ResponseEntity.ok(userList);
    }
}
