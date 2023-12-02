package com.example.DocumentManagement.controller;

import com.example.DocumentManagement.entity.DepartmentEntity;
import com.example.DocumentManagement.response.ListResponse;
import com.example.DocumentManagement.response.PageResponse;
import com.example.DocumentManagement.service.DepartmentService;
import com.example.DocumentManagement.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/department")
@RequiredArgsConstructor
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("")
    public ResponseEntity<ListResponse> getAllDepartments() {
        ListResponse response = departmentService.getAllDepartments();
        if (response == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<?> getAllUsers(
            @PathVariable(name = "id") String id
    ) {
        ListResponse response = departmentService.getAllUsersByDepartmentId(id);
        if (response == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartmentById(
            @PathVariable(name = "id") String id
    ) {
        DepartmentEntity response = departmentService.getDepartmentById(id);
        if (response == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }
}
