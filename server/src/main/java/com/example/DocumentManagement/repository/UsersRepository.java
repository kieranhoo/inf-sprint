package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
    Optional<UsersEntity> findByUsernameOrEmailAndIsDeletedFalse(String username, String email);

    @Query(value = "SELECT * FROM users  WHERE (user_name = ? OR email = ?) and is_deleted = false", nativeQuery = true)
    UsersEntity findByUsernameOrEmail(String username, String email);

    @Query(value = "SELECT * FROM users  WHERE id = ?1 and is_deleted = false", nativeQuery = true)
    UsersEntity findUserById(int userId);

    @Query(value = "SELECT * FROM users", nativeQuery = true)
    List<UsersEntity> findAllUsers();

    @Query(value = "SELECT * FROM users where department_id = ?", nativeQuery = true)
    List<UsersEntity> findUsersByDepartmentId(int dep_id);
}
