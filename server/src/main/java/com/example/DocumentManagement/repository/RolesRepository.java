package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RolesRepository extends JpaRepository<RolesEntity, Integer> {
    @Query(value = "SELECT * FROM roles  WHERE id = ?1", nativeQuery = true)
    RolesEntity findOneById(int id);
}
