package com.hospital.hospitalmanagement.management;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.transaction.Transactional;

import com.hospital.hospitalmanagement.doctor.Doctor;
import com.hospital.hospitalmanagement.doctor.DoctorService;
import com.hospital.hospitalmanagement.email.EmailSender;
import com.hospital.hospitalmanagement.management.token.ConfirmationToken;
import com.hospital.hospitalmanagement.management.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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
                request.getDepartment()
                )
        );
// create a confirmation link to be sent to the user email
        String link = "http://localhost:8051/api/v1/doctor/register/confirm?token=" + token;
        //Send Email
        emailSender.send(
                request.getEmail(),
                buildRegisterEmail(request.getFirstName(),link)
        );
        return token;
    }

    public void updateDoctor(Doctor doctor) {
        doctorService.updateDoctor(doctor);
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
                confirmationToken.getDoctor().getEmail());
        return "confirmed";

    }

    private String buildRegisterEmail(String name, String link) {
        return "\"<div style=\\\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\\\">\\n\" +\n" +
                "        \"\\n\" +\n" +
                "        \"<span style=\\\"display:none;font-size:1px;color:#fff;max-height:0\\\"></span>\\n\" +\n" +
                "        \"\\n\" +\n" +
                "        \"  <table role=\\\"presentation\\\" width=\\\"100%\\\" style=\\\"border-collapse:collapse;min-width:100%;width:100%!important\\\" cellpadding=\\\"0\\\" cellspacing=\\\"0\\\" border=\\\"0\\\">\\n\" +\n" +
                "        \"    <tbody><tr>\\n\" +\n" +
                "        \"      <td width=\\\"100%\\\" height=\\\"53\\\" bgcolor=\\\"#0b0c0c\\\">\\n\" +\n" +
                "        \"        \\n\" +\n" +
                "        \"        <table role=\\\"presentation\\\" width=\\\"100%\\\" style=\\\"border-collapse:collapse;max-width:580px\\\" cellpadding=\\\"0\\\" cellspacing=\\\"0\\\" border=\\\"0\\\" align=\\\"center\\\">\\n\" +\n" +
                "        \"          <tbody><tr>\\n\" +\n" +
                "        \"            <td width=\\\"70\\\" bgcolor=\\\"#0b0c0c\\\" valign=\\\"middle\\\">\\n\" +\n" +
                "        \"                <table role=\\\"presentation\\\" cellpadding=\\\"0\\\" cellspacing=\\\"0\\\" border=\\\"0\\\" style=\\\"border-collapse:collapse\\\">\\n\" +\n" +
                "        \"                  <tbody><tr>\\n\" +\n" +
                "        \"                    <td style=\\\"padding-left:10px\\\">\\n\" +\n" +
                "        \"                  \\n\" +\n" +
                "        \"                    </td>\\n\" +\n" +
                "        \"                    <td style=\\\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\\\">\\n\" +\n" +
                "        \"                      <span style=\\\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\\\">Confirm your email</span>\\n\" +\n" +
                "        \"                    </td>\\n\" +\n" +
                "        \"                  </tr>\\n\" +\n" +
                "        \"                </tbody></table>\\n\" +\n" +
                "        \"              </a>\\n\" +\n" +
                "        \"            </td>\\n\" +\n" +
                "        \"          </tr>\\n\" +\n" +
                "        \"        </tbody></table>\\n\" +\n" +
                "        \"        \\n\" +\n" +
                "        \"      </td>\\n\" +\n" +
                "        \"    </tr>\\n\" +\n" +
                "        \"  </tbody></table>\\n\" +\n" +
                "        \"  <table role=\\\"presentation\\\" class=\\\"m_-6186904992287805515content\\\" align=\\\"center\\\" cellpadding=\\\"0\\\" cellspacing=\\\"0\\\" border=\\\"0\\\" style=\\\"border-collapse:collapse;max-width:580px;width:100%!important\\\" width=\\\"100%\\\">\\n\" +\n" +
                "        \"    <tbody><tr>\\n\" +\n" +
                "        \"      <td width=\\\"10\\\" height=\\\"10\\\" valign=\\\"middle\\\"></td>\\n\" +\n" +
                "        \"      <td>\\n\" +\n" +
                "        \"        \\n\" +\n" +
                "        \"                <table role=\\\"presentation\\\" width=\\\"100%\\\" cellpadding=\\\"0\\\" cellspacing=\\\"0\\\" border=\\\"0\\\" style=\\\"border-collapse:collapse\\\">\\n\" +\n" +
                "        \"                  <tbody><tr>\\n\" +\n" +
                "        \"                    <td bgcolor=\\\"#1D70B8\\\" width=\\\"100%\\\" height=\\\"10\\\"></td>\\n\" +\n" +
                "        \"                  </tr>\\n\" +\n" +
                "        \"                </tbody></table>\\n\" +\n" +
                "        \"        \\n\" +\n" +
                "        \"      </td>\\n\" +\n" +
                "        \"      <td width=\\\"10\\\" valign=\\\"middle\\\" height=\\\"10\\\"></td>\\n\" +\n" +
                "        \"    </tr>\\n\" +\n" +
                "        \"  </tbody></table>\\n\" +\n" +
                "        \"\\n\" +\n" +
                "        \"\\n\" +\n" +
                "        \"\\n\" +\n" +
                "        \"  <table role=\\\"presentation\\\" class=\\\"m_-6186904992287805515content\\\" align=\\\"center\\\" cellpadding=\\\"0\\\" cellspacing=\\\"0\\\" border=\\\"0\\\" style=\\\"border-collapse:collapse;max-width:580px;width:100%!important\\\" width=\\\"100%\\\">\\n\" +\n" +
                "        \"    <tbody><tr>\\n\" +\n" +
                "        \"      <td height=\\\"30\\\"><br></td>\\n\" +\n" +
                "        \"    </tr>\\n\" +\n" +
                "        \"    <tr>\\n\" +\n" +
                "        \"      <td width=\\\"10\\\" valign=\\\"middle\\\"><br></td>\\n\" +\n" +
                "        \"      <td style=\\\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\\\">\\n\" +\n" +
                "        \"        \\n\" +\n" +
                "        \"            <p style=\\\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\\\">Hi \" + name + \",</p><p style=\\\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\\\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\\\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\\\"><p style=\\\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\\\"> <a href=\\\"\" + link + \"\\\">Activate Now</a> </p></blockquote>\\n Link will expire in 15 minutes. <p>See you soon</p>\" +\n" +
                "        \"        \\n\" +\n" +
                "        \"      </td>\\n\" +\n" +
                "        \"      <td width=\\\"10\\\" valign=\\\"middle\\\"><br></td>\\n\" +\n" +
                "        \"    </tr>\\n\" +\n" +
                "        \"    <tr>\\n\" +\n" +
                "        \"      <td height=\\\"30\\\"><br></td>\\n\" +\n" +
                "        \"    </tr>\\n\" +\n" +
                "        \"  </tbody></table><div class=\\\"yj6qo\\\"></div><div class=\\\"adL\\\">\\n\" +\n" +
                "        \"\\n\" +\n" +
                "        \"</div></div>\";";
    }


    public void sendUpdateEmail(long id, Model model) {
        Doctor doctor = doctorService.getDoctorByID(id);
        model.addAttribute("doctor", doctor);
        emailSender.send(doctor.getFirstName(), "update_doc");
    }
}
