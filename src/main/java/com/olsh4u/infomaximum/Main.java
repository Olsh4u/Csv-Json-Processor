package com.olsh4u.infomaximum;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter file path (or 'exit' to quit): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            FileProcessor fileProcessor = new FileProcessor();
            fileProcessor.processFile(input);
        }

        scanner.close();
    }
}
