package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.DepartmentEntity;
import com.example.DocumentManagement.response.ListResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository underTest;

    @AfterEach
    void tearDown(){
        underTest.deleteAll();
    }

    @Test
    void givenDepartments_whenFindAllDepartments_thenReturnAllDepartments() {
        // Given
        DepartmentEntity department1 = new DepartmentEntity("Management","Management Room");
        DepartmentEntity department2 = new DepartmentEntity("IT","IT Room");
        List<DepartmentEntity> expectDepartments = Arrays.asList(department1, department2);
        underTest.save(department1);
        underTest.save(department2);

        // When
        List<DepartmentEntity> actualDepartments = underTest.findAllDepartments();

        // Then
        Assertions.assertNotNull(actualDepartments);
        Assertions.assertEquals(expectDepartments.size(), actualDepartments.size());
        Assertions.assertEquals(department1.getName(), actualDepartments.get(0).getName());
        Assertions.assertEquals(department2.getName(), actualDepartments.get(1).getName());
    }

    @Test
    void givenZeroDepartments_whenFindAllDepartments_thenReturnZeroDepartments() {
        // Given

        // When
        List<DepartmentEntity> actualDepartments = underTest.findAllDepartments();

        // Then
        Assertions.assertEquals(Collections.emptyList(), actualDepartments);
        Assertions.assertEquals(0, actualDepartments.size());
    }

    @Test
    void givenEmptyDepartments_whenFindAllDepartments_thenReturnEmptyDepartments() {
        // Given
        DepartmentEntity department1 = new DepartmentEntity();
        DepartmentEntity department2 = new DepartmentEntity();
        List<DepartmentEntity> expectDepartments = Arrays.asList(department1, department2);
        underTest.save(department1);
        underTest.save(department2);

        // When
        List<DepartmentEntity> actualDepartments = underTest.findAllDepartments();

        // Then
        Assertions.assertNotNull(actualDepartments);
        Assertions.assertEquals(expectDepartments.size(), actualDepartments.size());
    }

    @Test
    void givenDepartmentsWithinSearchingRange_whenFindDepartmentById_thenReturnSuitableDepartment() {
        // Given
        DepartmentEntity department1 = new DepartmentEntity("Management","Management Room");
        DepartmentEntity department2 = new DepartmentEntity("IT","IT Room");
        DepartmentEntity resDepartment1 = underTest.save(department1);
        DepartmentEntity resDepartment2 = underTest.save(department2);

        // When
        DepartmentEntity actualDepartment = underTest.findDepartmentById(resDepartment1.getId());

        // Then
        Assertions.assertNotNull(actualDepartment);
        Assertions.assertEquals(department1.getName(), actualDepartment.getName());
        Assertions.assertEquals(department1.getDescription(), actualDepartment.getDescription());
    }

    @Test
    void givenDepartmentsWithoutSearchingRange_whenFindDepartmentById_thenReturnNull() {
        // Given
        DepartmentEntity department1 = new DepartmentEntity("Management","Management Room");
        DepartmentEntity department2 = new DepartmentEntity("IT","IT Room");
        DepartmentEntity resDepartment1 = underTest.save(department1);
        DepartmentEntity resDepartment2 = underTest.save(department2);

        // When
        DepartmentEntity actualDepartment = underTest.findDepartmentById(resDepartment1.getId()+resDepartment2.getId());

        // Then
        Assertions.assertNull(actualDepartment);
    }

    @Test
    void givenZeroDepartments_whenFindDepartmentById_thenReturnNull() {
        // Given

        // When
        DepartmentEntity actualDepartment = underTest.findDepartmentById(1);

        // Then
        Assertions.assertNull(actualDepartment);
    }

    @Test
    void givenEmptyDepartmentsWithinRange_whenFindDepartmentsById_thenReturnEmptyDepartment() {
        // Given
        DepartmentEntity department1 = new DepartmentEntity();
        DepartmentEntity department2 = new DepartmentEntity();
        DepartmentEntity resDepartment1 = underTest.save(department1);
        DepartmentEntity resDepartment2 = underTest.save(department2);

        // When
        DepartmentEntity actualDepartment = underTest.findDepartmentById(resDepartment1.getId());

        // Then
        Assertions.assertNotNull(actualDepartment);
        Assertions.assertEquals(department1.getName(), actualDepartment.getName());
        Assertions.assertEquals(department1.getDescription(), actualDepartment.getDescription());
    }

    @Test
    void givenEmptyDepartmentsWithoutRange_whenFindDepartmentsById_thenReturnNull() {
        // Given
        DepartmentEntity department1 = new DepartmentEntity();
        DepartmentEntity department2 = new DepartmentEntity();
        DepartmentEntity resDepartment1 = underTest.save(department1);
        DepartmentEntity resDepartment2 = underTest.save(department2);

        // When
        DepartmentEntity actualDepartment = underTest.findDepartmentById(resDepartment1.getId()+resDepartment2.getId());

        // Then
        Assertions.assertNull(actualDepartment);
    }
}
