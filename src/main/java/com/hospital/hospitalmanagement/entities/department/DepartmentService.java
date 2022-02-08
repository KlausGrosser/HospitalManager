package com.hospital.hospitalmanagement.entities.department;


import com.hospital.hospitalmanagement.entities.doctor.Doctor;
import com.hospital.hospitalmanagement.entities.doctor.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class DepartmentService {

    private final static String DEPARTMENT_NOT_FOUND_MSG =
            "department with name %s not found";
    private final DepartmentRepository departmentRepository;
    private final DoctorService doctorService;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public void addDoctorToDepartment(){

    }

    public Set<Doctor> getDoctorsByDepartment(Department department){
        return doctorService.getDoctorsByDepartment(department.getName());
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

    public List<Department> getDepartmentList() {
        return departmentRepository.findAll();
    }

    public Page<Department> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return departmentRepository.findAll(pageable);
    }
}
