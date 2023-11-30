package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.DepartmentEntity;
import com.example.DocumentManagement.entity.UserRoleEntity;
import com.example.DocumentManagement.entity.UsersEntity;
import com.example.DocumentManagement.exception.BadRequestException;
import com.example.DocumentManagement.exception.ConflictException;
import com.example.DocumentManagement.exception.NotFoundException;
import com.example.DocumentManagement.repository.DepartmentRepository;
import com.example.DocumentManagement.repository.UserRoleRepository;
import com.example.DocumentManagement.repository.UsersRepository;
import com.example.DocumentManagement.request.UserCreateRequest;
import com.example.DocumentManagement.response.MessageResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Test
    void registerSuccessfully() {
        UserCreateRequest userCreateRequest = new UserCreateRequest(
                "baotin172@gmail.com",
                "pass123",
                "Admin",
                1
        );
        String[] name = userCreateRequest.getEmail().split("@");
        UsersEntity usersEntity = UsersEntity.builder()
                .departmentId(userCreateRequest.getDepartmentId())
                .username(name[0])
                .email(userCreateRequest.getEmail())
                .isDeleted(false)
                .pass(passwordEncoder.encode(userCreateRequest.getPassword()))
                .build();
        UserRoleEntity userRole = new UserRoleEntity(usersEntity.getId(), 1);
        DepartmentEntity departmentEntity = new DepartmentEntity("Development", "Description 1");

        lenient().when(departmentRepository.save(any(DepartmentEntity.class))).thenReturn(departmentEntity);
        when(departmentRepository.findDepartmentById(1)).thenReturn(departmentEntity);
        when(usersRepository.save(any(UsersEntity.class))).thenReturn(usersEntity);
        when(userRoleRepository.save(any(UserRoleEntity.class))).thenReturn(userRole);

        MessageResponse messageResponse = authenticationService.register(userCreateRequest);

        Assertions.assertThat(messageResponse).isNotNull();
        Assertions.assertThat(messageResponse.getMessage()).isEqualTo("Register successfully!");
    }

    @Test
    void registerFailed_BadRequestEmailOrPassword() {
        UserCreateRequest userCreateRequest = new UserCreateRequest(
                "",
                "pass123",
                "Admin",
                1
        );

        Exception exception = assertThrows(BadRequestException.class, () ->{
            authenticationService.register(userCreateRequest);
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo("Email or Password must not be empty.");
    }

    @Test
    void registerFailed_ConflictUser() {
        UserCreateRequest userCreateRequest = new UserCreateRequest(
                "baotin172@gmail.com",
                "pass123",
                "Admin",
                1
        );
        String[] name = userCreateRequest.getEmail().split("@");
        UsersEntity usersEntity = UsersEntity.builder()
                .departmentId(userCreateRequest.getDepartmentId())
                .username(name[0])
                .email(userCreateRequest.getEmail())
                .isDeleted(false)
                .pass(passwordEncoder.encode(userCreateRequest.getPassword()))
                .build();
        lenient().when(usersRepository.save(any(UsersEntity.class))).thenReturn(usersEntity);
        when(usersRepository.findByUsernameOrEmail(name[0], userCreateRequest.getEmail())).thenReturn(usersEntity);

        Exception exception = assertThrows(ConflictException.class, () ->{
            authenticationService.register(userCreateRequest);
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo("User name or email already exists");
    }

    @Test
    void registerFailed_NotFoundDepartment() {
        UserCreateRequest userCreateRequest = new UserCreateRequest(
                "baotin172@gmail.com",
                "pass123",
                "Admin",
                1
        );

        Exception exception = assertThrows(NotFoundException.class, () ->{
            authenticationService.register(userCreateRequest);
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo("Can't not find department!");
    }

    @Test
    void registerFailed_NotFoundRole() {
        UserCreateRequest userCreateRequest = new UserCreateRequest(
                "baotin172@gmail.com",
                "pass123",
                "ABC",
                1
        );
        DepartmentEntity departmentEntity = new DepartmentEntity("Development", "Description 1");

        lenient().when(departmentRepository.save(any(DepartmentEntity.class))).thenReturn(departmentEntity);
        when(departmentRepository.findDepartmentById(1)).thenReturn(departmentEntity);

        Exception exception = assertThrows(NotFoundException.class, () ->{
            authenticationService.register(userCreateRequest);
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo("Role doesn't exist in System!");
    }



}