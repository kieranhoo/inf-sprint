package com.example.DocumentManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "token")
public class TokenEntity extends BaseEntity{
    @Column(name = "token", unique = true)
    public String token;

    @Column(name = "token_type_id")
    public int tokenTypeId;

    @Column(name = "revoked")
    public boolean revoked;

    @Column(name = "expired")
    public boolean expired;

    @Column(name = "user_id")
    public int userId;

    @Column(name = "token_category_id")
    public int tokenCategoryId;
}
