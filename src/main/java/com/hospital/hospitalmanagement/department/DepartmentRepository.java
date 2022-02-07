package com.hospital.hospitalmanagement.department;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findDepartmentByName(String departmentName);

    Optional<Department> findDepartmentById(Long id);

    @Transactional
    @Modifying
    @Query(
            "UPDATE Department a "+
                    "SET a.enabled = TRUE "+
                    "WHERE a.name = ?1"
    )
    int enableDepartment(String departmentName);


    void delete(Optional<Department> department);
}
