package com.example.DocumentManagement.controller;

import com.example.DocumentManagement.entity.UsersEntity;
import com.example.DocumentManagement.request.GetUserInfoRequest;
import com.example.DocumentManagement.response.ListResponse;
import com.example.DocumentManagement.response.OneUserResponse;
import com.example.DocumentManagement.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UsersService usersService;

    @GetMapping("")
    public ResponseEntity<ListResponse> getAllUsers() {
        return ResponseEntity.ok(usersService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsersEntity> getUserById(
            @PathVariable(name = "id") String id
    ) {
        UsersEntity response = usersService.getUserById(id);
        if (response == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/info")
    public ResponseEntity<OneUserResponse> getOneUser(
            @RequestBody GetUserInfoRequest getUserInfoRequest
    ) {
        return ResponseEntity.ok(usersService.getOneUser(getUserInfoRequest.getAccessToken()));
    }
}
