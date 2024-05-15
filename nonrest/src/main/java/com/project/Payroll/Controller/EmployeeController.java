package com.project.Payroll.Controller;

import com.project.Payroll.Entities.Employee;
import com.project.Payroll.Repositories.EmployeeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    private final EmployeeRepository repository;

    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/employees")
    List<Employee> getAllEmployees() {
        return this.repository.findAll();
    }

    @PostMapping("/employees")
    Employee newEmployee(@RequestBody Employee employee) {
        return this.repository.save(employee);
    }

    @GetMapping("/employees/{id}")
    Employee getEmployee(@PathVariable Long id) {
        return this.repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @PutMapping("/employees/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        return this.repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return this.repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return this.repository.save(newEmployee);
                });
    }

    @DeleteMapping("/employees/{id}")
    void deleteEmployee(@PathVariable Long id) {
        this.repository.deleteById(id);
    }

}
