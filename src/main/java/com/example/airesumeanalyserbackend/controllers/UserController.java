package com.example.airesumeanalyserbackend.controllers;

import com.example.airesumeanalyserbackend.dto.response.UserResponseDto;
import com.example.airesumeanalyserbackend.models.User;
import com.example.airesumeanalyserbackend.services.user_service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/web/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Returns the profile of the authenticated user only.
     * IDOR fix: ignore the userId param — always return the caller's own profile.
     */
    @GetMapping("/v1/displayUser")
    public ResponseEntity<UserResponseDto> displayUser(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(userService.getUserById(currentUser.getUserId()));
    }

    // displayAllUsers endpoint REMOVED — it allowed any authenticated user to dump
    // the entire user database (IDOR / user enumeration vulnerability).
}
