package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.UsersEntity;
import com.example.DocumentManagement.exception.BadRequestException;
import com.example.DocumentManagement.exception.NotFoundException;
import com.example.DocumentManagement.repository.UsersRepository;
import com.example.DocumentManagement.response.ListResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UsersService usersService;
    @Mock
    private UsersRepository usersRepository;
    @Test
    public void givenUsers_whenGetAllUsers_thenReturnAllUsers() {
        // Given
        List<UsersEntity> usersList = Arrays.asList(
                new UsersEntity(1, "User1", "user1@example.com", "password1", false, null),
                new UsersEntity(2, "User2", "user2@example.com", "password2", false, null)
        );
        when(usersRepository.findAllUsers()).thenReturn(usersList);
        // When
        ListResponse result = usersService.getAllUsers();
        // Then
        assertNotNull(result);
        assertEquals(usersList, result.getListContent());
        verify(usersRepository, times(1)).findAllUsers();
    }
    @Test
    public void givenZeroUsers_whenGetAllUsers_thenReturnZeroUsers() {
        // Given
        when(usersRepository.findAllUsers()).thenReturn(Collections.emptyList());
        // When
        ListResponse result = usersService.getAllUsers();
        // Then
        assertNotNull(result);
        assertEquals(Collections.emptyList(), result.getListContent());
        verify(usersRepository, times(1)).findAllUsers();
    }

    @Test
    public void givenTrueId_whenGetUserById_thenReturnUser() {
        // Given
        int userId = 1;
        UsersEntity mockUser = new UsersEntity(userId, "TestUser", "test@example.com", "password", false, null);
        when(usersRepository.findUserById(userId)).thenReturn(mockUser);
        // When
        UsersEntity result = usersService.getUserById(String.valueOf(userId));
        // Then
        assertNotNull(result);
        assertEquals(mockUser, result);
        verify(usersRepository, times(1)).findUserById(userId);
    }
    @Test
    public void givenInvalidId_whenGetUserById_thenThrowBadRequestException() {
        // Given
        String userId = "abcd";

        // When
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            usersService.getUserById(userId);
        });

        // Then
        Assertions.assertEquals("Request must be a Integer", exception.getMessage());
        verifyNoInteractions(usersRepository);
    }
    @Test
    public void givenNullId_whenGetUserById_thenThrowBadRequestException() {
        // Given

        // When
        BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> {
            usersService.getUserById(null);
        });

        // Then
        Assertions.assertEquals("Request must be a Integer", exception.getMessage());
        verifyNoInteractions(usersRepository);
    }
    @Test
    public void givenWrongId_whenGetUserById_thenThrowNotFoundException() {
        // Given
        int userId = 1;
        when(usersRepository.findUserById(1)).thenReturn(null);
        // When
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            usersService.getUserById(String.valueOf(userId));
        });
        // Then
        Assertions.assertEquals("User doesn't exist.", exception.getMessage());
        verify(usersRepository).findUserById(userId);
    }
}
