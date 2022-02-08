package com.hospital.hospitalmanagement.management.token;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.hospital.hospitalmanagement.entities.doctor.Doctor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class ConfirmationToken {
    @Id
    @SequenceGenerator(
            name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "confirmation_token_sequence"
    )

    private Long id;

    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    //confirmation is optional
    private LocalDateTime confirmedAt;
    @ManyToOne
    @JoinColumn(nullable = false,
            name = "doctor_id"
    )
    private Doctor doctor;

    public ConfirmationToken(String token,
                             LocalDateTime createdAt,
                             LocalDateTime expiresAt,
                             Doctor doctor) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.doctor = doctor;
    }
}
