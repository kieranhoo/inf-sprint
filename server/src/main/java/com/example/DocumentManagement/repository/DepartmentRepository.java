package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.DepartmentEntity;
import com.example.DocumentManagement.entity.DocumentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Integer> {

    @Query(value = "SELECT * FROM department", nativeQuery = true)
    List<DepartmentEntity> findAllDepartments();

    @Query(value = "SELECT * FROM department where id = ?", nativeQuery = true)
    DepartmentEntity findDepartmentById(int id);
}
