package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.DepartmentEntity;
import com.example.DocumentManagement.entity.UsersEntity;
import com.example.DocumentManagement.repository.DepartmentRepository;
import com.example.DocumentManagement.repository.UsersRepository;
import com.example.DocumentManagement.request.UserCreateRequest;
import com.example.DocumentManagement.response.ListResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    /*
    * TESTING STRATEGY: BLACK-BOX DECISION TABLE TESTING
    *
    * 1. GetAllDepartments
    * - Input: None
    * - States: Valid Departments, Zero Department, Empty Departments in DB
    * - Output: List of DepartmentEntity (Non-empty, Empty, Null)
    *
    * 2. GetDepartmentById
    * - Input: DepartmentId
    * - States:
    *   a/ Valid DepartmentId, Invalid DepartmentId
    *   b/ Non-empty Department, Zero Department, Empty Department in DB
    * - Output: DepartmentEntity (Non-Null, Null)
    *
    * 3. GetAllUsersFromDepartmentId
    * - Input: DepartmentId
    * - States:
    *   a/ Valid DepartmentId, Invalid DepartmentId
    *   b/ Users in Department, No Users in Department, Zero Department in DB
    * - Output: List of UsersEntity (Non-empty, Empty, Null)
    * */

    @InjectMocks
    private DepartmentService underTest;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void givenDepartments_whenFindAllDepartments_thenReturnAllDepartments() {
        // Given
        DepartmentEntity department1 = new DepartmentEntity("Management", "Management Room");
        DepartmentEntity department2 = new DepartmentEntity("IT", "IT Room");
        List<DepartmentEntity> departments = Arrays.asList(department1, department2);

        when(departmentRepository.findAllDepartments()).thenReturn(departments);

        // When
        ListResponse result = underTest.getAllDepartments();

        // Then
        assertNotNull(result);
        assertEquals(departments.size(), result.getListContent().size());
        assertEquals(department1, result.getListContent().get(0));
        assertEquals(department2, result.getListContent().get(1));

        // Verify that findAllDepartments method was called
        verify(departmentRepository).findAllDepartments();
    }

    @Test
    void givenZeroDepartments_whenFindAllDepartments_thenReturnZeroDepartments() {
        // Given

        // When
        ListResponse result = underTest.getAllDepartments();

        // Then
        assertNotNull(result);
        assertEquals(0, result.getListContent().size());

        // Verify that findAllDepartments method was called
        verify(departmentRepository).findAllDepartments();
    }

    @Test
    void givenEmptyDepartments_whenFindAllDepartments_thenReturnEmptyDepartments() {
        // Given
        DepartmentEntity department1 = new DepartmentEntity();
        DepartmentEntity department2 = new DepartmentEntity();
        List<DepartmentEntity> departments = Arrays.asList(department1, department2);

        when(departmentRepository.findAllDepartments()).thenReturn(departments);

        // When
        ListResponse result = underTest.getAllDepartments();

        // Then
        assertNotNull(result);
        assertEquals(departments.size(), result.getListContent().size());

        // Verify that findAllDepartments method was called
        verify(departmentRepository).findAllDepartments();
    }
    //////////////////

    @Test
    void givenDepartmentWithinSearchingRange_whenFindDepartmentById_thenReturnSuitableDepartment() {
        // Given
        int departmentId = 1;
        DepartmentEntity department = new DepartmentEntity("Management", "Management Room");
        department.setId(departmentId);

        when(departmentRepository.findDepartmentById(departmentId)).thenReturn(department);

        // When
        DepartmentEntity result = underTest.getDepartmentById(Integer.toString(departmentId));

        // Then
        assertEquals(department, result);

        // Verify that findDepartmentById method was called with the correct argument
        verify(departmentRepository).findDepartmentById(departmentId);
    }

    @Test
    void givenDepartmentWithoutSearchingRange_whenFindDepartmentById_thenReturnNull() {
        // Given
        int departmentId = 1;
        int queryId = 15;

        DepartmentEntity department = new DepartmentEntity("Management", "Management Room");
        department.setId(departmentId);

        lenient().when(departmentRepository.findDepartmentById(departmentId)).thenReturn(department);
        when(departmentRepository.findDepartmentById(queryId)).thenReturn(null);

        // When
        DepartmentEntity result = underTest.getDepartmentById(Integer.toString(queryId));

        // Then
        assertNull(result);

        // Verify that findDepartmentById method was called with the correct argument
        verify(departmentRepository).findDepartmentById(queryId);
    }

    @Test
    void givenZeroDepartments_whenFindDepartmentById_thenReturnNull() {
        // Given
        int queryId = 15;

        when(departmentRepository.findDepartmentById(queryId)).thenReturn(null);

        // When
        DepartmentEntity result = underTest.getDepartmentById(Integer.toString(queryId));

        // Then
        assertNull(result);

        // Verify that findDepartmentById method was called with the correct argument
        verify(departmentRepository).findDepartmentById(queryId);
    }

    @Test
    void givenEmptyDepartmentWithinRange_whenFindDepartmentById_thenReturnSuitableDepartment() {
        // Given
        int departmentId = 1;
        DepartmentEntity department = new DepartmentEntity();
        department.setId(departmentId);

        when(departmentRepository.findDepartmentById(departmentId)).thenReturn(department);

        // When
        DepartmentEntity result = underTest.getDepartmentById(Integer.toString(departmentId));

        // Then
        assertEquals(department, result);

        // Verify that findDepartmentById method was called with the correct argument
        verify(departmentRepository).findDepartmentById(departmentId);
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
    void givenUsersInDepartmentWithinSearchingRange_whenFindAllUsersByDepartmentId_thenReturnUsersList() {
        // Given
        int departmentId = 1;

        UsersEntity user1 = createMockUser("abc@gmail.com","123456","Admin",1);
        UsersEntity user2 = createMockUser("def@gmail.com","567890","IT",1);

        List<UsersEntity> users = Arrays.asList(user1, user2);

        when(usersRepository.findUsersByDepartmentId(departmentId)).thenReturn(users);

        // When
        ListResponse result = underTest.getAllUsersByDepartmentId(Integer.toString(departmentId));

        // Then
        assertNotNull(result);
        assertEquals(users.size(), result.getListContent().size());
        assertEquals(user1, result.getListContent().get(0));
        assertEquals(user2, result.getListContent().get(1));

        // Verify that findUsersByDepartmentId method was called with the correct argument
        verify(usersRepository).findUsersByDepartmentId(departmentId);
    }

    @Test
    void givenNoUsersInDepartmentWithinSearchingRange_whenFindAllUsersByDepartmentId_thenReturnEmptyList() {
        // Given
        int departmentId = 1;

        List<UsersEntity> users = List.of();

        when(usersRepository.findUsersByDepartmentId(departmentId)).thenReturn(users);

        // When
        ListResponse result = underTest.getAllUsersByDepartmentId(Integer.toString(departmentId));

        // Then
        assertNotNull(result);
        assertEquals(1, result.getListContent().size());

        // Verify that findUsersByDepartmentId method was called with the correct argument
        verify(usersRepository).findUsersByDepartmentId(departmentId);
    }

    @Test
    void givenNoDepartment_whenFindAllUsersByDepartmentId_thenReturnNull() {
        // Given
        int departmentId = 15;

        // When
        DepartmentEntity result = underTest.getDepartmentById(Integer.toString(departmentId));

        // Then
        assertNull(result);

        // Verify that findDepartmentById method was called with the correct argument
        verify(departmentRepository).findDepartmentById(departmentId);
    }

    @Test
    void givenUsersInDepartmentWithoutSearchingRange_whenFindAllUsersByDepartmentId_thenReturnNull() {
        // Given
        int departmentId = 1;
        int queryId = 15;

        UsersEntity user1 = createMockUser("abc@gmail.com","123456","Admin",1);
        UsersEntity user2 = createMockUser("def@gmail.com","567890","IT",1);

        List<UsersEntity> users = Arrays.asList(user1, user2);

        lenient().when(usersRepository.findUsersByDepartmentId(departmentId)).thenReturn(users);
        when(usersRepository.findUsersByDepartmentId(queryId)).thenReturn(null);

        // When
        ListResponse result = underTest.getAllUsersByDepartmentId(Integer.toString(queryId));

        // Then
        assertNotNull(result);
        assertNull(result.getListContent());

        // Verify that findUsersByDepartmentId method was called with the correct argument
        verify(usersRepository).findUsersByDepartmentId(queryId);
    }
}