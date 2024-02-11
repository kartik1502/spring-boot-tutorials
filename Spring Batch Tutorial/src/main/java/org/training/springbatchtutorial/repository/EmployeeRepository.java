package org.training.springbatchtutorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.training.springbatchtutorial.model.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
