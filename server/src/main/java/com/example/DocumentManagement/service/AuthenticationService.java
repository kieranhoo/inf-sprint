package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.*;
import com.example.DocumentManagement.exception.BadRequestException;
import com.example.DocumentManagement.exception.ConflictException;
import com.example.DocumentManagement.exception.NotFoundException;
import com.example.DocumentManagement.repository.*;
import com.example.DocumentManagement.request.AuthenticationRequest;
import com.example.DocumentManagement.request.UserCreateRequest;
import com.example.DocumentManagement.response.AuthenticationResponse;
import com.example.DocumentManagement.response.MessageResponse;
import com.example.DocumentManagement.supportFunction.SupportFunction;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService extends SupportFunction {
    private final UsersRepository userRepository;
    private final MyUserDetailsService myUserDetailsService;
    private final UserRoleRepository userRoleRepository;
    private final RolesRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenCategoryRepository tokenCategoryRepository;
    private final TokenTypeRepository tokenTypeRepository;
    private final DepartmentRepository departmentRepository;
    private final AuthenticationManager authenticationManager;

    public MessageResponse register(UserCreateRequest userCreateRequest){
        String[] name = userCreateRequest.getEmail().split("@");
        UsersEntity userCheck = userRepository.findByUsernameOrEmailAndIsDeletedFalse(name[0], userCreateRequest.getEmail()).orElse(null);
        if (userCheck != null)
            throw new ConflictException("User name or email already exists");

        DepartmentEntity departmentEntity = departmentRepository.findDepartmentById(userCreateRequest.getDepartmentId());
        if(departmentEntity == null) {
            throw new NotFoundException("Can't not find department!");
        }

        if(checkRole(userCreateRequest.getRole()) == null) {
            throw new NotFoundException("Role doesn't exist in System!");
        }

        var user = UsersEntity.builder()
                .departmentId(userCreateRequest.getDepartmentId())
                .username(name[0])
                .email(userCreateRequest.getEmail())
                .isDeleted(false)
                .pass(passwordEncoder.encode(userCreateRequest.getPassword()))
                .build();
        var savedUser = userRepository.save(user);

        userRoleRepository.save(new UserRoleEntity(savedUser.getId(), checkRole(userCreateRequest.getRole())));

        return new MessageResponse("Register successfully!");
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsernameOrEmailAndIsDeletedFalse(request.getEmail(), request.getEmail())
                .orElseThrow(() -> new NotFoundException("Not found user name or email"));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        MyUserDetails myUserDetails = myUserDetailsService.createMyUserDetails(user);
        var jwtToken = jwtService.generateToken(myUserDetails, user.getId().toString(), getUserRole(user.getId()));
        var refreshToken = jwtService.generateRefreshToken(myUserDetails, user.getId().toString(), getUserRole(user.getId()));
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken, "ACCESS");
        saveUserToken(user, refreshToken, "REFRESH");
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BadRequestException("header is null or didn't not start with Bearer");
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);


        if (userEmail != null) {
            var user = this.userRepository.findByUsernameOrEmailAndIsDeletedFalse(userEmail, userEmail)
                    .orElseThrow(() -> new NotFoundException("REFRESH token not found or expired"));
            tokenRepository.findByTokenAndAndTokenCategoryIdAndExpiredFalseAndRevokedFalse(refreshToken, tokenCategoryRepository.findByTokenCategoryName("REFRESH").orElseThrow().getId())
                    .orElseThrow(() -> new NotFoundException("REFRESH token not found or expired "));
            MyUserDetails myUserDetails = myUserDetailsService.createMyUserDetails(user);
            if (jwtService.isTokenValid(refreshToken, myUserDetails)) {
                var accessToken = jwtService.generateToken(myUserDetails, user.getId().toString(), getUserRole(user.getId()));
                revokeAllUserTokensAccess(user);
                saveUserToken(user, accessToken, "ACCESS");
                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }

        }
        throw new MalformedJwtException("token invalid");
    }

    private void saveUserToken(UsersEntity user, String jwtToken, String tokenCategory) {
        var token = TokenEntity.builder()
                .userId(user.getId())
                .token(jwtToken)
                .tokenTypeId(tokenTypeRepository.findByTokenTypeName("BEARER").get().getId())
                .expired(false)
                .revoked(false)
                .tokenCategoryId(tokenCategoryRepository.findByTokenCategoryName(tokenCategory)
                        .orElseThrow()
                        .getId())
                .build();
        tokenRepository.save(token);
    }

    public List<String> getUserRole(int userId) {
        List<String> roles = new ArrayList<>();
        List<UserRoleEntity> userRoleEntities = userRoleRepository.findByUserId(userId);
        //create CANDIDATE role if account don't have role
        if (userRoleEntities.size() == 0) {
            UserRoleEntity newUserRoleEntity = new UserRoleEntity(userId, 1);
            userRoleRepository.save(newUserRoleEntity);
            userRoleEntities = userRoleRepository.findByUserId(userId);
        }
        // get roles list
        for (UserRoleEntity userRole : userRoleEntities) {
            RolesEntity roleEntity = roleRepository.findOneById(userRole.getRoleId());
            roles.add(roleEntity.getRole());
        }
        return roles;
    }

    private void revokeAllUserTokens(UsersEntity user) {
        var validUserTokens = tokenRepository.findAllByUserIdAndExpiredFalseAndRevokedFalse(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void revokeAllUserTokensAccess(UsersEntity user) {
        var validUserTokens = tokenRepository.findAllByUserIdAndTokenCategoryIdAndExpiredFalseAndRevokedFalse(user.getId(), tokenCategoryRepository.findByTokenCategoryName("ACCESS").get().getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
