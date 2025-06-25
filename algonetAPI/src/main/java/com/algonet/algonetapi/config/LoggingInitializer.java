package com.algonet.algonetapi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
@Slf4j
public class LoggingInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        initializeCsvLogFile();
        log.info("Application logging system initialized");
    }

    private void initializeCsvLogFile() {
        File logsDir = new File("logs");
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }

        File csvFile = new File("logs/algonet.csv");
        if (!csvFile.exists()) {
            try (FileWriter writer = new FileWriter(csvFile)) {
                // Write CSV header
                writer.write("\"Timestamp\",\"Level\",\"Logger\",\"Message\"\n");
                log.info("CSV log file initialized with headers");
            } catch (IOException e) {
                log.error("Failed to initialize CSV log file", e);
            }
        }
    }
}
