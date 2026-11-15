package com.pluralsight;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;

public class DealershipFileManager {

    public static Dealership getDealership() {
        String filePath = "src/main/resources/inventory.csv";
        Dealership newDealer = null;

        try {
            BufferedReader bufreader = new BufferedReader(new FileReader(filePath));
            String[] header = bufreader.readLine().split("\\|");
            newDealer = new Dealership(header[0], header[1], header[2]);

            String input;
            while((input = bufreader.readLine()) != null) {
                String[] tokens = input.split("\\|");

                int vin = Integer.parseInt(tokens[0]);
                int year = Integer.parseInt(tokens[1]);
                String make = tokens[2];
                String model = tokens[3];
                String vehicleType = tokens[4];
                String color = tokens[5];
                int odometer = Integer.parseInt(tokens[6]);
                BigDecimal price = new BigDecimal(tokens[7]);

                Vehicle v = new Vehicle(vin, year, make, model, vehicleType, color, odometer, price);
                newDealer.addVehicle(v);
            }

            bufreader.close();

        } catch (IOException ioe) {
            System.out.println("Error reading file:" + ioe.getMessage());
        }

        //done: Change to Dealership
        return newDealer;
    }

    public static void saveDealership(Dealership dealership){
        ArrayList<Vehicle> saveInventory = dealership.getAllVehicles();
        String filePath = "src/main/resources/inventory.csv";

        try {
            BufferedWriter bufWriter = new BufferedWriter(new FileWriter(filePath));

            String header = String.format("%s|%s|%s%n",
                    dealership.getName(),
                    dealership.getAddress(),
                    dealership.getPhone());

            bufWriter.write(header);

            for (int i = 0; i < saveInventory.size(); i++) {
                Vehicle v = saveInventory.get(i);
                String vehicle = String.format("%s|%s|%s|%s|%s|%s|%s|%s%n",
                        v.getVin(),
                        v.getYear(),
                        v.getMake(),
                        v.getModel(),
                        v.getVehicleType(),
                        v.getColor(),
                        v.getOdometer(),
                        v.getPrice());
                bufWriter.write(vehicle);
            }
            bufWriter.close();

        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        System.out.println("\t=* Saved! *=");
    }
}
