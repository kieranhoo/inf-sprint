package com.example.DocumentManagement.controller;

import com.example.DocumentManagement.request.AuthenticationRequest;
import com.example.DocumentManagement.response.AuthenticationResponse;
import com.example.DocumentManagement.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {
    @InjectMocks
    private AuthenticationController controller;
    @Mock
    private AuthenticationService service;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @Test
    void callRefreshToken() {
        AuthenticationResponse expectedResponse = new AuthenticationResponse("newAccessToken", "newRefreshToken");
        when(service.refreshToken(request, response)).thenReturn(expectedResponse);
        ResponseEntity<AuthenticationResponse> responseEntity = controller.refreshToken(request, response);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());

        verify(service, times(1)).refreshToken(request, response);
        verifyNoMoreInteractions(service);

    }

    @Test
    void callRefreshTokenWhenTokenNotExpired() {
        AuthenticationResponse expToken = new AuthenticationResponse("expToken", "accessToken");
        when(service.refreshToken(request, response)).thenReturn(expToken);
        ResponseEntity<AuthenticationResponse> expTokenResp = controller.refreshToken(request, response);
        assertEquals(HttpStatus.OK, expTokenResp.getStatusCode());
        assertEquals(expToken, expTokenResp.getBody());

        AuthenticationResponse newToken = new AuthenticationResponse("newAccessToken", "newRefreshToken");
        when(service.refreshToken(request, response)).thenReturn(newToken);
        ResponseEntity<AuthenticationResponse> newTokenEntity = controller.refreshToken(request, response);
        assertEquals(HttpStatus.OK, newTokenEntity.getStatusCode());
        assertEquals(newToken, newTokenEntity.getBody());

        verify(service, times(2)).refreshToken(request, response);
        verifyNoMoreInteractions(service);

    }
}
