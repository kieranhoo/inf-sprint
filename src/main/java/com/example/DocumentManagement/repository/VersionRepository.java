package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.VersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersionRepository extends JpaRepository<VersionEntity, Integer> {

}
