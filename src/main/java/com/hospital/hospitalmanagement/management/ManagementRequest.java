package com.hospital.hospitalmanagement.management;


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
