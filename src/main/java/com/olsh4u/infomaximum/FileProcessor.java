package com.olsh4u.infomaximum;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileProcessor {
    private final ObjectStats objectStats;


    public FileProcessor() {
        objectStats = new ObjectStats();
    }

    protected void processFile(String filePath) {
        if (!checkFileExistence(filePath)) {
            return;
        }

        String fileExtension = getFileExtension(Paths.get(filePath));

        if (fileExtension.equalsIgnoreCase("csv")) {
            processCsvFile(filePath);
        }
        else if (fileExtension.equalsIgnoreCase("json")) {
            processJsonFile(filePath);
        }
        else {
            System.out.println("Unsupported file format: " + fileExtension + " JSON or CSV required");
            return;
        }

        objectStats.printSummaryStatistics();
    }

    void processCsvFile(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            reader.readNext();// to skip the first line with headers
            String[] line;
            while ((line = reader.readNext()) != null) {
                try {
                    processCsvObjectLine(line);
                    ObjectStats.countOfElements++;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format in the CSV file: " + e.getMessage());
                }
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException("CSV validation error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    }


    public void processJsonFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jsonParser = jsonFactory.createParser(reader);

            while (jsonParser.nextToken() == JsonToken.START_ARRAY) {
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    processJsonObject(jsonParser);
                    ObjectStats.countOfElements++;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
        }
    }

    protected void processCsvObjectLine(String[] line) throws NumberFormatException {
        objectStats.processObject(
                line[0],
                line[1],
                new BigDecimal(line[2]),
                new BigDecimal(line[3])
        );
    }

    protected void processJsonObject(JsonParser jsonParser) throws IOException {
        String group = null;
        String type = null;
        BigDecimal number = null;
        BigDecimal weight = null;

        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jsonParser.getCurrentName();
            jsonParser.nextToken();
            switch (fieldName) {
                case "group" -> group = jsonParser.getValueAsString();
                case "type" -> type = jsonParser.getValueAsString();
                case "number" -> number = jsonParser.getDecimalValue();
                case "weight" -> weight = jsonParser.getDecimalValue();
                default -> throw new IllegalArgumentException("Unrecognized field: " + fieldName);
            }
        }
        objectStats.processObject(group, type, number, weight);
    }
    private String getFileExtension(Path path) {
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    private boolean checkFileExistence(String filePath) {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            System.out.println("File does not exist: " + filePath);
            return false;
        }
        return true;
    }

}
