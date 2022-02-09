package com.hospital.hospitalmanagement.management;


import com.hospital.hospitalmanagement.entities.department.Department;
import com.hospital.hospitalmanagement.entities.doctor.Doctor;
import com.hospital.hospitalmanagement.entities.doctor.DoctorRole;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The main controller of the app. Contains all the mappings.
 */
@Controller
@AllArgsConstructor
public class ManagementController {

    private ManagementService managementService;

    //General Mappings:

    /**
     * Mapping to get the login page
     * @return login page
     */
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    /**
     * Mapping to get the registration page
     * @param model
     * @return registration page
     */
    @GetMapping(path = "/registration")
    public String register(Model model){
        model.addAttribute("doctor", new Doctor());
        return "registration_page";
    }

    /**
     * Mapping to add a new doctor to the doctors database
     * @param model
     * @return new doctor form
     */
    @GetMapping(path = "/doctor/new")
    public String addDoctor(Model model){
        model.addAttribute("listDepartments", managementService.getDepartmentList());
        model.addAttribute("listDoctorRoles", DoctorRole.values());
        model.addAttribute("doctor", new Doctor());
        return "new_doctor";
    }

    /**
     * Mapping that sends the user to the login page after confirming account in their email
     * @param token
     * @return login page
     */
    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token){
        managementService.confirmToken(token);
        return "login";
    }

    /**
     * Mapping that displays a prompt to check email after registering new account
     * @param request
     * @return prompt to check email for confirmation
     */
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

    //Doctor Mappings:

    /**
     * Mapping that displays the page containing doctors list
     * @param model
     * @return doctors list
     */
    @GetMapping(path = "/doctor_list")
    public String listDoctors(Model model){
        return findPaginated(1, "lastName", "asc", model);
    }

    /**
     * Mapping that crates and displays a table containing all current doctors
     * @param pageNo
     * @param sortField
     * @param sortDir
     * @param model
     * @return doctor list
     */
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

    /**
     * Mapping that sends user to an update page containing a form
     * @param id
     * @param model
     * @return update form
     */
    @GetMapping(path = "/doctor/update/{id}")
    public String updateForm(@PathVariable ( value = "id") Long id, Model model){
        Doctor doctor = managementService.getDoctorByID(id);
        model.addAttribute("listDepartments", managementService.getDepartmentList());
        model.addAttribute("listDoctorRoles", DoctorRole.values());
        model.addAttribute("doctor", doctor);
        return "update_doctor";
    }

    /**
     * Mapping that deletes a doctor
     * @param id
     * @return redirects to doctor list
     */
    @GetMapping(path = "/doctor/delete/{id}")
    public String deleteDoctor(@PathVariable ( value = "id") Long id){
        managementService.deleteDoctorById(id);
        return "redirect:/doctor_list";
    }

    /**
     * Mapping that adds a doctor straight from the doctors list
     * @param doctor
     * @return redirects to doctor list
     */
    @PostMapping(path = "/doctor/new")
    public String addDoctor(@ModelAttribute Doctor doctor){
        managementService.updateDoctor(doctor);
        return "redirect:/doctor_list";
    }

    /**
     * Mapping that takes the information from user input and puts ir into database to update doctor
     * @param doctor
     * @return redirects to doctor list
     */
    @PostMapping(path = "/doctor/update")
    public String update(@ModelAttribute Doctor doctor){
        managementService.updateDoctor(doctor);
        return "redirect:/doctor_list";
    }


    //Department Mappings:

    /**
     * Mapping that gets the page containing a list of all departments.
     * @param model
     * @return department list page
     */
    @GetMapping(path = "/departments_list")
    public String listDepartments(Model model){
        return findPaginatedDepartment(1, "name", "asc", model);
    }

    /**
     * Mapping that creates and displays a list containing all current departments
     * @param pageNo
     * @param sortField
     * @param sortDir
     * @param model
     * @return department list
     */
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

    /**
     * Mapping that gets the page containing a list of all the doctors inside the selected department
     * @param id
     * @param model
     * @return doctor list by department page
     */
    @GetMapping(path = "/department/showDoctors/{id}")
    public String showDoctorsByDepartment(@PathVariable ( value = "id") Long id, Model model){
        model.addAttribute("listDoctors", managementService.getDoctorsByDepartment(id));
        return "doctor_list_by_department";
    }

    /**
     * Mapping that displays a form to add  a new department to the database
     * @param model
     * @return new department form
     */
    @GetMapping(path = "/department/new")
    public String newDepartment(Model model){
        model.addAttribute("department", new Department());
        return "new_department";
    }

    /**
     * Mapping that deletes department from database
     * @param id
     * @return redirects to departments list
     */
    @GetMapping(path = "/department/delete/{id}")
    public String deleteDepartment(@PathVariable ( value = "id") Long id){
        managementService.deleteDepartmentByID(id);
        return "redirect:/departments_list";
    }

    /**
     * Mapping that takes user input into the database to create a new department
     * @param department
     * @return redirects to departments list
     */
    @PostMapping(path = "/department/new")
    public String newDepartment(@ModelAttribute Department department){
        managementService.newDepartment(department);
        return "redirect:/departments_list";
    }
}
