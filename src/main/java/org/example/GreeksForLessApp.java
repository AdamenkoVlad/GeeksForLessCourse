package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GreeksForLessApp {
    private static final Logger LOGGER = Logger.getLogger(GreeksForLessApp.class.getName());
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

            String url = "jdbc:postgresql://localhost:5432/GreeksForLess";
            String username = "postgres";
            String password = "4556";


//       System.out.println(Equation.getRoot("2*x+5=17",6)); //false
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            while (true) {
                System.out.println("Choose an option:");
                System.out.println("1. Save an equation");
                System.out.println("2. Find equations with specific roots");
                System.out.println("3. Find equations with exactly one root");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");

                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        saveEquation(connection);
                        break;
                    case 2:
                        findEquationsWithRoots(connection);
                        break;
                    case 3:
                        findEquationsWithSingleRoot(connection);
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Connection failed...", ex);
        }
    }

    private static void saveEquation(Connection connection) throws SQLException {
        System.out.print("Enter an equation to save: ");
        String equation = scanner.nextLine();
        if( Equation.isValidEquation(equation)){
            boolean isSaved = Equation.save(connection, equation);
            System.out.println("Equation \"" + equation + "\" is valid and saved: " + isSaved);
        }
        else System.out.println("Equation is not valid");

    }

    private static void findEquationsWithRoots(Connection connection) throws SQLException {
        System.out.print("Enter roots splitted by space: ");
        String[] rootsInput = scanner.nextLine().split(" ");
        List<Double> roots = new ArrayList<>();
        for (String root : rootsInput) {
            roots.add(Double.parseDouble(root.trim()));
        }
        List<String> equations = Equation.findEquationsWithRoots(connection, roots);
        System.out.println("Equations with specified roots: " + equations);
    }

    private static void findEquationsWithSingleRoot(Connection connection) throws SQLException {
        List<String> equations = Equation.findEquationsWithSingleRoot(connection);
        System.out.println("Equations with exactly one root: " + equations);
    }
}
