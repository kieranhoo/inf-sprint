package com.example.DocumentManagement.response;

import com.example.DocumentManagement.entity.DepartmentEntity;
import com.example.DocumentManagement.entity.VersionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponse {
    private Integer id;
    private String name;
    private String description;

    private Date createTime;
    private DepartmentEntity department;
    private List<VersionEntity> versions;

}
