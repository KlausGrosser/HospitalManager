package com.hospital.hospitalmanagement.management;

import java.time.LocalDateTime;
import javax.transaction.Transactional;

import com.hospital.hospitalmanagement.doctor.Doctor;
import com.hospital.hospitalmanagement.doctor.DoctorService;
import com.hospital.hospitalmanagement.email.EmailSender;
import com.hospital.hospitalmanagement.management.token.ConfirmationToken;
import com.hospital.hospitalmanagement.management.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ManagementService {

    private final DoctorService doctorService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidator emailValidator;
    private final EmailSender emailSender;

    public String register(ManagementRequest request) {
        //email should be validated

        if(!emailValidator
                .test(request.getEmail())){
            throw new IllegalStateException(
                    "invalid email"
            );
        }
        //signing the user up and receiving the token back
        String token = doctorService.newDoctor(new Doctor(request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                request.getDoctorRole(),
                request.getDepartmentTypes()

                )
        );
// create a confirmation link to be sent to the user email
        String link = "http://localhost:8051/api/v1/registration/confirm?token=" + token;
        //Send Email
        emailSender.send(
                request.getEmail(),
                buildEmail(request.getFirstName(),link)
        );
        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        // search for the token
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(
                        ()-> new IllegalStateException("token not found")
                );
        // check if the token is already confirmed or not
        if(confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("token is confirmed");
        }
        // check if the token expires at time after now or not
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("token is expired");
        }
        // set the confirmed time of the token to now
        confirmationTokenService.setConfirmedAt(token);
        // set the user that holds this token to enabled
        doctorService.enableDoctor(
<<<<<<< HEAD:src/main/java/com/hospital/hospitalmanagement/management/RegistrationService.java
                confirmationToken.getDoctor().getEmail()
        );
=======
                confirmationToken.getDoctor().getEmail());
>>>>>>> 4f659d813d5538e07464e59cac6fa16600e5653b:src/main/java/com/hospital/hospitalmanagement/management/ManagementService.java
        // return "confirmed"
        return "confirmed";

    }

    private String buildEmail(String name, String link) {
        //TODO : build email here
        return "";
    }

    public String update(ManagementRequest request) {
        //if the email isn
        if(doctorService.findDoctorByEmail(request.getEmail()) == null){
            return register(request);
        }



    }
}
