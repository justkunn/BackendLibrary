package com.example.library.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.DTO.Employee.Request.RequestDataEmployee;
import com.example.library.DTO.Employee.Response.MainResponse;
import com.example.library.Services.EmployeeService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/employe")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/add")
    public ResponseEntity<MainResponse> addEmployee(RequestDataEmployee requestDataEmployee) {
        log.info("Add Employee : {}", requestDataEmployee);

        ResponseEntity<MainResponse> addEmployee = employeeService.addEmployee(requestDataEmployee);
        return addEmployee;
    }

    @PutMapping("/edit{idEmployee}")
    public ResponseEntity<MainResponse> editEmployee(@PathVariable @Valid Integer idEmployee, RequestDataEmployee dataEmployee) {
        log.info("Editing data employee with ID : {}", idEmployee);

        ResponseEntity<MainResponse> editEmployee = employeeService.editEmployee(idEmployee, dataEmployee);

        return editEmployee;
    }

    @GetMapping("/get{idEmployee}")
    public ResponseEntity<MainResponse> getEmployee(@PathVariable @Valid Integer idEmployee) {
        log.info("Get data employee with ID : {}", idEmployee);
        
        ResponseEntity<MainResponse> getEmployee = employeeService.getEmployee(idEmployee);
        return getEmployee;
    }

    @DeleteMapping("/delete{idEmployee}")
    public ResponseEntity<MainResponse> deleteEmployee(@PathVariable @Valid Integer idEmployee) {
        log.info("Delete data employee with ID : {}", idEmployee);

        ResponseEntity<MainResponse> deleteEmployee = employeeService.deleteEmployee(idEmployee);
        return deleteEmployee;
    }
}
