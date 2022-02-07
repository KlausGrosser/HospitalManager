package com.hospital.hospitalmanagement.doctor;

import com.hospital.hospitalmanagement.department.Department;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
public class Doctor implements UserDetails {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )

    private long ID;
    private String firstName;
    private String lastName;
    @JoinColumn(name = "department_id")
    private Department department;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private DoctorRole doctorRole;
    private boolean departmentChangeEligible = false;
    private LocalDate startingDepartmentDate;
    private Boolean locked = false;
    private Boolean enabled = false;


    public Doctor(String firstName, String lastName, String email, String password, DoctorRole doctorRole, Department department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.doctorRole = doctorRole;
        this.department = department;
        this.startingDepartmentDate = LocalDate.now();
    }

    public Doctor(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(doctorRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword(){
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
