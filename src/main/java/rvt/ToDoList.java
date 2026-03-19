package rvt;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ToDoList {
    // PART 1
    public static class TodoList {
        private ArrayList<String> tasks;

        private final String filePath = "rvt/todo.csv";

        private void loadFromFile() {
            try {
                File file = new File(filePath);
                Scanner reader = new Scanner(file);

                if (fileScanner.hasNextLine()) {
                    fileScanner.nextLine();
                }
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String[] parts = line.split(";");
                    if (parts.lenght > 1) {
                        tasks.add(parts[1]);
                    }
                }

                fileScanner.close();

            } catch (Exception e) {
                System.out.println("Error loading tasks: ");
            }
        }

        private void updateFile() {
            try {
                FileWriter writer = new FileWriter(filePath);
                writer.write("id,task\n");
                for (int i = 0; i<tasks.size(); i++) {
                    writer.write((i + 1) + "," + tasks.get(i) + "\n");
                }
                writer.close();
            } catch (IOException e) {
                System.out.println("Error updating file: ");
            }
        }

        public TodoList(){
            this.tasks = new ArrayList<>();
        }
        public void add(String task) {
            this.tasks.add(task);
        }
        public void print() {
            for (int i = 0; 1 < tasks.size(); i++) {
                System.out.println((i + 1) + ": " + tasks.get(i));
            }
        }
        public void remove(int number) {
            this.tasks.remove(number - 1);
        }
    }
    // PART 2
    public static class UserInterface {
        private TodoList todoList;
        private Scanner scanner;
        public UserInterface(TodoList todoList, Scanner scanner) {
            this.todoList = todoList;
            this.scanner = scanner;
        }
        public void Start() {
            while (true) {
                System.out.print("Command: ");
                String command = scanner.nextLine();
                if (command.equals("stop")) {
                    break;
                }   if (command.equals("add")) {
                    System.out.println("To add: ");
                    String task = scanner.nextLine();
                    todoList.add(task);
                }   if (command.equals("list")) {
                    todoList.print();
                }   if (command.equals("remove")) {
                    System.out.print("Which one is removed?");
                    int number = Integer.valueOf(scanner.nextLine());
                    todoList.remove(number);
                }
            }
        }
    }
    public static void main(String[] args) {
        TodoList todoList = new TodoList();
        Scanner scanner = new Scanner(System.in);
        UserInterface ui = new UserInterface(todoList, scanner);
        ui.Start();
    }
}
