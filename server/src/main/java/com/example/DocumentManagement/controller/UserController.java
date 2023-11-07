package com.example.DocumentManagement.controller;

import com.example.DocumentManagement.entity.UserEntity;
import com.example.DocumentManagement.response.ListResponse;
import com.example.DocumentManagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<ListResponse> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(
            @PathVariable(name = "id") String id
    ) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
