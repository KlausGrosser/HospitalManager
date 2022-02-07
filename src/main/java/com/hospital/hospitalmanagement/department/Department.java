package com.hospital.hospitalmanagement.department;

import com.hospital.hospitalmanagement.doctor.Doctor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
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
    @OneToMany
    private List<Doctor> activeDoctorsList;

    public Department(String name) {
        this.name = name;
    }

    public Long getID() {
        return this.id;
    }
}
