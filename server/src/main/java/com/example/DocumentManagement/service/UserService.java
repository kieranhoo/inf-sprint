package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.DepartmentEntity;
import com.example.DocumentManagement.entity.UserEntity;
import com.example.DocumentManagement.repository.DepartmentRepository;
import com.example.DocumentManagement.repository.UserRepository;
import com.example.DocumentManagement.response.ListResponse;
import com.example.DocumentManagement.supportFunction.SupportFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService extends SupportFunction {
    private final UserRepository userRepository;
    public ListResponse getAllUsers() {
        List<UserEntity> users = userRepository.findAllUsers();
        return new ListResponse(users);
    }

    public UserEntity getUserById(String idFromPathVariable) {
        int id = checkRequest(idFromPathVariable);
        return userRepository.findUserById(id);
    }
}
