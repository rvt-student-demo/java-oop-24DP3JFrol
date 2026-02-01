package rvt;

import java.util.Scanner;

public class Chapter100Exercises {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Chapter 100 Programming Exercises ===");
        System.out.println("1. User-Friendly Division Practice");
        System.out.println("2. Addition of Multiple Groups");
        System.out.print("\nSelect exercise (1 or 2): ");
        
        String choice = scanner.nextLine().trim();
        
        if (choice.equals("1")) {
            System.out.println("\n--- Exercise 1: Division Practice ---\n");
            runDivisionPractice();
        } else if (choice.equals("2")) {
            System.out.println("\n--- Exercise 2: Group Addition ---");
            System.out.println("(Use file redirection for input, e.g.: java Chapter100Exercises < data.txt)");
            System.out.println("Enter your data (Ctrl+D or Ctrl+Z to finish):\n");
            runGroupAddition();
        } else {
            System.out.println("Invalid choice. Please run again and select 1 or 2");
        }
        
        scanner.close();
    }
    
    // EXERCISE 1
    public static void runDivisionPractice() {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.print("Enter the numerator: ");
            String numeratorInput = scanner.nextLine().trim();
            
            
            if (numeratorInput.length() > 0 && 
                (numeratorInput.charAt(0) == 'q' || numeratorInput.charAt(0) == 'Q')) {
                break;
            }
            
            int numerator;
            try {
                numerator = Integer.parseInt(numeratorInput);
            } catch (NumberFormatException e) {
                System.out.println("You entered bad data.");
                System.out.println("Please try again.\n");
                continue;
            }
            
            System.out.print("Enter the divisor:  ");
            String divisorInput = scanner.nextLine().trim();
            
            int divisor;
            try {
                divisor = Integer.parseInt(divisorInput);
            } catch (NumberFormatException e) {
                System.out.println("You entered bad data.");
                System.out.println("Please try again.\n");
                continue;
            }
            
            if (divisor == 0) {
                System.out.println("You can't divide " + numerator + " by 0\n");
            } else {
                int result = numerator / divisor;
                System.out.println(numerator + " / " + divisor + " is " + result + "\n");
            }
            scanner.close();
        }
        
        System.out.println("\nDivision Practice finished.");
    }
    
    // EXERCISE 2
    public static void runGroupAddition() {
        Scanner scanner = new Scanner(System.in);
        String currentGroup = null;
        int sum = 0;
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            
            if (line.isEmpty()) {
                continue;
            }
            
            try {
                int number = Integer.parseInt(line);
                sum += number;
            } catch (NumberFormatException e) {
                
                if (currentGroup != null) {
                    System.out.println(currentGroup);
                    System.out.println("Sum = " + sum);
                    System.out.println();
                }
                currentGroup = line;
                sum = 0;
            }
        }
        
        
        if (currentGroup != null) {
            System.out.println(currentGroup);
            System.out.println("Sum = " + sum);
        }
        
        scanner.close();
    }
}