package rvt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

// --- MODELI (Datu struktūras) ---

// Klase, kas reprezentē kategoriju datubāzē
class Category {
    private int id;
    private String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }
}

// Klase, kas reprezentē produktu datubāzē
class Product {
    private int id;
    private String name;
    private double price;
    private int categoryId;

    public Product(int id, String name, double price, int categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getCategoryId() { return categoryId; }
}

// --- DATUBĀZES SAVIENOJUMS ---

class DatabaseConnection {
    // Definējam datubāzes faila nosaukumu. Fails tiks izveidots projekta saknes mapē.
    private static final String URL = "jdbc:sqlite:veikals.db";

    // Metode savienojuma izveidošanai ar SQLite
    public static Connection connect() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        // OBLIGĀTI: Ieslēdzam Foreign Key (ārējo atslēgu) atbalstu, jo SQLite pēc noklusējuma to neizpilda
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    // Metode tabulu inicializācijai
    public static void createNewTables() {
        // Tabulas 'categories' SQL definīcija
        String sqlCategories = "CREATE TABLE IF NOT EXISTS categories ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " name TEXT NOT NULL"
                + ");";

        // Tabulas 'products' SQL definīcija ar relāciju uz categories(id)
        String sqlProducts = "CREATE TABLE IF NOT EXISTS products ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " name TEXT NOT NULL,"
                + " price REAL NOT NULL,"
                + " category_id INTEGER NOT NULL,"
                + " FOREIGN KEY (category_id) REFERENCES categories(id)"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            // Izpildām tabulu izveidi
            stmt.execute(sqlCategories);
            stmt.execute(sqlProducts);
            System.out.println("[Sistēma] Datubāzes fails un tabulas ir pārbaudītas/sagatavotas.");
        } catch (SQLException e) {
            System.out.println("Kļūda, inicializējot datubāzi: " + e.getMessage());
        }
    }
}

