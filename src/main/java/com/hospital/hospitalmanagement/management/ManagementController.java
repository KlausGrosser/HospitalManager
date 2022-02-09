package com.hospital.hospitalmanagement.management;


import com.hospital.hospitalmanagement.entities.department.Department;
import com.hospital.hospitalmanagement.entities.doctor.Doctor;
import com.hospital.hospitalmanagement.entities.doctor.DoctorRole;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@AllArgsConstructor
public class ManagementController {

    private ManagementService managementService;

    @GetMapping(path = "/departments_list")
    public String listDepartments(Model model){
        return findPaginatedDepartment(1, "name", "asc", model);
    }

    @GetMapping(path = "/department/page/{pageNo}")
    public String findPaginatedDepartment(@PathVariable (value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                Model model) {
        int pageSize = 5;

        Page<Department> page = managementService.findPaginatedDepartment(pageNo, pageSize, sortField, sortDir);
        List<Department> listDepartments = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("listDepartments", listDepartments);
        return "departments_list";
    }

    @GetMapping(path = "/department/showDoctors/{id}")
    public String showDoctorsByDepartment(@PathVariable ( value = "id") Long id, Model model){
        model.addAttribute("listDoctors", managementService.getDoctorsByDepartment(id));
        return "doctor_list_by_department";
    }

    @GetMapping(path = "/department/new")
    public String newDepartment(Model model){
        model.addAttribute("department", new Department());
        return "new_department";
    }

    @PostMapping(path = "/department/new")
    public String newDepartment(@ModelAttribute Department department){
        managementService.newDepartment(department);
        return "redirect:/departments_list";
    }

    @GetMapping(path = "/department/delete/{id}")
    public String deleteDepartment(@PathVariable ( value = "id") Long id){
        managementService.deleteDepartmentByID(id);
        return "redirect:/departments_list";
    }

    @GetMapping(path = "/registration")
    public String register(Model model){
        model.addAttribute("doctor", new Doctor());
        return "registration_page";
    }


    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping(path = "/doctor/new")
    public String addDoctor(Model model){
        model.addAttribute("listDepartments", managementService.getDepartmentList());
        model.addAttribute("listDoctorRoles", DoctorRole.values());
        model.addAttribute("doctor", new Doctor());
        return "new_doctor";
    }

    @PostMapping(path = "/doctor/new")
    public String addDoctor(@ModelAttribute Doctor doctor){
        managementService.updateDoctor(doctor);
        return "redirect:/doctor_list";
    }

    @GetMapping(path = "/doctor/delete/{id}")
    public String deleteDoctor(@PathVariable ( value = "id") Long id){
        managementService.deleteDoctorById(id);
        return "redirect:/doctor_list";
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
        return "redirect:/doctor_list";
    }

    @PostMapping(path = "/department/updateDoc")
    public String updateInsideDepartment(@ModelAttribute Doctor doctor, HttpServletRequest request){
        managementService.updateDoctor(doctor);
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;

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

    @GetMapping(path = "/doctor/page/{pageNo}")
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
