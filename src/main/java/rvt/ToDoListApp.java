package rvt;

import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Paths;
import java.io.FileWriter;
import java.io.IOException;

// --- TodoList ---
class TodoList {
    private ArrayList<String> tasks;
    private final String filePath = "rvt\todo.csv";

    public TodoList() {
        this.tasks = new ArrayList<>();
        loadFromFile(); 
    }

    private void loadFromFile() {
        try (Scanner fileScanner = new Scanner(Paths.get(filePath))) {
            if (fileScanner.hasNextLine()) {
                fileScanner.nextLine(); 
            }
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    this.tasks.add(parts[1]);
                }
            }
        } catch (Exception e) {

        }
    }

    private int getLastId() {
        int lastId = 0;
        try (Scanner fileScanner = new Scanner(Paths.get(filePath))) {
            if (fileScanner.hasNextLine()) fileScanner.nextLine(); 
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                lastId = Integer.parseInt(parts[0]);
            }
        } catch (Exception e) {
            return 0;
        }
        return lastId;
    }

    public boolean checkEventString(String value) {
        return value.length() >= 3 && value.matches("^[a-zA-Z0-9 ]+$");
    }

    public void add(String task) {
        this.tasks.add(task);
        int nextId = getLastId() + 1;

        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(nextId + "," + task + "\n");
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл.");
        }
    }

    private boolean updateFile() {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("id,task\n");
            for (int i = 0; i < tasks.size(); i++) {
                writer.write((i + 1) + "," + tasks.get(i) + "\n");
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void remove(int number) {
        int index = number - 1;
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            updateFile();
        }
    }

    public void print() {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ": " + tasks.get(i));
        }
    }
}

// --- UserInterface ---
class UserInterface {
    private TodoList todoList;
    private Scanner scanner;

    public UserInterface(TodoList todoList, Scanner scanner) {
        this.todoList = todoList;
        this.scanner = scanner;
    }

    public void start() {
        while (true) {
            System.out.print("Command: ");
            String command = scanner.nextLine().toLowerCase().trim();

            if (command.equals("stop")) {
                break;
            }

            if (command.equals("add")) {
                System.out.print("To add: ");
                String task = scanner.nextLine();
                if (todoList.checkEventString(task)) {
                    todoList.add(task);
                } else {
                    System.out.println("Invalid task! Use only letters/numbers (min 3).");
                }

            } else if (command.equals("list")) {
                todoList.print();
            } else if (command.equals("remove")) {
                System.out.print("Which one is removed? ");
                try {
                    int idToRemove = Integer.parseInt(scanner.nextLine());
                    todoList.remove(idToRemove);
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a number.");
                }
            }
        }
    }
}

// --- Main ---
public class ToDoListApp {
    public static void main(String[] args) {
        TodoList list = new TodoList();
        Scanner scanner = new Scanner(System.in);
        UserInterface ui = new UserInterface(list, scanner);
        
        ui.start();
    }
}