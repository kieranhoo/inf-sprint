package com.example.DocumentManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "department")
public class DepartmentEntity extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
}
