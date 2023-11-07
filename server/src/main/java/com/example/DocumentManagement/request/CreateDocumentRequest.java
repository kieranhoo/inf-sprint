package com.example.DocumentManagement.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateDocumentRequest {
    private String nameDocument;
    private String description;
    private String url;
    private String nameVersion;
    private String note;
    private String departmentId;
}