// --- GALVENĀ PROGRAMMA UN LIETOTĀJA SASKARNE ---

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Programmas sākumā pārbaudām un izveidojam datubāzi/tabulas
        DatabaseConnection.createNewTables();

        boolean running = true;
        while (running) {
            System.out.println("--- Izvēlne ---");
            System.out.println("1 - Pievienot kategoriju");
            System.out.println("2 - Pievienot produktu");
            System.out.println("3 - Parādīt visas kategorijas");
            System.out.println("4 - Parādīt visus produktus");
            System.out.println("5 - Meklēt produktus pēc kategorijas");
            System.out.println("0 - Iziet");
            System.out.print("Jūsu izvēle: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Kļūda: Lūdzu, ievadiet derīgu ciparu!");
                continue;
            }

            switch (choice) {
                case 1:
                    addCategory();
                    break;
                case 2:
                    addProduct();
                    break;
                case 3:
                    showCategories();
                    break;
                case 4:
                    showProducts();
                    break;
                case 5:
                    searchProductsByCategory();
                    break;
                case 0:
                    running = false;
                    System.out.println("Programma tiek aizvērta. Uz redzēšanos!");
                    break;
                default:
                    System.out.println("Nederīga izvēle, mēģiniet vēlreiz.");
            }
        }
    }

    // 1. Funkcija jaunas kategorijas pievienošanai
    private static void addCategory() {
        System.out.print("Ievadiet jaunās kategorijas nosaukumu: ");
        String name = scanner.nextLine();

        // Izmantojam '?', lai novērstu SQL injekcijas (SQL Injection)
        String sql = "INSERT INTO categories(name) VALUES(?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("Kategorija '" + name + "' veiksmīgi pievienota!");
            
        } catch (SQLException e) {
            System.out.println("Kļūda, pievienojot kategoriju: " + e.getMessage());
        }
    }

    // 2. Funkcija jauna produkta pievienošanai
    private static void addProduct() {
        System.out.print("Ievadiet produkta nosaukumu: ");
        String name = scanner.nextLine();

        System.out.print("Ievadiet produkta cenu: ");
        double price = 0;
        try {
            price = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Kļūda: Nederīgs cenas formāts. Darbība atcelta.");
            return;
        }

        // Parādām esošās kategorijas, lai lietotājs zina ID
        showCategories();
        System.out.print("Ievadiet izvēlētās kategorijas ID no saraksta: ");
        int categoryId = 0;
        try {
            categoryId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Kļūda: Nederīgs ID formāts. Darbība atcelta.");
            return;
        }

        // Drošs vaicājums ar PreparedStatement
        String sql = "INSERT INTO products(name, price, category_id) VALUES(?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, categoryId);
            pstmt.executeUpdate();
            System.out.println("Produkts '" + name + "' veiksmīgi pievienots!");
            
        } catch (SQLException e) {
            System.out.println("Kļūda, pievienojot produktu: " + e.getMessage());
        }
    }

    // 3. Funkcija visu kategoriju attēlošanai
    private static void showCategories() {
        String sql = "SELECT * FROM categories";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("--- Visas Kategorijas ---");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.println("ID: " + rs.getInt("id") + " | Nosaukums: " + rs.getString("name"));
            }
            if (!hasData) System.out.println("Kategoriju saraksts ir tukšs.");
            System.out.println("-------------------------");
            
        } catch (SQLException e) {
            System.out.println("Kļūda, nolasot kategorijas: " + e.getMessage());
        }
    }

    // 4. Funkcija visu produktu attēlošanai (ar kategorijas nosaukumu)
    private static void showProducts() {
        // Izmantojam LEFT JOIN, lai apvienotu tabulas un iegūtu kategorijas nosaukumu
        String sql = "SELECT p.id, p.name, p.price, c.name AS category_name " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("--- Visi Produkti ---");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.println("ID: " + rs.getInt("id") + 
                                   " | Nosaukums: " + rs.getString("name") + 
                                   " | Cena: " + rs.getDouble("price") + " EUR" +
                                   " | Kategorija: " + rs.getString("category_name"));
            }
            if (!hasData) System.out.println("Produktu saraksts ir tukšs.");
            System.out.println("---------------------");
            
        } catch (SQLException e) {
            System.out.println("Kļūda, nolasot produktus: " + e.getMessage());
        }
    }

    // 5. Funkcija meklēšanai pēc kategorijas ID vai nosaukuma
    private static void searchProductsByCategory() {
        System.out.print("Ievadiet meklējamās kategorijas ID vai nosaukumu: ");
        String searchInput = scanner.nextLine();

        // Drošs SQL vaicājums, kas meklē pēc ID vai daļēja nosaukuma (LIKE)
        String sql = "SELECT p.id, p.name, p.price, c.name AS category_name " +
                     "FROM products p " +
                     "JOIN categories c ON p.category_id = c.id " +
                     "WHERE c.id = ? OR c.name LIKE ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            int searchId = -1;
            try {
                searchId = Integer.parseInt(searchInput);
            } catch (NumberFormatException ignored) {
                // Ja lietotājs ievadīja tekstu, ID paliek -1 un datubāze atlasīs pēc nosaukuma
            }

            pstmt.setInt(1, searchId);
            pstmt.setString(2, "%" + searchInput + "%"); // Pievienojam procentu zīmes LIKE operācijai

            ResultSet rs = pstmt.executeQuery();

            System.out.println("--- Meklēšanas Rezultāti ---");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Produkts: " + rs.getString("name") + 
                                   " (ID: " + rs.getInt("id") + ")" +
                                   " | Cena: " + rs.getDouble("price") + " EUR" +
                                   " | Kategorija: " + rs.getString("category_name"));
            }
            
            if (!found) {
                System.out.println("Nekas netika atrasts pēc kritērija: " + searchInput);
            }
            System.out.println("----------------------------");

        } catch (SQLException e) {
            System.out.println("Kļūda, meklējot produktus: " + e.getMessage());
        }
    }
}
