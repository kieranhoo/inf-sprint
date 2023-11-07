package com.example.DocumentManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "token_type")
public class TokenTypeEntity extends BaseEntity{
    @Column(name = "token_type_name")
    private String tokenTypeName;
}
