package vn.ptit.hrms.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import vn.ptit.hrms.service.DataGeneratorService;

@Component
@ConditionalOnProperty(name = "app.data-generator.enabled", havingValue = "true")
public class DataGeneratorRunner implements CommandLineRunner {

    private final DataGeneratorService dataGeneratorService;

    @Autowired
    public DataGeneratorRunner(DataGeneratorService dataGeneratorService) {
        this.dataGeneratorService = dataGeneratorService;
    }

    @Override
    public void run(String... args) {
        System.out.println("Starting data generation process...");
        // Generate fresh data
        dataGeneratorService.generateData();

        System.out.println("Data generation process completed.");
    }
}