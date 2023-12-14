package com.example.DocumentManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.DocumentManagement.entity.RolesEntity;
import com.example.DocumentManagement.entity.UserRoleEntity;
import com.example.DocumentManagement.entity.UsersEntity;
import com.example.DocumentManagement.exception.NotFoundException;
import com.example.DocumentManagement.repository.RolesRepository;
import com.example.DocumentManagement.repository.UserRoleRepository;
import com.example.DocumentManagement.repository.UsersRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class MyUserDetails {

    /*
    * TESTING STRATEGY: BLACK-BOX DECISION TABLE TESTING
    *
    * 1. loadUserByUsername
    * - Input: String username
    * - States: Existed Username, NotFound Username, Empty Username
    * - Output: UserDetails (UserEntity, List<UserRoles>)
    *
    * 2. getAllRoleOfUser
    * - Input: UsersEntity user
    * - States: UserNotFound, UserHasRoles, UserNotHaveRoles
    * - Output: List<RolesEntity>
    *
    * 3. createMyUserDetails
    * - Input: UsersEntity user
    * - States: UserNotFound, UserHasRoles, UserNotHaveRoles
    * - Output: UserDetails (UserEntity, List<UserRoles>)
    * */

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
    public void testLoadUserByUsername_UserNameExists() {
        // Mock User
        UsersEntity mockUser = new UsersEntity();
        mockUser.setId(1);
        mockUser.setDepartmentId(1);
        mockUser.setUsername("ducan1406");
        mockUser.setEmail("ducan1406@gmail.com");
        mockUser.setPass("123456");

        // Mock Role

        // Role 1
        RolesEntity mockRole1 = new RolesEntity();
        mockRole1.setId(1);
        mockRole1.setRole("READ");
        // Role 2
        RolesEntity mockRole2 = new RolesEntity();
        mockRole2.setId(2);
        mockRole2.setRole("WRITE");

        // Mock User Role

        // User Role 1
        UserRoleEntity mockUserRole1 = new UserRoleEntity();
        mockUserRole1.setId(1);
        mockUserRole1.setUserId(mockUser.getId());
        mockUserRole1.setRoleId(1);
        // User Role 2
        UserRoleEntity mockUserRole2 = new UserRoleEntity();
        mockUserRole2.setId(2);
        mockUserRole2.setUserId(mockUser.getId());
        mockUserRole2.setRoleId(2);

        when(usersRepository.findByUsernameOrEmailAndIsDeletedFalse("ducan1406", "ducan1406"))
                .thenReturn(Optional.of(mockUser));

        when(userRoleRepository.findByUserId(mockUser.getId()))
                .thenReturn(Arrays.asList(mockUserRole1, mockUserRole2));

        when(rolesRepository.findOneById(mockUserRole1.getRoleId())).thenReturn(mockRole1);

        when(rolesRepository.findOneById(mockUserRole2.getRoleId())).thenReturn(mockRole2);

        // Load UserDetails by loadUserByUsername
        UserDetails userDetails = myUserDetailsService.loadUserByUsername("ducan1406");

        // Test information (username, password) of users
        assertNotNull(userDetails);
        assertEquals(mockUser.getUsername(), userDetails.getUsername());
        assertEquals(mockUser.getPass(), userDetails.getPassword());
    
        // Test authorities of users
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertFalse(authorities.isEmpty());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("READ")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("WRITE")));
    }

    @Test
    public void testLoadUserByUsername_UserNameNotFound() {
        // Mock User
        UsersEntity mockUser = new UsersEntity();
        mockUser.setId(1);
        mockUser.setDepartmentId(1);
        mockUser.setUsername("ducan1406");
        mockUser.setEmail("ducan1406@gmail.com");
        mockUser.setPass("123456");

        // Mock Role

        // Role 1
        RolesEntity mockRole1 = new RolesEntity();
        mockRole1.setId(1);
        mockRole1.setRole("READ");
        // Role 2
        RolesEntity mockRole2 = new RolesEntity();
        mockRole2.setId(2);
        mockRole2.setRole("WRITE");

        // Mock User Role

        // User Role 1
        UserRoleEntity mockUserRole1 = new UserRoleEntity();
        mockUserRole1.setId(1);
        mockUserRole1.setUserId(mockUser.getId());
        mockUserRole1.setRoleId(1);
        // User Role 2
        UserRoleEntity mockUserRole2 = new UserRoleEntity();
        mockUserRole2.setId(2);
        mockUserRole2.setUserId(mockUser.getId());
        mockUserRole2.setRoleId(2);

        when(usersRepository.findByUsernameOrEmailAndIsDeletedFalse("ducan14062002", "ducan14062002"))
                .thenReturn(Optional.empty());

        when(userRoleRepository.findByUserId(mockUser.getId()))
                .thenReturn(Arrays.asList(mockUserRole1, mockUserRole2));

        when(rolesRepository.findOneById(mockUserRole1.getRoleId())).thenReturn(mockRole1);

        when(rolesRepository.findOneById(mockUserRole2.getRoleId())).thenReturn(mockRole2);

        assertThrows(UsernameNotFoundException.class, () -> {
            myUserDetailsService.loadUserByUsername("ducan14062002");
        });
    }

    @Test
    public void testLoadUserByUsername_UserNameEmpty() {
        // Mock User

        // Assert that a UsernameNotFoundException is thrown when the username is empty
        assertThrows(UsernameNotFoundException.class, () -> {
            myUserDetailsService.loadUserByUsername("");
        });
}
 
    @Test
    public void testGetAllRoleOfUser_UserHasRoles() {
        // Mock User
        UsersEntity mockUser = new UsersEntity();
        mockUser.setId(1);
        mockUser.setDepartmentId(1);
        mockUser.setUsername("ducan1406");
        mockUser.setEmail("ducan1406@gmail.com");
        mockUser.setPass("123456");

        // Mock User Role 1
        UserRoleEntity mockUserRoleEntity = new UserRoleEntity();
        mockUserRoleEntity.setId(1);
        mockUserRoleEntity.setUserId(mockUser.getId());
        mockUserRoleEntity.setRoleId(2);

        // Mock Role 1
        RolesEntity mockRolesEntity = new RolesEntity();
        mockRolesEntity.setId(2);
        mockRolesEntity.setRole("WRITE");

        when(userRoleRepository.findByUserId(mockUser.getId()))
                .thenReturn(Arrays.asList(mockUserRoleEntity));

        when(rolesRepository.findOneById(mockUserRoleEntity.getRoleId()))
                .thenReturn(mockRolesEntity);

        List<RolesEntity> roles = myUserDetailsService.getAllRoleOfUser(mockUser);

        // Check roles
        assertNotNull(roles);
        assertFalse(roles.isEmpty());
        assertTrue(roles.contains(mockRolesEntity));
    }

    public List<RolesEntity> getAllRoleOfUser(UsersEntity user) {
    List<UserRoleEntity> userRoles = userRoleRepository.findByUserId(user.getId());
        if (userRoles.isEmpty()) {
            throw new NotFoundException("Role of user is empty");
        }
        List<RolesEntity> roles = new ArrayList<>();
        for (UserRoleEntity userRole : userRoles) {
            RolesEntity role = rolesRepository.findOneById(userRole.getRoleId());
            roles.add(role);
        }
        return roles;
    }


    @Test
    public void testGetAllRoleOfUser_UserNotFound() {
        // Mock User
        UsersEntity mockUser = new UsersEntity();
        mockUser.setId(1);
        mockUser.setDepartmentId(1);
        mockUser.setUsername("ducan1406");
        mockUser.setEmail("ducan1406@gmail.com");
        mockUser.setPass("123456");

        when(userRoleRepository.findByUserId(mockUser.getId())).thenReturn(Collections.emptyList());
       
        assertThrows(NotFoundException.class, () -> {
            myUserDetailsService.getAllRoleOfUser(mockUser);
        });
    }


    @Test
    public void testGetAllRoleOfUser_NoRolesFound() {
        // Mock User
        UsersEntity mockUser = new UsersEntity();
        mockUser.setId(1);
        mockUser.setDepartmentId(1);
        mockUser.setUsername("ducan1406");
        mockUser.setEmail("ducan1406@gmail.com");
        mockUser.setPass("123456");

        when(userRoleRepository.findByUserId(mockUser.getId()))
                .thenReturn(Collections.emptyList());

        assertThrows(NotFoundException.class, () -> {
            myUserDetailsService.getAllRoleOfUser(mockUser);
        });
    }

    @Test
    public void testCreateMyUserDetails_UserHasRoles() {
        // Mock User
        UsersEntity mockUser = new UsersEntity();
        mockUser.setId(1);
        mockUser.setDepartmentId(1);
        mockUser.setUsername("ducan1406");
        mockUser.setEmail("ducan1406@gmail.com");
        mockUser.setPass("123456");

        // Mock Role
        RolesEntity mockRolesEntity = new RolesEntity();
        mockRolesEntity.setId(2);
        mockRolesEntity.setRole("WRITE");

        // Mock User Role
        UserRoleEntity mockUserRoleEntity = new UserRoleEntity();
        mockUserRoleEntity.setId(1);
        mockUserRoleEntity.setUserId(mockUser.getId());
        mockUserRoleEntity.setRoleId(2);

        // Mock the behavior of getAllRoleOfUser to return roles
        when(userRoleRepository.findByUserId(mockUser.getId())).thenReturn(Collections.singletonList(mockUserRoleEntity));

        // Execute the method
        UserDetails userDetails = myUserDetailsService.createMyUserDetails(mockUser);

        // Verify the results
        assertNotNull(userDetails);
        assertEquals(mockUser.getUsername(), userDetails.getUsername());

        if (mockRolesEntity.getRole() == null && !mockRolesEntity.getRole().isEmpty()) {
            assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority(mockRolesEntity.getRole())));
        }
    }

    @Test
    public void testCreateMyUserDetails_UserNotFound() {
        // Mock User (User not found)
        UsersEntity mockUser = new UsersEntity();
        mockUser.setId(1);
        mockUser.setDepartmentId(1);
        mockUser.setUsername("ducan1406");
        mockUser.setEmail("ducan1406@gmail.com");
        mockUser.setPass("123456");

        // Mock the behavior of getAllRoleOfUser to return empty roles list
        when(userRoleRepository.findByUserId(mockUser.getId())).thenReturn(Collections.emptyList());
    }

    @Test
    public void testCreateMyUserDetails_UserNotHaveRoles() {
        // Mock User
        UsersEntity mockUser = new UsersEntity();
        mockUser.setId(1);
        mockUser.setDepartmentId(1);
        mockUser.setUsername("ducan1406");
        mockUser.setEmail("ducan1406@gmail.com");
        mockUser.setPass("123456");
    
        // Mock the behavior of getAllRoleOfUser to return empty roles list
        when(userRoleRepository.findByUserId(mockUser.getId())).thenReturn(Collections.emptyList());
    
        // Mock an empty role (null role field)
        RolesEntity mockRolesEntity = new RolesEntity();
        mockRolesEntity.setId(2);
        mockRolesEntity.setRole(null);
    
        // Mock the behavior of rolesRepository.findOneById to return an empty role
        when(rolesRepository.findOneById(mockRolesEntity.getId())).thenReturn(mockRolesEntity);
    
        // Expect an exception to be thrown when calling createMyUserDetails
        assertThrows(NotFoundException.class, () -> {
            myUserDetailsService.createMyUserDetails(mockUser);
        });
    }


}
