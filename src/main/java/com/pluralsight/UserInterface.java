package com.pluralsight;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {
    private Dealership dealership;
    Scanner scanner = new Scanner(System.in);

    private void init() {
        this.dealership = DealershipFileManager.getDealership();
    }

    public void display() {
        init();
        String menu = """
                ==* MENU *==
                [1] - Find vehicles within a price range\s
                [2] - Find vehicles by make / model\s
                [3] - Find vehicles by year range\s
                [4] - Find vehicles by color\s
                [5] - Find vehicles by mileage range\s
                [6] - Find vehicles by type (car, truck, SUV, van)\s
                [7] - List ALL vehicles\s
                [8] - Add a vehicle\s
                [9] - Remove a vehicle\s
                [99]- Quit
                
                ENTER option:\s""";

        String option;
        boolean exit = false;

        do {
            System.out.print(menu);
            option = scanner.nextLine().trim();

            switch (option) {
                case "1" -> processGetByPriceRequest();
                case "2" -> processGetByMakeModelRequest();
                case "3" -> processGetByYearRequest();
                case "4" -> processGetByColorRequest();
                case "5" -> processGetByMileageRequest();
                case "6" -> processGetByVehicleTypeRequest();
                case "7" -> processGetAllVehiclesRequest();
                case "8" -> System.out.print("processAddVehicleRequest()");
                case "9" -> System.out.print("processRemoveVehicleRequest()");
                case "99" -> {
                    System.out.println("\nBye Bye :)");
                    exit = true;
                }
                default -> System.out.println("⚠\uFE0F Not an option: Try again\n");
            }

        } while (!exit);
    }

    //done: change to private
    private void displayVehicles(ArrayList<Vehicle> vehicles) {
        //done: create a loop and display the vehicles;
        String closeTable = "-".repeat(94);

        String rowFormatHeading = "| %-5s | %-4s | %-10s | %-14s | %-10s | %-7s | %-8s | %-10s | %n";
        String rowFormat = "| %-5s | %-4s | %-10s | %-14s | %-11s | %-7s | %-8s | $%-9s | %n";

        System.out.println(closeTable);
        System.out.printf(rowFormatHeading, "vin", "year", "make", "model", "vehicleType", "color", "odometer", "price");
        System.out.println(closeTable);

        for (Vehicle v : vehicles) {
            System.out.printf(rowFormat,
                    v.getVin(),
                    v.getYear(),
                    v.getMake(),
                    v.getModel(),
                    v.getVehicleType(),
                    v.getColor(),
                    v.getOdometer(),
                    v.getPrice());
        }
        System.out.println(closeTable + "\n");
    }

    public void processGetAllVehiclesRequest() {
        ArrayList<Vehicle> allVehicles = dealership.getAllVehicles();
        displayVehicles(allVehicles);
    }

    public void processGetByPriceRequest() {
        System.out.println("\t[SEARCH] - Price");
        BigDecimal min = BigDecimal.ZERO;
        BigDecimal max = BigDecimal.ZERO;

        boolean valid = false;
        do {
            try {
                //todo: defend against negative numbers.
                System.out.print("\tENTER Min price: $");
                min = new BigDecimal(scanner.nextLine());

                System.out.print("\tENTER Max price: $");
                max = new BigDecimal(scanner.nextLine());

                valid = true;

            } catch (NumberFormatException nfe) {
                System.out.println("\tNot a valid number: " + nfe.getMessage());
            }
        } while (!valid);

        if (this.dealership.getVehiclesByPrice(min, max).isEmpty()) {
            System.out.println("\n\t=* no vehicles found *=\n");
        } else {
            System.out.println("\nprice.");
            displayVehicles(this.dealership.getVehiclesByPrice(min, max));
        }
    }

    public void processGetByMakeModelRequest() {
        System.out.println("\t[SEARCH] - Make & Model");
        String make;
        String model;

        boolean notValid;
        do {

            System.out.print("\tENTER Make: ");
            make = scanner.nextLine();

            System.out.print("\tENTER Model: ");
            model = scanner.nextLine();

            //done: find a better way to implement
            notValid = make == null || make.isBlank() || model == null || model.isBlank();
            if (notValid) {
                System.out.println("\tInput cannot be empty\n");
            }

        } while (notValid);

        if (this.dealership.getVehiclesByMakeModel(make, model).isEmpty()) {
            System.out.println("\n\t=* no vehicles found *=\n");
        } else {
            System.out.println("\nmake and model.");
            displayVehicles(this.dealership.getVehiclesByMakeModel(make, model));
        }
    }

    public void processGetByYearRequest() {
        System.out.println("\t[SEARCH] - Year");
        int min = 0;
        int max = 0;

        boolean valid = false;
        do {
            try {
                System.out.print("\tENTER Earliest year: ");
                min = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("\tENTER Latest year: ");
                max = Integer.parseInt(scanner.nextLine().trim());

                valid = true;

            } catch (NumberFormatException nfe) {
                System.out.println("\tNot a valid year: " + nfe.getMessage());
            }
        } while (!valid);

        if (this.dealership.getVehiclesByYear(min, max).isEmpty()) {
            System.out.println("\n\t=* no vehicles found *=\n");
        } else {
            System.out.println("\nyear.");
            displayVehicles(this.dealership.getVehiclesByYear(min, max));
        }
    }

    public void processGetByColorRequest() {
        System.out.println("[SEARCH] - Color");

        String color;
        boolean valid = false;

        do {
            System.out.print("\tENTER Color: ");
            color = scanner.nextLine().trim().toLowerCase();
            valid = (color.isBlank()) ? false : true;

        } while (!valid);

        if (this.dealership.getVehiclesByColor(color).isEmpty()) {
            System.out.println("\n\t=* no vehicles found *=\n");
        } else {
            System.out.println("\ncolor.");
            displayVehicles(this.dealership.getVehiclesByColor(color));
        }
    }

    public void processGetByMileageRequest() {
        System.out.println("\t[SEARCH] - Mileage");
        int min = 0;
        int max = 0;

        boolean valid = false;
        do {
            try {
                //todo: defend against negative numbers.
                System.out.print("\tENTER Min: ");
                min = Integer.parseInt(scanner.nextLine().trim());

                System.out.print("\tENTER Max: ");
                max = Integer.parseInt(scanner.nextLine().trim());

                valid = true;

            } catch (NumberFormatException nfe) {
                System.out.println("\tNot a valid number: " + nfe.getMessage());
            }
        } while (!valid);

        if (this.dealership.getVehiclesByMileage(min, max).isEmpty()) {
            System.out.println("\n\t=* no vehicles found *=\n");
        } else {
            System.out.println("\nmileage.");
            displayVehicles(this.dealership.getVehiclesByMileage(min, max));
        }
    }

    public void processGetByVehicleTypeRequest() {
        System.out.println("[SEARCH] - Vehicle type");

        String vehicleType;
        boolean valid = false;

        do {
            System.out.print("\tENTER Type: ");
            vehicleType = scanner.nextLine().trim().toLowerCase();
            valid = (vehicleType.isBlank()) ? false : true;

        } while (!valid);

        if (this.dealership.getVehiclesByType(vehicleType).isEmpty()) {
            System.out.println("\n\t=* no vehicles found *=\n");
        } else {
            System.out.println("\nvehicle type.");
            displayVehicles(this.dealership.getVehiclesByType(vehicleType));
        }
    }
}