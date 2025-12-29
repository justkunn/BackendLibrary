package com.example.library.Services;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.library.DTO.Employee.Request.RequestDataEmployee;
import com.example.library.DTO.Employee.Response.MainResponse;
import com.example.library.DTO.Employee.Response.ResponseDataEmployee;
import com.example.library.Entity.EmployeeEntity;
import com.example.library.Repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    String formatDate = sdf.format(new Date());

    public ResponseEntity<MainResponse> addEmployee(RequestDataEmployee employee) {
        log.info("preparing to add new employee");

        try {
            // set to database
            EmployeeEntity employeeEntity = new EmployeeEntity();
            EmployeeEntity employeeDb = mapToEntity(employeeEntity, employee);

            employeeRepository.save(employeeDb);

            // set response

            String msg = "Employee has been registered";
            MainResponse mainResponse = mapToResponse(employeeEntity, HttpStatus.OK, msg);
            return ResponseEntity.status(HttpStatus.OK).body(mainResponse);
        } catch (Exception e) {
            log.info("failed to execute program");

            String msg = "Employee failed to register";
            MainResponse response = mapToResponse(null, HttpStatus.BAD_REQUEST, msg);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    public ResponseEntity<MainResponse> editEmployee(Integer idEmployee, RequestDataEmployee employee) {
        log.info("preparing edit employee with ID : {}", idEmployee);

        //set to database
        EmployeeEntity employeeEntity = employeeRepository.findByIdEmployee(idEmployee).orElse(null);

        mapToEntity(employeeEntity, employee);
        employeeRepository.save(employeeEntity);

        MainResponse mainResponse = mapToResponse(employeeEntity, HttpStatus.OK, "Employee has been updated");

        return ResponseEntity.ok(mainResponse);
    }

    public ResponseEntity<MainResponse> getEmployee (Integer idEmployee) {
        log.info("preparing to get data employee with ID : {}", idEmployee);

        //get employee
        EmployeeEntity employeeEntity = employeeRepository.findByIdEmployee(idEmployee).orElse(null);

        MainResponse mainResponse = mapToResponse(employeeEntity, HttpStatus.OK, "Employee has been retrieved");

        return ResponseEntity.status(HttpStatus.OK).body(mainResponse);
    }

    public ResponseEntity<MainResponse> deleteEmployee (Integer idEmployee) {
        log.info("preparing to delete employee with ID : {}", idEmployee);

        //get employee form database

        Optional<EmployeeEntity> employeeEntity = employeeRepository.findByIdEmployee(idEmployee);

        if(employeeEntity.isPresent()) {
            EmployeeEntity employeeToDelete = employeeEntity.get();
            employeeRepository.delete(employeeToDelete);
            log.info("Employee with ID {} has been deleted", idEmployee);
            MainResponse mainResponse = mapToResponse(employeeToDelete, HttpStatus.OK, "Employee has been deleted");
            return ResponseEntity.status(HttpStatus.OK).body(mainResponse);
        } else {
            log.info("Employee with ID {} not found", idEmployee);
            MainResponse mainResponse = mapToResponse(null, HttpStatus.NOT_FOUND, "Employee not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mainResponse);
        }
        
    }

    public MainResponse mapToResponse(EmployeeEntity employeeEntity, HttpStatus status, String msg) {
        log.info("preparing set response");

        MainResponse mainResponse = new MainResponse();

        if (status == HttpStatus.OK) {
            mainResponse.setStatus("Success");
            mainResponse.setMessage("Employe has rergistered");
        } else if (status == HttpStatus.BAD_REQUEST) {
            mainResponse.setStatus("Failed");
            mainResponse.setMessage("Employe failed to regis");
        } else {
            mainResponse.setStatus("Error");
            mainResponse.setMessage("Unknow Error");
        }
        mainResponse.setResponseCode(status.value());

        if (employeeEntity != null) {
            ResponseDataEmployee responseDataEmployee = new ResponseDataEmployee();
            responseDataEmployee.setIdEmployee(employeeEntity.getIdEmployee());
            responseDataEmployee.setEmployeeName(employeeEntity.getEmployeeName());
            responseDataEmployee.setAge(employeeEntity.getAge());
            responseDataEmployee.setEmail(employeeEntity.getEmail());
            responseDataEmployee.setPhoneNumber(employeeEntity.getPhoneNumber());
            responseDataEmployee.setPosition(employeeEntity.getPosition());

            mainResponse.setData(responseDataEmployee);
        } else {
            mainResponse.setData(null);
        }
        mainResponse.setTimestamp(formatDate);

        return mainResponse;
    }

    public EmployeeEntity mapToEntity(EmployeeEntity employeeEntity, RequestDataEmployee employee) {
        log.info("preparing set requset to database");

        employeeEntity.setEmployeeName(employee.getEmployeeName());
        employeeEntity.setAge(employee.getAge());
        employeeEntity.setEmail(employee.getEmail());
        employeeEntity.setPhoneNumber(employee.getPhoneNumber());
        employeeEntity.setPosition(employee.getPosition());

        return employeeEntity;
    }
}
