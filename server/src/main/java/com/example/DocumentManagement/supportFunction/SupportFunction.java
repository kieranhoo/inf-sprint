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

    public int checkPage(String pageFromParam) {
        if (pageFromParam == null || pageFromParam.equals("")) {
            return 1;
        }
        try {
            return Integer.parseInt(pageFromParam);
        } catch (Exception e) {
            return 1;
        }
    }

    public int checkSize(String sizeFromParam) {
        if (sizeFromParam == null || sizeFromParam.equals("")) {
            return 10;
        }
        try {
            return Integer.parseInt(sizeFromParam);
        } catch (Exception e) {
            return 10;
        }
    }

    public Integer checkRole(String role){
        if (role.equals("Admin")) return 1;
        else if(role.equals("Employee")) return 2;
        else return null;
    }
}
