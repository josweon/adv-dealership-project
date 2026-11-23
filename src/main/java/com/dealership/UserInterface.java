package com.dealership;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.System.exit;

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
                [0] - Sell/Lease a vehicle\s
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
                case "8" -> processAddVehicleRequest();
                case "9" -> processRemoveVehicleRequest();
                case "0" -> processSellLeaseVehicleRequest();
                case "99" -> {
                    System.out.println("\nBye Bye :)");
                    exit = true;
                }
                default -> System.out.println("⚠\uFE0F Not an option: Try again\n");
            }

        } while (!exit);
    }

    private void processSellLeaseVehicleRequest() {
        String menu = """
                \t=* CONTRACT *=
                \t[1] Sell vehicle
                \t[2] Lease vehicle
                \t[0] <-Back
                
                \tENTER option:\s""";

        String option;
        boolean exit = false;

        do {
            System.out.print(menu);
            option = scanner.nextLine();

            switch (option) {
                case "1" -> processSellVehicleRequest();
                case "2" -> processLeaseVehicleRequest();
                case "0" -> exit = true;
                default -> System.out.println("\t⚠\uFE0F Not an option: Try again\n");
            }

        } while (!exit);

    }

    private void processLeaseVehicleRequest() {
        System.out.println("\nlease.");

        displayVehicles(this.dealership.getAllVehicles());
        Vehicle vehicle = null;

        do {
            int vin = promptInt("vin");
            vehicle = this.dealership.getVehiclesByVin(vin);

            if (vehicle != null) {
                System.out.printf("\t[LEASE] %s %s %s%n",
                        vehicle.getYear(),
                        vehicle.getMake(),
                        vehicle.getModel());
            }

        } while (vehicle == null);

        LocalDate now = LocalDate.now();
        String customerName = promptString("name");
        String customerEmail = promptString("email");

        LeaseContract leaseContract = new LeaseContract(now.toString(), customerName, customerEmail, vehicle);

        System.out.println("\n========== LEASE SUMMARY ==========");
        System.out.printf("Date:       %s%n", leaseContract.getDate());
        System.out.printf("Customer:   %s (%s)%n", leaseContract.getCustomerName(), leaseContract.getCustomerEmail());
        System.out.printf("Vehicle:    %d %s %s%n", vehicle.getYear(), vehicle.getMake(), vehicle.getModel());
        System.out.printf("Price:      $%,.2f%n", vehicle.getPrice());

        System.out.println("\nTerms---");
        System.out.printf("%-18s $%,10.2f%n", "Lease Fee (7%):", leaseContract.getLeaseFee());
        System.out.printf("%-18s $%,10.2f%n", "Monthly Payment:", leaseContract.getMonthlyPayment());
        System.out.printf("%-18s %10s%n",     "Term:", "36 months");
        System.out.println("-----------------------------------");
        System.out.printf("%-18s $%,10.2f%n", "Total Price:", leaseContract.getTotalPrice());
        System.out.println("===================================");

        System.out.print("Do you accept(y/n)? ");
        String response = scanner.nextLine();

        if (response.equalsIgnoreCase("y")) {
            ContractDataManager.saveContract(leaseContract);
            this.dealership.removeVehicle(vehicle);
            System.out.printf("Enjoy your %s %s!%n", vehicle.getMake(), vehicle.getModel());
        } else {
            System.out.println("Hope you come again!");
        }
    }

    private void processSellVehicleRequest() {
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
                //@todo: defend against negative numbers.
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

    public void processAddVehicleRequest() {
        System.out.println("\t[UPDATE] - Add Vehicle");

        int vin, year, odometer;
        String make, model, vehicleType, color;
        BigDecimal price;

        int size = this.dealership.getAllVehicles().size();
        Vehicle lastV = this.dealership.getAllVehicles().get(size - 1);

        vin = lastV.getVin() + 1;
        year = promptInt("Year");
        make = promptString("Make");
        model = promptString("Model");
        vehicleType = promptString("Vehicle type");
        color = promptString("Color");
        odometer = promptInt("Odometer");
        price = promptBigD("Price");

        Vehicle newV = new Vehicle(vin, year, make, model, vehicleType, color, odometer, price);
        this.dealership.addVehicle(newV);
        DealershipFileManager.saveDealership(this.dealership);
    }

    public void processRemoveVehicleRequest() {
        System.out.println("\t[UPDATE] - Remove Vehicle");

        int vin;

        displayVehicles(this.dealership.getAllVehicles());
        vin = promptInt("vin");

        boolean success = false;
        for (Vehicle v : this.dealership.getAllVehicles()) {
            if (v.getVin() == vin) {
                System.out.printf("\tAre you sure you'd like to remove this %s %s %s(y/n)?",
                        v.getYear(),
                        v.getMake(),
                        v.getModel());

                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("y")) {
                    this.dealership.removeVehicle(v);
                    success = true;
                    DealershipFileManager.saveDealership(this.dealership);
                    break;
                } else {
                    System.out.println("\t=* Not removed *=");
                }
            }
        }

        if (!success) {
            System.out.println("\t=* Vehicle doesn't exist *=");
        }
    }

    //region Scanner helpers
    private int promptInt(String vehicleField) {
        int number = 0;
        boolean valid = false;
        do {
            try {
                System.out.printf("\tENTER %s: ", vehicleField);
                number = Integer.parseInt(scanner.nextLine().trim());

                if (number <= 0) {
                    System.out.println("\tInvalid number: Cannot be negative");
                } else {
                    valid = true;
                }

            } catch (NumberFormatException nfe) {
                System.out.println("\tInvalid number: " + nfe.getMessage());
            }
        } while (!valid);

        return number;
    }

    private String promptString(String vehicleField) {
        String query;
        String[] vehicleTypes = {"car", "truck", "SUV", "van"};
        boolean valid = false;

        do {
            System.out.printf("\tENTER %s: ", vehicleField);
            query = scanner.nextLine().trim();

            //todo: fix
            if (vehicleField.equals("Vehicle type")) {
                for (String type : vehicleTypes) {
                    if (type.contains(query)) {
                        query = type;
                    } else {
                        query = "";
                    }
                }
            }

            if (!query.isBlank()) {
                valid = true;
            } else {
                System.out.println("\tInvalid input: Not a Vehicle type");
            }
        } while (!valid);

        return query;
    }

    private BigDecimal promptBigD(String vehicleField) {
        BigDecimal price = BigDecimal.ZERO;
        boolean valid = false;

        do {
            try {
                System.out.printf("\tENTER %s: $", vehicleField);
                price = new BigDecimal(scanner.nextLine().trim());

                if (price.signum() <= 0) {
                    System.out.println("\tInvalid number: Cannot be <= 0");
                } else {
                    valid = true;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("\tInvalid number: " + nfe.getMessage());
            }
        } while (!valid);

        return price;
    }
    //endregion
}