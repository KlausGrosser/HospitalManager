package com.hospital.hospitalmanagement.entities.doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByEmail(String email);

    @Transactional
    @Modifying
    @Query(
            "UPDATE Doctor a "+
                    "SET a.enabled = TRUE "+
                    "WHERE a.email = ?1"
    )
    int enableDoctor(String email);
}
