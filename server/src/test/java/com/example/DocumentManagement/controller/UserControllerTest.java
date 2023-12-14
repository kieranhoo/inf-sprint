package com.example.DocumentManagement.controller;

import com.example.DocumentManagement.entity.DepartmentEntity;
import com.example.DocumentManagement.entity.UsersEntity;
import com.example.DocumentManagement.request.GetUserInfoRequest;
import com.example.DocumentManagement.request.UserCreateRequest;
import com.example.DocumentManagement.response.ListResponse;
import com.example.DocumentManagement.response.OneUserResponse;
import com.example.DocumentManagement.service.DepartmentService;
import com.example.DocumentManagement.service.UsersService;

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
public class UserControllerTest {
    /*
     * TESTING STRATEGY: BLACK-BOX DECISION TABLE TESTING
     *
     * 1. getAllUsers
     * - Input: None
     * - States: Valid User, Empty User
     * - Output: Response List of UserEntity (Non-empty, Empty, Null)
     *
     * 2. getUserById
     * - Input: UserID
     * - States: Valid UserID, Invalid UserID, Empty UserID
     * - Output: Response UserEntity (Non-Null, Null)
     * */

    @InjectMocks
    private UserController userController;
    @Mock
    private UsersService usersService;
    
    @Test
    void testGetAllUsers_ValidUser() {
        // Given
        UsersEntity user1 = new UsersEntity();
        user1.setDepartmentId(1);
        user1.setUsername("ducan1406");
        user1.setEmail("ducan1406@gmail.com");
        user1.setPass("123456");

        UsersEntity user2 = new UsersEntity();
        user2.setDepartmentId(2);
        user2.setUsername("anduckhmt146");
        user2.setEmail("anduckhmt146@gmail.com");
        user2.setPass("123456");

        ListResponse expectedResponse = new ListResponse(Arrays.asList(user1, user2));
    
        when(usersService.getAllUsers()).thenReturn(expectedResponse);
    
        // When
        ResponseEntity<ListResponse> responseEntity = userController.getAllUsers();
    
        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }
    
    @Test
    void testGetAllUsers_EmptyUser() {
        // When
        ListResponse expectedResponse = new ListResponse(List.of());
    
        when(usersService.getAllUsers()).thenReturn(expectedResponse);
    
        // When
        ResponseEntity<ListResponse> responseEntity = userController.getAllUsers();
    
        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }
    
    @Test
    void testGetAllUsers_EmptyUser2() {
        // Given
        UsersEntity user1 = new UsersEntity();
        UsersEntity user2 = new UsersEntity();
       

        ListResponse expectedResponse = new ListResponse(Arrays.asList(user1, user2));
    
        when(usersService.getAllUsers()).thenReturn(expectedResponse);
    
        // When
        ResponseEntity<ListResponse> responseEntity = userController.getAllUsers();
    
        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }
    
    @Test
    void testGetUserById_ValidUserID() {
        // Given
        String userId = "1";
        UsersEntity user = new UsersEntity();
        user.setDepartmentId(1);
        user.setUsername("ducan1406");
        user.setEmail("ducan1406@gmail.com");
        user.setPass("123456");

        when(usersService.getUserById(userId)).thenReturn(user);

        // When
        ResponseEntity<UsersEntity> responseEntity = userController.getUserById(userId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
    }
    
    @Test
    void testGetUserById_UserIDNotFound() {
        // Given
        String userId = "999"; // Assuming a non-existent user

        when(usersService.getUserById(userId)).thenReturn(null);

        // When
        ResponseEntity<UsersEntity> responseEntity = userController.getUserById(userId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void testGetUserById_EmptyUserID() {
        // Given
        String userId = ""; // Assuming a non-existent user

        when(usersService.getUserById(userId)).thenReturn(null);

        // When
        ResponseEntity<UsersEntity> responseEntity = userController.getUserById(userId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
    @Test
    void testGetUserById_NullUserId() {
        // Given
        String nullUserId = null; // Null user ID

        // When
        ResponseEntity<UsersEntity> responseEntity = userController.getUserById(nullUserId);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    void testGetUserById_ValidNumericUserIdAsString() {
        // Given
        String userId = "42"; // A valid numeric user ID

        UsersEntity user = new UsersEntity();
        user.setDepartmentId(1);
        user.setUsername("ducan1406");
        user.setEmail("ducan1406@gmail.com");
        user.setPass("123456");

        when(usersService.getUserById(userId)).thenReturn(user);

        // When
        ResponseEntity<UsersEntity> responseEntity = userController.getUserById(userId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
    }

    @Test
    void testGetOneUser_ValidAccessToken() {
        // Given
        String accessToken = "1ddf23er2df3t343wed5fg36f373jd3";
        GetUserInfoRequest request = new GetUserInfoRequest(accessToken);
        
        // Mock the behavior of usersService.getOneUser
        UsersEntity user = new UsersEntity();
        user.setId(1);
        user.setDepartmentId(1);
        user.setUsername("ducan1406");
        user.setEmail("ducan1406@gmail.com");
        user.setPass("123456");

        OneUserResponse expectedResponse = new OneUserResponse();
        expectedResponse.setId(user.getId());
        expectedResponse.setUserName(user.getUsername());
        expectedResponse.setEmail(user.getEmail());
        expectedResponse.setDepartmentId(user.getDepartmentId());
        when(usersService.getOneUser(accessToken)).thenReturn(expectedResponse);

        // When
        ResponseEntity<OneUserResponse> responseEntity = userController.getOneUser(request);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(usersService).getOneUser(accessToken);
    }
    
}
