package com.example.DocumentManagement.controller;

import com.example.DocumentManagement.request.GetUserInfoRequest;
import com.example.DocumentManagement.response.OneUserResponse;
import com.example.DocumentManagement.service.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/common")
@RequiredArgsConstructor
public class CommonController {
    @Autowired
    private CommonService commonService;

    @GetMapping("/user-info")
    public ResponseEntity<OneUserResponse> getOneUser(
            @RequestBody GetUserInfoRequest getUserInfoRequest
    ) {
        return ResponseEntity.ok(commonService.getOneUser(getUserInfoRequest.getAccessToken()));
    }
}
