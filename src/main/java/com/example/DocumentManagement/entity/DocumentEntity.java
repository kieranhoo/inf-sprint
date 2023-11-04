package com.example.DocumentManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "document")
public class DocumentEntity extends BaseEntity{
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = Shape.STRING)
    @Column(name = "create_time")
    private String createTime;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = Shape.STRING)
    @Column(name = "date_deleted")
    private String dateDeleted;
}
