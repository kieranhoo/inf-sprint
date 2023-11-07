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
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<ListResponse> getAllUsers(
            @PathVariable(name = "id") String id
    ) {
        return ResponseEntity.ok(departmentService.getAllUsersByDepartmentId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentEntity> getDepartmentById(
            @PathVariable(name = "id") String id
    ) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }
}
