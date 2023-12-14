package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.MyUserDetails;
import com.example.DocumentManagement.entity.*;
import com.example.DocumentManagement.exception.BadRequestException;
import com.example.DocumentManagement.exception.ConflictException;
import com.example.DocumentManagement.exception.NotFoundException;
import com.example.DocumentManagement.repository.*;
import com.example.DocumentManagement.request.AuthenticationRequest;
import com.example.DocumentManagement.request.UserCreateRequest;
import com.example.DocumentManagement.response.AuthenticationResponse;
import com.example.DocumentManagement.response.MessageResponse;
import io.jsonwebtoken.MalformedJwtException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private TokenTypeRepository tokenTypeRepository;

    @Mock
    private TokenCategoryRepository tokenCategoryRepository;

    @Mock
    private MyUserDetailsService myUserDetailsService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Test
    void register_Successfully() {
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

        Exception exception = assertThrows(BadRequestException.class, () -> {
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
        when(usersRepository.findByUsernameOrEmail(name[0], userCreateRequest.getEmail())).thenReturn(usersEntity);

        Exception exception = assertThrows(ConflictException.class, () -> {
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

        Exception exception = assertThrows(NotFoundException.class, () -> {
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

        when(departmentRepository.findDepartmentById(1)).thenReturn(departmentEntity);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            authenticationService.register(userCreateRequest);
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo("Role doesn't exist in System!");
    }

    @Test
    void authenticate_Successfully() {
        /*------------------Given--------------------*/
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("hubatin@gmail.com", "pass123");
        UsersEntity usersEntity = createMockUser("hubatin@gmail.com", "pass123", "Admin", 1);
        MyUserDetails myUserDetails = myUserDetailsService.createMyUserDetails(usersEntity);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                usersEntity.getEmail(),
                usersEntity.getPass()
        ));
        TokenTypeEntity tokenTypeEntity = createTokenType();
        TokenCategoryEntity tokenCategoryAccess = createTokenCategory("ACCESS");
        TokenCategoryEntity tokenCategoryRefresh = createTokenCategory("REFRESH");

        //Create user with password encode to save, other field is the same as userEntity
        UsersEntity userPasswordEncode = createMockUser("hubatin@gmail.com", "pass123", "Admin", 1);
        userPasswordEncode.setPass(passwordEncoder.encode("pass123"));

        /*------------------When--------------------*/
        when(usersRepository.findByUsernameOrEmail(usersEntity.getEmail(), usersEntity.getEmail()))
                .thenReturn(usersEntity);

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                usersEntity.getEmail(),
                usersEntity.getPass()
        ))).thenReturn(authentication);
        when(myUserDetailsService.createMyUserDetails(usersEntity)).thenReturn(myUserDetails);

        //Token Type
        when(tokenTypeRepository.findTypeByTokenTypeName("BEARER")).thenReturn(tokenTypeEntity);

        //Token Category
        //Access
        when(tokenCategoryRepository.findCategoryByTokenCategoryName("ACCESS")).thenReturn(tokenCategoryAccess);
        //Refresh
        when(tokenCategoryRepository.findCategoryByTokenCategoryName("REFRESH")).thenReturn(tokenCategoryRefresh);

        /*------------------Then--------------------*/
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);
        Assertions.assertThat(authenticationResponse).isNotNull();
    }

    @Test
    void authenticate_NotFoundUser() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("hubatin@gmail.com", "pass123");

        Exception exception = assertThrows(NotFoundException.class, () -> {
            authenticationService.authenticate(authenticationRequest);
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo("Not found user name or email");
    }

    @Test
    void refreshToken_Successfully() {
        String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJFTVBMT1lFRSJdLCJ1c2VySWQiOiIxIiwic3ViIjoiYmFvdGluZGVwdHJhaSIsImlhdCI6MTcwMTM0OTU0OSwiZXhwIjoxNzAxMzUwNDQ5fQ.nyNTxsUI3jfOVsYhK_KgbA8jsMUz2RedNhVHdUQNTWY";
        TokenEntity tokenEntity = new TokenEntity(
                "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJFTVBMT1lFRSJdLCJ1c2VySWQiOiIxIiwic3ViIjoiYmFvdGluZGVwdHJhaSIsImlhdCI6MTcwMTM0OTU0OSwiZXhwIjoxNzAxMzUwNDQ5fQ.nyNTxsUI3jfOVsYhK_KgbA8jsMUz2RedNhVHdUQNTWY",
                1,
                false,
                false,
                1,
                1
        );
        TokenTypeEntity tokenTypeEntity = createTokenType();
        TokenCategoryEntity tokenCategoryAccess = createTokenCategory("ACCESS");
        TokenCategoryEntity tokenCategoryRefresh = createTokenCategory("REFRESH");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJFTVBMT1lFRSJdLCJ1c2VySWQiOiIxIiwic3ViIjoiYmFvdGluZGVwdHJhaSIsImlhdCI6MTcwMTM0OTU0OSwiZXhwIjoxNzAxMzUwNDQ5fQ.nyNTxsUI3jfOVsYhK_KgbA8jsMUz2RedNhVHdUQNTWY");
        MockHttpServletResponse response = new MockHttpServletResponse();
        UsersEntity usersEntity = createMockUser("hubatin@gmail.com", "pass123", "Admin", 1);
        MyUserDetails myUserDetails = myUserDetailsService.createMyUserDetails(usersEntity);

        //extract email to make email is not null
        lenient().when(jwtService.extractUsername(refreshToken)).thenReturn(usersEntity.getEmail());
        //check if condition userEmail != null
        when(usersRepository.findByUsernameOrEmailAndIsDeletedFalse(usersEntity.getEmail(), usersEntity.getEmail()))
                .thenReturn(Optional.of(usersEntity));
        when(tokenCategoryRepository.findCategoryByTokenCategoryName("REFRESH")).thenReturn(tokenCategoryRefresh);
        when(tokenRepository.findByTokenAndAndTokenCategoryIdAndExpiredFalseAndRevokedFalse(refreshToken, 1))
                .thenReturn(Optional.of(tokenEntity));
        when(myUserDetailsService.createMyUserDetails(usersEntity)).thenReturn(myUserDetails);

        //Check when if condition jwtService.isTokenValid(refreshToken, myUserDetails)
        when(jwtService.isTokenValid(refreshToken, myUserDetails)).thenReturn(true);
        when(tokenCategoryRepository.findCategoryByTokenCategoryName("ACCESS")).thenReturn(tokenCategoryAccess);
        when(tokenTypeRepository.findTypeByTokenTypeName("BEARER")).thenReturn(tokenTypeEntity);

        AuthenticationResponse authenticationResponse = authenticationService.refreshToken(request, response);
        Assertions.assertThat(authenticationResponse).isNotNull();
    }

    @Test
    void refreshToken_BadRequestException() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "aaaa eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJFTVBMT1lFRSJdLCJ1c2VySWQiOiIxIiwic3ViIjoiYmFvdGluZGVwdHJhaSIsImlhdCI6MTcwMTM0OTU0OSwiZXhwIjoxNzAxMzUwNDQ5fQ.nyNTxsUI3jfOVsYhK_KgbA8jsMUz2RedNhVHdUQNTWY");
        MockHttpServletResponse response = new MockHttpServletResponse();

        Exception exception = assertThrows(BadRequestException.class, () -> {
            authenticationService.refreshToken(request, response);
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo("header is null or didn't not start with Bearer");
    }

    @Test
    void refreshToken_NotFoundException_ByUser() {
        String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJFTVBMT1lFRSJdLCJ1c2VySWQiOiIxIiwic3ViIjoiYmFvdGluZGVwdHJhaSIsImlhdCI6MTcwMTM0OTU0OSwiZXhwIjoxNzAxMzUwNDQ5fQ.nyNTxsUI3jfOVsYhK_KgbA8jsMUz2RedNhVHdUQNTWY";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJFTVBMT1lFRSJdLCJ1c2VySWQiOiIxIiwic3ViIjoiYmFvdGluZGVwdHJhaSIsImlhdCI6MTcwMTM0OTU0OSwiZXhwIjoxNzAxMzUwNDQ5fQ.nyNTxsUI3jfOVsYhK_KgbA8jsMUz2RedNhVHdUQNTWY");
        MockHttpServletResponse response = new MockHttpServletResponse();
        UsersEntity usersEntity = createMockUser("hubatin@gmail.com", "pass123", "Admin", 1);

        lenient().when(jwtService.extractUsername(refreshToken)).thenReturn(usersEntity.getEmail());

        Exception exception = assertThrows(NotFoundException.class, () -> {
            authenticationService.refreshToken(request, response);
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo("REFRESH token not found or expired");
    }

    @Test
    void refreshToken_NotFoundException_ByToken() {
        String refreshToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJFTVBMT1lFRSJdLCJ1c2VySWQiOiIxIiwic3ViIjoiYmFvdGluZGVwdHJhaSIsImlhdCI6MTcwMTM0OTU0OSwiZXhwIjoxNzAxMzUwNDQ5fQ.nyNTxsUI3jfOVsYhK_KgbA8jsMUz2RedNhVHdUQNTWY";
        TokenCategoryEntity tokenCategoryRefresh = createTokenCategory("REFRESH");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJFTVBMT1lFRSJdLCJ1c2VySWQiOiIxIiwic3ViIjoiYmFvdGluZGVwdHJhaSIsImlhdCI6MTcwMTM0OTU0OSwiZXhwIjoxNzAxMzUwNDQ5fQ.nyNTxsUI3jfOVsYhK_KgbA8jsMUz2RedNhVHdUQNTWY");
        MockHttpServletResponse response = new MockHttpServletResponse();
        UsersEntity usersEntity = createMockUser("hubatin@gmail.com", "pass123", "Admin", 1);

        lenient().when(jwtService.extractUsername(refreshToken)).thenReturn(usersEntity.getEmail());
        when(usersRepository.findByUsernameOrEmailAndIsDeletedFalse(usersEntity.getEmail(), usersEntity.getEmail()))
                .thenReturn(Optional.of(usersEntity));
        when(tokenCategoryRepository.findCategoryByTokenCategoryName("REFRESH")).thenReturn(tokenCategoryRefresh);

        Exception exception = assertThrows(NotFoundException.class, () -> {
            authenticationService.refreshToken(request, response);
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo("REFRESH token not found or expired");
    }

    @Test
    void refreshToken_TokenInvalid() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJFTVBMT1lFRSJdLCJ1c2VySWQiOiIxIiwic3ViIjoiYmFvdGluZGVwdHJhaSIsImlhdCI6MTcwMTM0OTU0OSwiZXhwIjoxNzAxMzUwNDQ5fQ.nyNTxsUI3jfOVsYhK_KgbA8jsMUz2RedNhVHdUQNTWY");
        MockHttpServletResponse response = new MockHttpServletResponse();
        UsersEntity usersEntity = createMockUser("hubatin@gmail.com", "pass123", "Admin", 1);

        Exception exception = assertThrows(MalformedJwtException.class, () -> {
            authenticationService.refreshToken(request, response);
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo("token invalid");
    }

    @Test
    void saveUserToken_Successfully() {
        /*------------------Given--------------------*/
        UsersEntity usersEntity = createMockUser("hubatin@gmail.com", "pass123", "Admin", 1);
        TokenEntity tokenEntity = new TokenEntity(
                "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJFbXBsb3llZSJdLCJ1c2VySWQiOiIxIiwic3ViIjoiZW1wbG95ZWUiLCJpYXQiOjE3MDIxOTA4MzUsImV4cCI6MTcwMjE5MTczNX0.StBQ-PPRenx1hlgd1tVvQvrP6_DLjSRJmu-gmY6C57Q",
                1,
                false,
                false,
                1,
                1
        );
        TokenTypeEntity tokenTypeEntity = createTokenType();
        TokenCategoryEntity tokenCategoryAccess = createTokenCategory("ACCESS");

        /*------------------When--------------------*/
        //Token Type
        when(tokenTypeRepository.findTypeByTokenTypeName("BEARER")).thenReturn(tokenTypeEntity);

        //Token Category
        //Access
        when(tokenCategoryRepository.findCategoryByTokenCategoryName("ACCESS")).thenReturn(tokenCategoryAccess);

        /*------------------Then--------------------*/
        authenticationService.saveUserToken(usersEntity, tokenEntity.getToken(), tokenCategoryAccess.getTokenCategoryName());
        verify(tokenRepository).save(any(TokenEntity.class));
    }

    @Test
    void saveUserToken_SuccessfullyWithoutToken() {
        /*------------------Given--------------------*/
        UsersEntity usersEntity = createMockUser("hubatin@gmail.com", "pass123", "Admin", 1);
        TokenTypeEntity tokenTypeEntity = createTokenType();
        TokenCategoryEntity tokenCategoryAccess = createTokenCategory("ACCESS");

        /*------------------When--------------------*/
        //Token Type
        when(tokenTypeRepository.findTypeByTokenTypeName("BEARER")).thenReturn(tokenTypeEntity);

        //Token Category
        //Access
        when(tokenCategoryRepository.findCategoryByTokenCategoryName("ACCESS")).thenReturn(tokenCategoryAccess);

        /*------------------Then--------------------*/
        authenticationService.saveUserToken(usersEntity, "", tokenCategoryAccess.getTokenCategoryName());
        verify(tokenRepository).save(any(TokenEntity.class));
    }

    @Test
    void getUserRole_Successfully() {
        UsersEntity usersEntity = createMockUser("hubatin@gmail.com", "pass123", "Admin", 1);

        RolesEntity roles = new RolesEntity("Admin");
        roles.setId(1);

        List<UserRoleEntity> userRoleEntities = new ArrayList<>();
        userRoleEntities.add(new UserRoleEntity(1, 1));

        when(userRoleRepository.findByUserId(usersEntity.getId())).thenReturn(userRoleEntities);
        when(rolesRepository.findOneById(1)).thenReturn(roles);

        List<String> response = authenticationService.getUserRole(usersEntity.getId());
        List<String> expected = new ArrayList<>();
        expected.add("Admin");
        Assertions.assertThat(response).isEqualTo(expected);
    }

    @Test
    void revokeAllUserTokens_Successfully() {
        UsersEntity usersEntity = createMockUser("hubatin@gmail.com", "pass123", "Admin", 1);
        List<TokenEntity> listToken = new ArrayList<>();
        listToken.add(new TokenEntity(
                "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJFbXBsb3llZSJdLCJ1c2VySWQiOiIxIiwic3ViIjoiZW1wbG95ZWUiLCJpYXQiOjE3MDIxOTA4MzUsImV4cCI6MTcwMjE5MTczNX0.StBQ-PPRenx1hlgd1tVvQvrP6_DLjSRJmu-gmY6C57Q",
                1,
                false,
                false,
                1,
                1
        ));
        listToken.add(new TokenEntity(
                "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJFbXBsb3llZSJdLCJ1c2VySWQiOiIxIiwic3ViIjoiZW1wbG95ZWUiLCJpYXQiOjE3MDIxOTA4MzUsImV4cCI6MTcwMjE5MTczNX0.StBQ-PPRenx1hlgd1tVvQvrP6_DLjSRJmu-gmY6C57Q",
                1,
                false,
                false,
                1,
                1
        ));

        when(tokenRepository.findAllByUserIdAndExpiredFalseAndRevokedFalse(usersEntity.getId())).thenReturn(listToken);

        authenticationService.revokeAllUserTokens(usersEntity);
        verify(tokenRepository).saveAll(listToken);
    }

    @Test
    void revokeAllUserTokensAccess_Successfully() {
        UsersEntity usersEntity = createMockUser("hubatin@gmail.com", "pass123", "Admin", 1);
        List<TokenEntity> listToken = new ArrayList<>();
        listToken.add(new TokenEntity(
                "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJFbXBsb3llZSJdLCJ1c2VySWQiOiIxIiwic3ViIjoiZW1wbG95ZWUiLCJpYXQiOjE3MDIxOTA4MzUsImV4cCI6MTcwMjE5MTczNX0.StBQ-PPRenx1hlgd1tVvQvrP6_DLjSRJmu-gmY6C57Q",
                1,
                false,
                false,
                1,
                1
        ));
        listToken.add(new TokenEntity(
                "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJFbXBsb3llZSJdLCJ1c2VySWQiOiIxIiwic3ViIjoiZW1wbG95ZWUiLCJpYXQiOjE3MDIxOTA4MzUsImV4cCI6MTcwMjE5MTczNX0.StBQ-PPRenx1hlgd1tVvQvrP6_DLjSRJmu-gmY6C57Q",
                1,
                false,
                false,
                1,
                1
        ));
        TokenCategoryEntity tokenCategoryAccess = createTokenCategory("ACCESS");

        when(tokenRepository.findAllByUserIdAndTokenCategoryIdAndExpiredFalseAndRevokedFalse(usersEntity.getId(), 1)).thenReturn(listToken);
        when(tokenCategoryRepository.findCategoryByTokenCategoryName("ACCESS")).thenReturn(tokenCategoryAccess);

        authenticationService.revokeAllUserTokensAccess(usersEntity);
        verify(tokenRepository).saveAll(listToken);
    }


    private UsersEntity createMockUser(String email, String password, String role, Integer departmentId) {
        String[] name = email.split("@");
        UsersEntity usersEntity = new UsersEntity();

        usersEntity.setId(1);
        usersEntity.setUsername(name[0]);
        usersEntity.setDepartmentId(departmentId);
        usersEntity.setEmail(email);
        usersEntity.setPass(password);
        usersEntity.setIsDeleted(false);
        usersEntity.setDateDeleted(null);

        return usersEntity;
    }

    private TokenTypeEntity createTokenType() {
        TokenTypeEntity tokenTypeEntity = new TokenTypeEntity();
        tokenTypeEntity.setId(1);
        tokenTypeEntity.setTokenTypeName("BEARER");

        return tokenTypeEntity;
    }

    private TokenCategoryEntity createTokenCategory(String categoryName) {
        TokenCategoryEntity tokenCategoryEntity = new TokenCategoryEntity();
        tokenCategoryEntity.setId(1);
        tokenCategoryEntity.setTokenCategoryName(categoryName);

        return tokenCategoryEntity;
    }

}