package com.example.DocumentManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "version")
public class VersionEntity extends BaseEntity{
    @Column(name = "document_id")
    private Integer documentId;

    @Column(name = "url")
    private String url;

    @Column(name = "name")
    private String name;

    @Column(name = "current_version")
    private Boolean currentVersion;

    @Column(name = "update_time")
    private Date updateTime;
    @Column(name = "note")
    private String note;
    
}
