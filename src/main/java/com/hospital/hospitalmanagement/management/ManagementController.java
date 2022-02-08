package com.hospital.hospitalmanagement.management;


import com.hospital.hospitalmanagement.doctor.Doctor;
import com.hospital.hospitalmanagement.doctor.DoctorRole;
import com.hospital.hospitalmanagement.doctor.DoctorService;
import com.hospital.hospitalmanagement.management.token.ConfirmationToken;
import com.hospital.hospitalmanagement.management.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class ManagementController {

    private ManagementService managementService;

    @GetMapping(path = "/registration")
    public String register(Model model){
        model.addAttribute("doctor", new Doctor());
        return "registration_page";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping(path = "/doctor/delete/{id}")
    public String deleteDoctor(@PathVariable ( value = "id") Long id, Model model){

    }

    //register
    @PostMapping(path = "/registerNewDoc")
    public String register(@ModelAttribute Doctor request){
        managementService.register(new ManagementRequest(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword()
        ));
        return "register_check_email";
    }

    @GetMapping(path = "/doctor/update/{id}")
    public String updateForm(@PathVariable ( value = "id") Long id, Model model){
        Doctor doctor = managementService.getDoctorByID(id);
        model.addAttribute("listDepartments", managementService.getDepartmentList());
        model.addAttribute("listDoctorRoles", DoctorRole.values());
        model.addAttribute("doctor", doctor);
        return "update_doctor";
    }

    @PostMapping(path = "/doctor/update")
    public String update(@ModelAttribute Doctor doctor){
        managementService.updateDoctor(doctor);
        return "doctor is updated!";
    }

    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token){
        managementService.confirmToken(token);
        return "index";
    }

    @GetMapping(path = "/doctor_list")
    public String listDoctors(Model model){
        return findPaginated(1, "lastName", "asc", model);
    }

    @GetMapping(path = "/page/{pageNo}")
    public String findPaginated(@PathVariable (value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        int pageSize = 5;

        Page<Doctor> page = managementService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Doctor> listDoctors = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listDoctors", listDoctors);
        return "doctor_list";
    }
}
