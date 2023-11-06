package com.example.DocumentManagement.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse {
    private long totalElement;
    private int totalPages;
    private List<?> listContent;
}
