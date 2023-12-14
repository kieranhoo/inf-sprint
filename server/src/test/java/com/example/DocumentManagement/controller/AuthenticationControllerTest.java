package com.example.DocumentManagement.controller;

import com.example.DocumentManagement.request.AuthenticationRequest;
import com.example.DocumentManagement.request.UserCreateRequest;
import com.example.DocumentManagement.response.AuthenticationResponse;
import com.example.DocumentManagement.response.MessageResponse;
import com.example.DocumentManagement.service.AuthenticationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {
    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService authenticationService;

    @Test
    void register_Successfully() {
        //Given
        UserCreateRequest userCreateRequest = new UserCreateRequest(
                "baotin172@gmail.com",
                "pass123",
                "Admin",
                1
        );
        MessageResponse messageResponse = new MessageResponse("Register successfully!");

        //When
        when(authenticationService.register(userCreateRequest)).thenReturn(messageResponse);

        ResponseEntity<MessageResponse> response = authenticationController.register(userCreateRequest);

        //Then
        Assertions.assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
        Assertions.assertThat(messageResponse).isEqualTo(response.getBody());
    }

    @Test
    void authenticate_Successfully() {
        //Given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("hubatin@gmail.com", "pass123");
        AuthenticationResponse authenticationResponse = new AuthenticationResponse("", "");

        //When
        when(authenticationService.authenticate(authenticationRequest)).thenReturn(authenticationResponse);

        ResponseEntity<AuthenticationResponse> response = authenticationController.authenticate(authenticationRequest);

        //Then
        Assertions.assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
        Assertions.assertThat(authenticationResponse).isEqualTo(response.getBody());
    }

    @Test
    void refreshToken_Successfully() {
        //Given
        AuthenticationResponse authenticationResponse = new AuthenticationResponse("", "");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJFTVBMT1lFRSJdLCJ1c2VySWQiOiIxIiwic3ViIjoiYmFvdGluZGVwdHJhaSIsImlhdCI6MTcwMTM0OTU0OSwiZXhwIjoxNzAxMzUwNDQ5fQ.nyNTxsUI3jfOVsYhK_KgbA8jsMUz2RedNhVHdUQNTWY");
        MockHttpServletResponse response = new MockHttpServletResponse();

        //When
        when(authenticationService.refreshToken(request, response)).thenReturn(authenticationResponse);

        ResponseEntity<AuthenticationResponse> responseEntity = authenticationController.refreshToken(request, response);

        //Then
        Assertions.assertThat(HttpStatus.OK).isEqualTo(responseEntity.getStatusCode());
        Assertions.assertThat(authenticationResponse).isEqualTo(responseEntity.getBody());
    }
}
