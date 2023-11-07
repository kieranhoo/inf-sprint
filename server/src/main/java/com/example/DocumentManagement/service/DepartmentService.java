package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.DepartmentEntity;
import com.example.DocumentManagement.entity.DocumentEntity;
import com.example.DocumentManagement.entity.UserEntity;
import com.example.DocumentManagement.repository.DepartmentRepository;
import com.example.DocumentManagement.repository.UserRepository;
import com.example.DocumentManagement.repository.VersionRepository;
import com.example.DocumentManagement.response.ListResponse;
import com.example.DocumentManagement.supportFunction.SupportFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService extends SupportFunction {
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public ListResponse getAllDepartments() {
        List<DepartmentEntity> departments = departmentRepository.findAllDepartments();
        return new ListResponse(departments);
    }

    public DepartmentEntity getDepartmentById(String idFromPathVariable) {
        int id = checkRequest(idFromPathVariable);
        return departmentRepository.findDepartmentById(id);
    }

    public ListResponse getAllUsersByDepartmentId(String idFromPathVariable) {
        int id = checkRequest(idFromPathVariable);
        List<UserEntity> users = userRepository.findUsersByDepartmentId(id);
        return new ListResponse(users);
    }
}
