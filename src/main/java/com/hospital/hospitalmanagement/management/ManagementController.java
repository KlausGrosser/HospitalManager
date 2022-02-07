package com.hospital.hospitalmanagement.management;


import com.hospital.hospitalmanagement.doctor.Doctor;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    /*update
        we need to send an email with a token so when they click on it it returns them to the update form

        - email
        - token sent
     */
    @PostMapping(path = "doctor/update")
    public String update (@RequestBody ManagementRequest request, Model model){
        Doctor doctor = new Doctor(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                request.getDoctorRole(),
                request.getDepartment());
        model.addAttribute("doctor", doctor);
        return managementService.updateDoctor(request);
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
