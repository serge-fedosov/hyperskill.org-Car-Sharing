package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Main {

    static final String JDBC_DRIVER = "org.h2.Driver";
    static String DB_URL = "jdbc:h2:";

    static Scanner scanner = new Scanner(System.in);

    public static void createDB() {

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
            stmt = conn.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS company (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR UNIQUE NOT NULL)";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS car (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR UNIQUE NOT NULL, " +
                "company_id INT NOT NULL REFERENCES company(id))";
//                "company_id INT NOT NULL FOREIGN KEY REFERENCES company(id))";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE IF NOT EXISTS customer (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR UNIQUE NOT NULL, " +
                    "rented_car_id INT REFERENCES car(id))";
            stmt.executeUpdate(sql);

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (Exception ignored) { }
            try {
                if (conn != null) conn.close();
            } catch (Exception ignored) { }
        }
    }

    public static ArrayList<Company> getCompanyList() {
        ArrayList<Company> companies = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
            stmt = conn.createStatement();

            String sql = "SELECT id, name FROM company ORDER BY id";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                companies.add(new Company(rs.getInt("id"), rs.getString("name")));
            }

            rs.close();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (Exception ignored) { }
            try {
                if (conn != null) conn.close();
            } catch (Exception ignored) { }
        }

        return companies.isEmpty() ? null : companies;
    }

    public static ArrayList<Car> getCarList(int id) {
        ArrayList<Car> cars = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
            stmt = conn.createStatement();

            String sql = "SELECT id, name FROM car WHERE company_id = " + id + " ORDER BY id";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                cars.add(new Car(rs.getInt("id"), rs.getString("name")));
            }

            rs.close();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (Exception ignored) { }
            try {
                if (conn != null) conn.close();
            } catch (Exception ignored) { }
        }

        return cars.isEmpty() ? null : cars;
    }

    public static void carList(int id) {
        ArrayList<Car> cars = getCarList(id);
        if (cars == null || cars.size() == 0 ) {
            System.out.println("\nThe car list is empty!");
        } else {
            int count = 1;
            System.out.println("\nCar list:");
            for (var car : cars) {
                System.out.println(count + ". " + car.getName());
                count++;
            }
        }
    }

    public static void createCar(int id) {
        System.out.println("\nEnter the car name:");

        String name = scanner.nextLine();

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
            stmt = conn.createStatement();

            String sql = "INSERT INTO car (name, company_id) VALUES ('" + name + "', " + id + ")";
            stmt.executeUpdate(sql);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (Exception ignored) { }
            try {
                if (conn != null) conn.close();
            } catch (Exception ignored) { }
        }

        System.out.println("The car was added!");
    }

    public static void companyMenu(int id) {

        String command = null;
        do {

            System.out.println("\n1. Car list\n" +
                    "2. Create a car\n" +
                    "0. Back");

            command = scanner.nextLine();
            switch (command) {
                case "1" -> carList(id);
                case "2" -> createCar(id);
                default -> {
                }
            }

        } while (!"0".equals(command));
    }

    public static void companyList() {
        ArrayList<Company> companies = getCompanyList();
        if (companies == null || companies.size() == 0 ) {
            System.out.println("\nThe company list is empty!");
            return;
        } else {
            int count = 1;
            System.out.println("\nChoose the company:");
            for (var company : companies) {
                System.out.println(count + ". " + company.getName());
                count++;
            }
            System.out.println("0. Back");
        }

        String command = scanner.nextLine();
        if (!"0".equals(command)) {
            String name = companies.get(Integer.parseInt(command) - 1).getName();
            int id = companies.get(Integer.parseInt(command) - 1).getId();
            System.out.println("\n'" + name + "' company\n");
            companyMenu(id);
        }
    }

    public static void createCompany() {
        System.out.println("\nEnter the company name:");

        String name = scanner.nextLine();

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
            stmt = conn.createStatement();

            String sql = "INSERT INTO company (name) VALUES ('" + name + "')";
            stmt.executeUpdate(sql);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (Exception ignored) { }
            try {
                if (conn != null) conn.close();
            } catch (Exception ignored) { }
        }

        System.out.println("The company was created!");
    }

    public static void createCustomer() {
        System.out.println("\nEnter the customer name:");

        String name = scanner.nextLine();

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
            stmt = conn.createStatement();

            String sql = "INSERT INTO customer (name) VALUES ('" + name + "')";
            stmt.executeUpdate(sql);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (Exception ignored) { }
            try {
                if (conn != null) conn.close();
            } catch (Exception ignored) { }
        }

        System.out.println("The customer was added!");
    }

    public static void managerMenu() {
        String command = null;
        do {

            System.out.println("\n1. Company list\n" +
                    "2. Create a company\n" +
                    "0. Back");

            command = scanner.nextLine();
            switch (command) {
                case "1" -> companyList();
                case "2" -> createCompany();
                default -> {
                }
            }

        } while (!"0".equals(command));
    }

    public static ArrayList<Customer> getCustomerList() {
        ArrayList<Customer> customers = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
            stmt = conn.createStatement();

            String sql = "SELECT id, name FROM customer ORDER BY id";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                customers.add(new Customer(rs.getInt("id"), rs.getString("name")));
            }

            rs.close();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (Exception ignored) { }
            try {
                if (conn != null) conn.close();
            } catch (Exception ignored) { }
        }

        return customers.isEmpty() ? null : customers;
    }

    public static void customerList() {
        ArrayList<Customer> customers = getCustomerList();
        if (customers == null || customers.size() == 0 ) {
            System.out.println("\nThe customer list is empty!");
            return;
        } else {
            int count = 1;
            System.out.println("\nCustomer list:");
            for (var customer : customers) {
                System.out.println(count + ". " + customer.getName());
                count++;
            }
            System.out.println("0. Back");
        }

        String command = scanner.nextLine();
        if (!"0".equals(command)) {
            String name = customers.get(Integer.parseInt(command) - 1).getName();
            int id = customers.get(Integer.parseInt(command) - 1).getId();
            //System.out.println("\n'" + name + "' company\n");
            customerMenu(id);
        }
    }

    public static void customerMenu(int id) {

        String command = null;
        do {

            System.out.println("\n1. Rent a car\n" +
                    "2. Return a rented car\n" +
                    "3. My rented car\n" +
                    "0. Back");

            command = scanner.nextLine();
            switch (command) {
                case "1" -> rentCar(id);
                case "2" -> returnCar(id);
                case "3" -> customerRentedCar(id);
                default -> {
                }
            }

        } while (!"0".equals(command));
    }

    public static void returnCar(int id) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
            stmt = conn.createStatement();

            String sql = "SELECT rented_car_id FROM customer WHERE id = " + id;
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int carId = rs.getInt("rented_car_id");
            if (carId == 0) {
                System.out.println("\nYou didn't rent a car!");
                return;
            }
            rs.close();

            sql = "UPDATE customer SET rented_car_id = NULL WHERE id = " + id;
            stmt.executeUpdate(sql);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (Exception ignored) { }
            try {
                if (conn != null) conn.close();
            } catch (Exception ignored) { }
        }

        System.out.println("You've returned a rented car!");
    }

    public static void customerRentedCar(int id) {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
            stmt = conn.createStatement();

            String sql = "SELECT rented_car_id FROM customer WHERE id = " + id;
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int carId = rs.getInt("rented_car_id");
            if (carId == 0) {
                System.out.println("\nYou didn't rent a car!");
                return;
            }
            rs.close();

            sql = "SELECT name, company_id FROM car WHERE id = " + carId;
            rs = stmt.executeQuery(sql);
            rs.next();
            String carName = rs.getString("name");
            int companyId = rs.getInt("company_id");
            rs.close();

            sql = "SELECT name FROM company WHERE id = " + companyId;
            rs = stmt.executeQuery(sql);
            rs.next();
            String companyName = rs.getString("name");
            rs.close();

            System.out.println("Your rented car:");
            System.out.println(carName);
            System.out.println("Company:");
            System.out.println(companyName);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (Exception ignored) { }
            try {
                if (conn != null) conn.close();
            } catch (Exception ignored) { }
        }
    }

    public static void rentCar(int id) {

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
            stmt = conn.createStatement();

            String sql = "SELECT rented_car_id FROM customer WHERE id = " + id;
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int carId = rs.getInt("rented_car_id");
            if (carId != 0) {
                System.out.println("\nYou've already rented a car!");
                return;
            }
            rs.close();

            ArrayList<Company> companies = getCompanyList();
            if (companies == null || companies.size() == 0 ) {
                System.out.println("\nThe company list is empty!");
                return;
            } else {
                int count = 1;
                System.out.println("\nChoose a company:");
                for (var company : companies) {
                    System.out.println(count + ". " + company.getName());
                    count++;
                }
                System.out.println("0. Back");
            }

            String command = scanner.nextLine();
            int companyId = 0;
            if ("0".equals(command)) {
                return;
            } else {
                companyId = companies.get(Integer.parseInt(command) - 1).getId();
            }

            HashSet<Integer> rentedCar = new HashSet<>();
            sql = "SELECT rented_car_id FROM customer";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                rentedCar.add(rs.getInt("rented_car_id"));
            }
            rs.close();

            ArrayList<Car> carsWithRenter = getCarList(companyId);
            if (carsWithRenter == null || carsWithRenter.size() == 0 ) {
                System.out.println("\nThe car list is empty!");
                return;
            }

            ArrayList<Car> cars = new ArrayList<>();
            for (var car : carsWithRenter) {
                if (!rentedCar.contains(car.getId())) {
                    cars.add(car);
                }
            }

            if (cars == null || cars.size() == 0 ) {
                System.out.println("\nThe car list is empty!");
                return;
            } else {
                int count = 1;
                System.out.println("\nChoose a car:");
                for (var car : cars) {
                    System.out.println(count + ". " + car.getName());
                    count++;
                }
                System.out.println("0. Back");
            }

            command = scanner.nextLine();
            carId = 0;
            String carName = null;
            if ("0".equals(command)) {
                return;
            } else {
                carId = cars.get(Integer.parseInt(command) - 1).getId();
                carName = cars.get(Integer.parseInt(command) - 1).getName();
            }

            sql = "UPDATE customer SET rented_car_id = " + carId +  " WHERE id = " + id;
            stmt.executeUpdate(sql);

            System.out.println("You rented '" + carName + "'");
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (Exception ignored) { }
            try {
                if (conn != null) conn.close();
            } catch (Exception ignored) { }
        }
    }

    public static void mainMenu() {

        String command = null;
        do {

            System.out.println("\n1. Log in as a manager\n" +
                    "2. Log in as a customer\n" +
                    "3. Create a customer\n" +
                    "0. Exit");

            command = scanner.nextLine();
            switch (command) {
                case "1" -> managerMenu();
                case "2" -> customerList();
                case "3" -> createCustomer();
                default -> {
                }
            }

        } while (!"0".equals(command));
    }

    public static void main(String[] args) {
        if (args.length > 1 && "-databaseFileName".equals(args[0])) {
            DB_URL = DB_URL + "./src/carsharing/db/" + args[1];
        } else {
            DB_URL = DB_URL + "./test.db";
        }

        createDB();;
        mainMenu();
    }
}

class Entry {

    private int id;
    private String name;

    public Entry(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Company extends Entry {

    public Company(int id, String name) {
        super(id, name);
    }
}

class Customer extends Entry {

    public Customer(int id, String name) {
        super(id, name);
    }
}

class Car extends Entry {

    public Car(int id, String name) {
        super(id, name);
    }
}
