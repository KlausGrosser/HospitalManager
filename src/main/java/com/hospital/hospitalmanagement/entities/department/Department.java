package com.hospital.hospitalmanagement.entities.department;


import com.hospital.hospitalmanagement.entities.doctor.Doctor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Department {
    @Id
    @SequenceGenerator(
            name = "department_sequence",
            sequenceName = "department_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "department_sequence"
    )
    private Long id;
    private String name;
    @Transient
    private Set<Doctor> doctors;
    @Transient
    private DepartmentService departmentService;

    public Department(String name) {
        this.name = name;
    }

    public Long getID() {
        return this.id;
    }

    public Set<Doctor> getDoctors(){
        return departmentService.getDoctorsByDepartment(this);
    }
}
