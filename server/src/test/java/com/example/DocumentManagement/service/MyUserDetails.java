package com.example.DocumentManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.DocumentManagement.entity.RolesEntity;
import com.example.DocumentManagement.entity.UserRoleEntity;
import com.example.DocumentManagement.entity.UsersEntity;
import com.example.DocumentManagement.exception.NotFoundException;
import com.example.DocumentManagement.repository.RolesRepository;
import com.example.DocumentManagement.repository.UserRoleRepository;
import com.example.DocumentManagement.repository.UsersRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.List;


public class MyUserDetails {

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private RolesRepository rolesRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername_UserExists() {

        UsersEntity mockUser = new UsersEntity();
        mockUser.setId(1);
        mockUser.setDepartmentId(10);
        mockUser.setUsername("testUser");
        mockUser.setEmail("testuser@example.com");
        mockUser.setPass("encryptedPassword");
        mockUser.setIsDeleted(false);
        mockUser.setDateDeleted(null);

        when(usersRepository.findByUsernameOrEmailAndIsDeletedFalse("testUser", "testUser"))
                .thenReturn(Optional.of(mockUser));

        // Mock roles and authorities
        UserRoleEntity mockUserRoleEntity = new UserRoleEntity();
        mockUserRoleEntity.setId(1);
        mockUserRoleEntity.setUserId(mockUser.getId());
        mockUserRoleEntity.setRoleId(2);

        RolesEntity mockRolesEntity = new RolesEntity();
        mockRolesEntity.setId(2);
        mockRolesEntity.setRole("ROLE_USER");

        when(userRoleRepository.findByUserId(mockUser.getId())).thenReturn(Arrays.asList(mockUserRoleEntity));
        when(rolesRepository.findOneById(mockUserRoleEntity.getRoleId())).thenReturn(mockRolesEntity);

        // Execute the method
        UserDetails userDetails = myUserDetailsService.loadUserByUsername("testUser");

        // Verify the results
        assertNotNull(userDetails);
        assertEquals(mockUser.getUsername(), userDetails.getUsername());
        // Additional assertions for authorities
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(usersRepository.findByUsernameOrEmailAndIsDeletedFalse("nonExistentUser", "nonExistentUser"))
                .thenReturn(Optional.empty());

        // Expect an exception
        assertThrows(UsernameNotFoundException.class, () -> {
            myUserDetailsService.loadUserByUsername("nonExistentUser");
        });
    }

    @Test
    public void testGetAllRoleOfUser_UserHasRoles() {
        UsersEntity mockUser = new UsersEntity();
        mockUser.setId(1);
        mockUser.setDepartmentId(10);
        mockUser.setUsername("testUser");
        mockUser.setEmail("testuser@example.com");
        mockUser.setPass("encryptedPassword");
        mockUser.setIsDeleted(false);
        mockUser.setDateDeleted(null);

        UserRoleEntity mockUserRoleEntity = new UserRoleEntity();
        mockUserRoleEntity.setId(1);
        mockUserRoleEntity.setUserId(mockUser.getId());
        mockUserRoleEntity.setRoleId(2);

        RolesEntity mockRolesEntity = new RolesEntity();
        mockRolesEntity.setId(2);
        mockRolesEntity.setRole("ROLE_USER");

        when(userRoleRepository.findByUserId(mockUser.getId())).thenReturn(Arrays.asList(mockUserRoleEntity));
        when(rolesRepository.findOneById(mockUserRoleEntity.getRoleId())).thenReturn(mockRolesEntity);

        List<RolesEntity> roles = myUserDetailsService.getAllRoleOfUser(mockUser);

        assertNotNull(roles);
        assertFalse(roles.isEmpty());
    }

    @Test
    public void testGetAllRoleOfUser_NoRolesFound() {
        UsersEntity mockUser = new UsersEntity();
        mockUser.setId(1);
        mockUser.setDepartmentId(10);
        mockUser.setUsername("testUser");
        mockUser.setEmail("testuser@example.com");
        mockUser.setPass("encryptedPassword");
        mockUser.setIsDeleted(false);
        mockUser.setDateDeleted(null);

        when(userRoleRepository.findByUserId(mockUser.getId())).thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> {
            myUserDetailsService.getAllRoleOfUser(mockUser);
        });
    }
}
