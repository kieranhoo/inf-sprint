package com.example.DocumentManagement.controller;

import com.example.DocumentManagement.entity.DepartmentEntity;
import com.example.DocumentManagement.entity.UsersEntity;
import com.example.DocumentManagement.request.UserCreateRequest;
import com.example.DocumentManagement.response.ListResponse;
import com.example.DocumentManagement.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentControllerTest {

    /*
     * TESTING STRATEGY: BLACK-BOX DECISION TABLE TESTING
     *
     * 1. GetAllDepartments
     * - Input: None
     * - States: Valid Departments, Zero Department, Empty Departments in DB
     * - Output: Response List of DepartmentEntity (Non-empty, Empty, Null)
     *
     * 2. GetDepartmentById
     * - Input: DepartmentId
     * - States:
     *   a/ Valid DepartmentId, Invalid DepartmentId
     *   b/ Non-empty Department, Zero Department, Empty Department in DB
     * - Output: Response DepartmentEntity (Non-Null, Null)
     *
     * 3. GetAllUsersFromDepartmentId
     * - Input: DepartmentId
     * - States:
     *   a/ Valid DepartmentId, Invalid DepartmentId
     *   b/ Users in Department, No Users in Department, Zero Department in DB
     * - Output: Response List of UsersEntity (Non-empty, Empty, Null)
     * */

    @InjectMocks
    private DepartmentController underTest;
    @Mock
    private DepartmentService departmentService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void givenDepartments_whenFindAllDepartments_thenReturnOK() {
        // Given
        DepartmentEntity department1 = new DepartmentEntity("Management", "Management Room");
        DepartmentEntity department2 = new DepartmentEntity("IT", "IT Room");
        ListResponse expectedResponse = new ListResponse(Arrays.asList(department1, department2));

        when(departmentService.getAllDepartments()).thenReturn(expectedResponse);

        // When
        ResponseEntity<ListResponse> responseEntity = underTest.getAllDepartments();

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void givenZeroDepartments_whenFindAllDepartments_thenReturnOK() {
        // Given
        ListResponse expectedResponse = new ListResponse(List.of());
        when(departmentService.getAllDepartments()).thenReturn(expectedResponse);

        // When
        ResponseEntity<ListResponse> responseEntity = underTest.getAllDepartments();

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void givenEmptyDepartments_whenFindAllDepartments_thenReturnNotFound() {
        // Given
        DepartmentEntity department1 = new DepartmentEntity();
        DepartmentEntity department2 = new DepartmentEntity();
        ListResponse expectedResponse = new ListResponse(Arrays.asList(department1, department2));

        when(departmentService.getAllDepartments()).thenReturn(expectedResponse);

        // When
        ResponseEntity<ListResponse> responseEntity = underTest.getAllDepartments();

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }
    //////////////////

    @Test
    void givenDepartmentWithinSearchingRange_whenFindDepartmentById_thenReturnOK() {
        // Given
        String departmentId = "1";
        DepartmentEntity department = new DepartmentEntity("Management", "Management Room");
        department.setId(Integer.parseInt(departmentId));
        when(departmentService.getDepartmentById(departmentId)).thenReturn(department);

        // When
        ResponseEntity<?> responseEntity = underTest.getDepartmentById(departmentId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(department, responseEntity.getBody());
    }

    @Test
    void givenDepartmentWithoutSearchingRange_whenFindDepartmentById_thenReturnNotFound() {
        // Given
        String departmentId = "1";
        String queryId = "15";
        DepartmentEntity department = new DepartmentEntity("Management", "Management Room");
        department.setId(Integer.parseInt(departmentId));
        lenient().when(departmentService.getDepartmentById(departmentId)).thenReturn(department);

        // When
        ResponseEntity<?> responseEntity = underTest.getDepartmentById(queryId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void givenZeroDepartments_whenFindDepartmentById_thenReturnNull() {
        // Given
        String departmentId = "1";

        // When
        ResponseEntity<?> responseEntity = underTest.getDepartmentById(departmentId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void givenEmptyDepartmentWithinRange_whenFindDepartmentById_thenReturnSuitableDepartment() {
        // Given
        String departmentId = "1";
        DepartmentEntity department = new DepartmentEntity();
        department.setId(Integer.parseInt(departmentId));
        when(departmentService.getDepartmentById(departmentId)).thenReturn(department);

        // When
        ResponseEntity<?> responseEntity = underTest.getDepartmentById(departmentId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(department, responseEntity.getBody());
    }

    ///////////////////////////

    UsersEntity createMockUser(String email, String password, String role, Integer departmentId){
        UserCreateRequest userCreateRequest = new UserCreateRequest(email, password, role, departmentId);
        String[] name = userCreateRequest.getEmail().split("@");
        UsersEntity user = UsersEntity.builder()
                .departmentId(userCreateRequest.getDepartmentId())
                .username(name[0])
                .email(userCreateRequest.getEmail())
                .isDeleted(false)
                .pass(passwordEncoder.encode(userCreateRequest.getPassword()))
                .build();
        return user;
    }

    @Test
    void givenUsersInDepartmentWithinSearchingRange_whenFindAllUsersByDepartmentId_thenReturnOK() {
        // Given
        String departmentId = "1";
        UsersEntity user1 = createMockUser("abc@gmail.com","123456","Admin",1);
        UsersEntity user2 = createMockUser("def@gmail.com","567890","IT",1);
        ListResponse expectedResponse = new ListResponse(Arrays.asList(user1, user2));
        when(departmentService.getAllUsersByDepartmentId(departmentId)).thenReturn(expectedResponse);

        // When
        ResponseEntity<?> responseEntity = underTest.getAllUsers(departmentId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void givenNoUsersInDepartmentWithinSearchingRange_whenFindAllUsersByDepartmentId_thenReturnOK() {
        // Given
        String departmentId = "1";
        ListResponse expectedResponse = new ListResponse(List.of());
        when(departmentService.getAllUsersByDepartmentId(departmentId)).thenReturn(expectedResponse);

        // When
        ResponseEntity<?> responseEntity = underTest.getAllUsers(departmentId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void givenNoDepartment_whenFindAllUsersByDepartmentId_thenReturnNull() {
        // Given
        String departmentId = "1";

        // When
        ResponseEntity<?> responseEntity = underTest.getAllUsers(departmentId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void givenUsersInDepartmentWithoutSearchingRange_whenFindAllUsersByDepartmentId_thenReturnNull() {
        // Given
        String departmentId = "1";
        String queryId = "15";
        UsersEntity user1 = createMockUser("abc@gmail.com","123456","Admin",1);
        UsersEntity user2 = createMockUser("def@gmail.com","567890","IT",1);
        ListResponse expectedResponse = new ListResponse(Arrays.asList(user1, user2));
        lenient().when(departmentService.getAllUsersByDepartmentId(departmentId)).thenReturn(expectedResponse);
        when(departmentService.getAllUsersByDepartmentId(queryId)).thenReturn(null);

        // When
        ResponseEntity<?> responseEntity = underTest.getAllUsers(queryId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
