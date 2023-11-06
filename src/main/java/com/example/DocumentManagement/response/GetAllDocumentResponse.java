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
public class GetAllDocumentResponse {
    private Integer id;
    private String name;
    private String description;
    private Date createTime;
}
