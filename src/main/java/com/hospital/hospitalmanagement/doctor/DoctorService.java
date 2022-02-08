package com.hospital.hospitalmanagement.doctor;

import com.hospital.hospitalmanagement.management.token.ConfirmationToken;
import com.hospital.hospitalmanagement.management.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public void updateDoctor(Doctor doctor){
        String encodedPassword = bCryptPasswordEncoder.encode(doctor.getPassword());
        doctor.setPassword(encodedPassword);
        doctorRepository.save(doctor);
    }

    public Doctor findDoctorByEmail(String email){
        Optional<Doctor> x = doctorRepository.findByEmail(email);
        Doctor doctor = null;
        if(doctorRepository.findByEmail(email).isPresent()){
            doctor = x.get();
        }else {
            throw new RuntimeException("Doctor not found for email: " + email);
        }
        return doctor;
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

    public Doctor getDoctorByID(long id) {
        return doctorRepository.getById(id);
    }

    public Page<Doctor> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return doctorRepository.findAll(pageable);
    }
}
