package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.DepartmentEntity;
import com.example.DocumentManagement.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    @Query(value = "SELECT * FROM user", nativeQuery = true)
    List<UserEntity> findAllUsers();

    @Query(value = "SELECT * FROM user where id = ?", nativeQuery = true)
    UserEntity findUserById(int id);

    @Query(value = "SELECT * FROM user where department_id = ?", nativeQuery = true)
    List<UserEntity> findUsersByDepartmentId(int dep_id);
}
