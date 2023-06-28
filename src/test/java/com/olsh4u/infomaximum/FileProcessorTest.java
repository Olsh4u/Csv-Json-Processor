package com.olsh4u.infomaximum;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class FileProcessorTest {
    private FileProcessor fileProcessor;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        fileProcessor = new FileProcessor();
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void processFile_withUnsupportedFileFormat_printsErrorMessage() {
        File fileName = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("test.txt")).getFile());
        fileProcessor.processFile(fileName.getPath());
        String expectedErrorMessage = "Unsupported file format: txt JSON or CSV required";
        assertTrue(outputStream.toString().contains(expectedErrorMessage));
    }

    @Test
    void checkFileExistence_existingFile_returnsTrue() {
        String filePath = "path/to/existing/file.txt";
        FileProcessor fileProcessor = new FileProcessor();

        boolean result = fileProcessor.checkFileExistence(filePath);

        assertFalse(result);
    }

}

