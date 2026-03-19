package rvt.UzdevumsArCSV;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// --- 1. STUDENT ---
class Student {
    private String firstName;
    private String lastName;
    private String email;
    private String personalCode;
    private String registrationDate;

    public Student(String firstName, String lastName, String email, String personalCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.personalCode = personalCode;
        this.registrationDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public Student(String firstName, String lastName, String email, String personalCode, String date) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.personalCode = personalCode;
        this.registrationDate = date;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPersonalCode() { return personalCode; }
    public String getRegistrationDate() { return registrationDate; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }

    public String toCsv() {
        return String.join(",", firstName, lastName, email, personalCode, registrationDate);
    }
}

// --- 2. VALIDATOR ---
class Validator {
    public static boolean isValidName(String name) {
        return name != null && name.matches("[a-zA-ZĀ-ž]{3,}");
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static boolean isValidPersonalCode(String code) {
        return code != null && code.matches("\\d{6}-\\d{5}");
    }
}

// --- 3. FILEHANDLER ---
class FileHandler {
    private static final String FILE_NAME = "students.csv";

    public static void saveStudents(List<Student> students) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Student s : students) {
                writer.println(s.toCsv());
            }
        } catch (IOException e) {
            System.err.println("Recording error: " + e.getMessage());
        }
    }

    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return students;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    students.add(new Student(parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            }
        } catch (IOException e) {
            System.err.println("Reading error: " + e.getMessage());
        }
        return students;
    }
}

// --- 4. REGISTRATIONSYSTEM ---
public class RegistrationSystem {
    private static List<Student> students = FileHandler.loadStudents();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== Student Database ===");
            System.out.println("[register] Registration | [show] List | [remove] Delete | [edit] Edit | [exit] Exit");
            System.out.print("Введите команду: ");
            String choice = scanner.nextLine().trim().toLowerCase();
            switch (choice) {
                case "register": register(); break;
                case "show": showStudents(); break;
                case "remove": remove(); break;
                case "edit": edit(); break;
                case "exit": return;
                default: System.out.println("Command not recognized.");
            }
        }
    }

    private static void register() {
        System.out.print("Name: "); String fName = scanner.nextLine();
        System.out.print("Last name: "); String lName = scanner.nextLine();
        System.out.print("Email: "); String email = scanner.nextLine();
        System.out.print("Pers. kode: (123456-12345): "); String code = scanner.nextLine();

        if (!Validator.isValidName(fName) || !Validator.isValidName(lName)) {
            System.out.println("Error: Name/Last name is incorrect."); return;
        }
        if (!Validator.isValidEmail(email)) {
            System.out.println("Error: Wrong Email"); return;
        }
        if (!Validator.isValidPersonalCode(code)) {
            System.out.println("Error: Wrong personal code format."); return;
        }

        for (Student s : students) {
            if (s.getPersonalCode().equals(code)) {
                System.out.println("Error: The same personal code already exist"); return;
            }
        }

        students.add(new Student(fName, lName, email, code));
        FileHandler.saveStudents(students);
        System.out.println("Sucsesfully added!");
    }

    private static void showStudents() {
        if (students.isEmpty()) {
            System.out.println("Database is empty."); return;
        }

        String hr = "+-----------------+-----------------+---------------------------+---------------+---------------------+";
        System.out.println(hr);
        System.out.printf("| %-15s | %-15s | %-25s | %-13s | %-19s |\n", "Name", "Last name", "Email", "Personal code", "Reg. Date");
        System.out.println(hr);

        for (Student s : students) {
            System.out.printf("| %-15s | %-15s | %-25s | %-13s | %-19s |\n",
                    s.getFirstName(), s.getLastName(), s.getEmail(), s.getPersonalCode(), s.getRegistrationDate());
        }
        System.out.println(hr);
    }

    private static void remove() {
        System.out.print("Enter personal code to delete: ");
        String code = scanner.nextLine();
        if (students.removeIf(s -> s.getPersonalCode().equals(code))) {
            FileHandler.saveStudents(students);
            System.out.println("Deleted.");
        } else {
            System.out.println("Not found.");
        }
    }

    private static void edit() {
        System.out.print("Enter personal code to make changes: ");
        String code = scanner.nextLine();
        for (Student s : students) {
            if (s.getPersonalCode().equals(code)) {
                System.out.print("New name: "); s.setFirstName(scanner.nextLine());
                System.out.print("New last name: "); s.setLastName(scanner.nextLine());
                System.out.print("New Email: "); s.setEmail(scanner.nextLine());
                FileHandler.saveStudents(students);
                System.out.println("Data updated.");
                return;
            }
        }
        System.out.println("Student not found.");
    }
}