package org.training.springbatchtutorial.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.training.springbatchtutorial.model.entity.Employee;
import org.training.springbatchtutorial.repository.EmployeeRepository;

@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {

    private final EmployeeRepository employeeRepository;
    @Bean
    public FlatFileItemReader<Employee> reader() {

        FlatFileItemReader<Employee> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/employee.csv"));
        itemReader.setName("readCsv");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<Employee> lineMapper() {

        DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("employee_id", "first_name", "last_name", "gender", "contact_no", "country", "date_of_birth");

        BeanWrapperFieldSetMapper<Employee> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Employee.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public CustomProcessor processor() {
        return new CustomProcessor();
    }
    @Bean
    public RepositoryItemWriter<Employee> writer() {
        RepositoryItemWriter<Employee> itemWriter = new RepositoryItemWriter<>();
        itemWriter.setRepository(employeeRepository);
        itemWriter.setMethodName("save");
        return itemWriter;
    }

    @Bean
    public Step createRecords(JobRepository jobRepository, PlatformTransactionManager transactionManager) {

        return new StepBuilder( "createRecords", jobRepository)
                .<Employee, Employee>chunk(10, transactionManager)
                .reader(reader())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job runJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {

        return new JobBuilder("runJob", jobRepository)
                .flow(createRecords(jobRepository, transactionManager))
                .end().build();
    }

    @Bean
    public TaskExecutor taskExecutor() {

        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }
}
