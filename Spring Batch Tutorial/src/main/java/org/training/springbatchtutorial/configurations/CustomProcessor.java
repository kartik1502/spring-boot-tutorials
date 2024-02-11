package org.training.springbatchtutorial.configurations;

import org.springframework.batch.item.ItemProcessor;
import org.training.springbatchtutorial.model.entity.Employee;

public class CustomProcessor implements ItemProcessor<Employee, Employee> {

    @Override
    public Employee process(Employee employee) throws Exception {
        return employee;
    }

}
