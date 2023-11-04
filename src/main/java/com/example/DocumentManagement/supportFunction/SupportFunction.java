package com.example.DocumentManagement.supportFunction;

import com.example.DocumentManagement.exception.BadRequestException;

public class SupportFunction {
    public int checkRequest(String request) {
        if(request == null || request.equals("")) {
            throw new BadRequestException("Request must be a Integer");
        }
        try {
            return Integer.parseInt(request);
        } catch (Exception e) {
            throw new BadRequestException("Request must be a Integer");
        }
    }
}
