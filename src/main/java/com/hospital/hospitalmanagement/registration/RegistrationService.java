package com.hospital.hospitalmanagement.registration;

import java.time.LocalDateTime;
import javax.transaction.Transactional;

import com.hospital.hospitalmanagement.doctor.Doctor;
import com.hospital.hospitalmanagement.doctor.DoctorService;
import com.hospital.hospitalmanagement.email.EmailSender;
import com.hospital.hospitalmanagement.registration.token.ConfirmationToken;
import com.hospital.hospitalmanagement.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final DoctorService doctorService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailValidator emailValidator;
    private final EmailSender emailSender;

    public String register(RegistrationRequest request) {
        //email should be validated

        if(!emailValidator
                .test(request.getEmail())){
            throw new IllegalStateException(
                    "invalid email"
            );
        }
        //signing the user up and receiving the token back
        String token = doctorService.signUpUser(new Doctor( request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword()
                )
        );
// create a confirmation link to be sent to the user email
// http://localhost:8080/api/v1/registration/confirm?token=" + token
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
        doctorService.enableAppUser(
                confirmationToken.getAppUser().getEmail()
        );
        // return "confirmed"
        return "confirmed";

    }

    private String buildEmail(String name, String link) {
        //TODO : build email here
        return ""
    }
}