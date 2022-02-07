package com.hospital.hospitalmanagement.department;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DepartmentService {

    private final static String DEPARTMENT_NOT_FOUND_MSG =
            "department with name %s not found";
    private final DepartmentRepository departmentRepository;

    public void enableDepartment(String departmentName) {
        departmentRepository.enableDepartment(departmentName);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public void saveDepartment(Department department){
        this.departmentRepository.save(department);
    }

    public Department getDepartmentById(Long id){
        Optional<Department> optional = departmentRepository.findById(id);
        Department department = null;
        if (optional.isPresent()) {
            department = optional.get();
        } else {
            throw new RuntimeException(" Department not found for id :: " + id);
        }
        return department;
    }

    public Department getDepartmentByName(String departmentName){
        Optional<Department> optional = departmentRepository.findDepartmentByName(departmentName);
        Department department = null;
        if (optional.isPresent()) {
            department = optional.get();
        } else {
            throw new RuntimeException(" Department not found for name :: " + departmentName);
        }
        return department;
    }

    public void deleteDepartmentByName(String departmentName){
        if(departmentRepository.findDepartmentByName(departmentName).isPresent()){
            departmentRepository.deleteById(departmentRepository.findDepartmentByName(departmentName).get().getID());
        }
        throw new IllegalStateException(DEPARTMENT_NOT_FOUND_MSG);
    }

    public void deleteDepartmentById (long id) {
        departmentRepository.deleteById(id);
    }

}
