package com.hospital.hospitalmanagement.doctor;

import com.hospital.hospitalmanagement.management.token.ConfirmationToken;
import com.hospital.hospitalmanagement.management.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DoctorService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";
    private final DoctorRepository doctorRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;


    public String newDoctor(Doctor doctor) {
        boolean userExists = doctorRepository
                .findByEmail(doctor.getEmail())
                .isPresent();
        if(userExists){

            throw new IllegalStateException("email is taken");
        }


        String encodedPassword = bCryptPasswordEncoder.encode(doctor.getPassword());
        doctor.setPassword(encodedPassword);
        doctorRepository.save(doctor);


        String token =  UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                doctor
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public String updateDoctor(Doctor doctor){



        String encodedPassword = bCryptPasswordEncoder.encode(doctor.getPassword());
        doctor.setPassword(encodedPassword);
        doctorRepository.save(doctor);


        String token =  UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                doctor
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;

    }

    public boolean findDoctorByEmail(String email){
        return doctorRepository.findByEmail(email).isPresent();
    }

    public void enableDoctor(String email) {
      doctorRepository.enableDoctor(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return doctorRepository.findByEmail(email).orElseThrow(
                () ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)
                        )
        );
    }
}
