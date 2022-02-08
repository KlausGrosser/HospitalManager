package com.hospital.hospitalmanagement.management;


import com.hospital.hospitalmanagement.department.Department;
import com.hospital.hospitalmanagement.doctor.DoctorRole;
import lombok.*;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ManagementRequest {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
}
