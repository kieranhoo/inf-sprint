package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.DepartmentEntity;
import com.example.DocumentManagement.entity.UsersEntity;
import com.example.DocumentManagement.repository.DepartmentRepository;
import com.example.DocumentManagement.repository.UsersRepository;
import com.example.DocumentManagement.response.ListResponse;
import com.example.DocumentManagement.supportFunction.SupportFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService extends SupportFunction {
    private final DepartmentRepository departmentRepository;
    private final UsersRepository usersRepository;

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
        List<UsersEntity> users = usersRepository.findUsersByDepartmentId(id);
        return new ListResponse(users);
    }
}
