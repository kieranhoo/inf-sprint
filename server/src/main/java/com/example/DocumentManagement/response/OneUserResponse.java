package com.example.DocumentManagement.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OneUserResponse {
    private Integer id;
    private String userName;
    private String email;
    private Integer departmentId;
}
