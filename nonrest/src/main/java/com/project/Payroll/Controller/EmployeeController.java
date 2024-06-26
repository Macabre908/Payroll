package com.project.Payroll.Controller;

import com.project.Payroll.Entities.Employee;
import com.project.Payroll.Exceptions.EmployeeNotFoundException;
import com.project.Payroll.Repositories.EmployeeRepository;
import com.project.Payroll.evolution.EmployeeModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class EmployeeController {

    private final EmployeeRepository repository;
    private final EmployeeModelAssembler employeeModelAssembler;

    public EmployeeController(EmployeeRepository repository, EmployeeModelAssembler employeeModelAssembler) {
        this.repository = repository;
        this.employeeModelAssembler = employeeModelAssembler;
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return this.repository.findAll();
    }

    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@RequestBody Employee employee) {
        EntityModel<Employee> entityModel = employeeModelAssembler.toModel(repository.save(employee));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return this.repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @PutMapping("/employees/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        Employee updatedEmployee = this.repository.findById(id)
                .map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                    return this.repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return this.repository.save(newEmployee);
                });

        EntityModel<Employee> entityModel = employeeModelAssembler.toModel(updatedEmployee);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);

    }

    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        this.repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
