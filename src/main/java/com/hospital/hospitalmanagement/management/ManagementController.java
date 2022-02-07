package com.hospital.hospitalmanagement.management;


import com.hospital.hospitalmanagement.doctor.Doctor;
import com.hospital.hospitalmanagement.doctor.DoctorService;
import com.hospital.hospitalmanagement.management.token.ConfirmationToken;
import com.hospital.hospitalmanagement.management.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping(path ="api/v1/" )
@AllArgsConstructor
public class ManagementController {

    private ManagementService managementService;

    //register
    @PostMapping(path = "doctor/register")
    public String register(@RequestBody ManagementRequest request){
        return managementService.register(request);
    }

    @GetMapping(path = "doctor/update/{id}")
    public String updateForm(@PathVariable ( value = "id") long id, Model model){
        managementService.sendUpdateEmail(id, model);
        return "update_email_sent";
    }

    @PostMapping(path = "doctor/update")
    public String update(@RequestBody Doctor doctor){
        managementService.updateDoctor(doctor);
        return "doctor is updated!";
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token){
        return managementService.confirmToken(token);
    }

    @GetMapping
    public String register(){
        return "register_page";
    }


}
