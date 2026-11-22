package com.dealership;

public class ContractDataManager {
    // Contract order
    // CONTRACT_TYPE|DATE|CUSTOMER_NAME|CUSTOMER_EMAIL|VIN|YEAR|MAKE|MODEL|VEHICLE_TYPE|COLOR|ODOMETER|VEHICLE_PRICE|
    // ...
    // For sales: SALES_TAX|RECORDING_FEE|PROCESSING_FEE|TOTAL_PRICE|FINANCE_OPTION|MONTHLY_PAYMENT
    // For lease: EXPECTED_ENDING_VALUE|LEASE_FEE|TOTAL_PRICE|MONTHLY_PAYMENT

    public static String saveContract(Contract contract){
        String output = "";
        String contractType;
        if (contract instanceof SalesContract) {
            contractType = "SALE";

            String date = contract.getDate();
            String customerName = contract.getCustomerName();
            String customerEmail = contract.getCustomerEmail();
            String customerInfo = String.format("%s|%s|%s", date, customerName, customerEmail);

            Vehicle vehicle = contract.getVehicleSold();
            String vehicleString = vehicleString(vehicle);

            SalesContract salesContract = (SalesContract) contract;
            // For sales: SALES_TAX|RECORDING_FEE|PROCESSING_FEE|TOTAL_PRICE|FINANCE_OPTION|MONTHLY_PAYMENT
            String salesTax = salesContract.getSalesTaxAmount().toString();
            String recordingFee = salesContract.getRecordingFee().toString();
            String processingFee = salesContract.getProcessingFee().toString();
            String totalPrice = salesContract.getTotalPrice().toString();
            String financeOption = (salesContract.getToFinance()) ? "YES" : "NO";
            String monthlyPayment = salesContract.getMonthlyPayment().toString();

            String salesSpecificFields = String.format("%s|%s|%s|%s|%s|%s",
                    salesTax,
                    recordingFee,
                    processingFee,
                    totalPrice,
                    financeOption,
                    monthlyPayment);

            String fullSalesContract = String.format("%s|%s|%s|%s", contractType, customerInfo, vehicleString, salesSpecificFields);
            output = fullSalesContract;

        } else {
            contractType = "LEASE";

            String date = contract.getDate();
            String customerName = contract.getCustomerName();
            String customerEmail = contract.getCustomerEmail();
            String customerInfo = String.format("%s|%s|%s", date, customerName, customerEmail);

            Vehicle vehicle = contract.getVehicleSold();
            String vehicleString = vehicleString(vehicle);

            LeaseContract leaseContract = (LeaseContract) contract;
            // EXPECTED_ENDING_VALUE|LEASE_FEE|TOTAL_PRICE|MONTHLY_PAYMENT
            String expectedEndingValue = leaseContract.getEndingValue().toString();
            String leaseFee = leaseContract.getLeaseFee().toString();
            String totalPrice = leaseContract.getTotalPrice().toString();
            String monthlyPayment = leaseContract.getMonthlyPayment().toString();

            String leaseSpecificFields = String.format("%s|%s|%s|%s",
                    expectedEndingValue,
                    leaseFee,
                    totalPrice,
                    monthlyPayment);

            String fullLeaseContract = String.format("%s|%s|%s|%s", contractType, customerInfo, vehicleString, leaseSpecificFields);
            output = fullLeaseContract;
        }
        return output;
    }

    private static String vehicleString(Vehicle vehicle) {
        String vin = Integer.toString(vehicle.getVin());
        String year = Integer.toString(vehicle.getYear());
        String make = vehicle.getMake();
        String model = vehicle.getModel();
        String vehicleType = vehicle.getVehicleType();
        String color = vehicle.getColor();
        String odometer = Integer.toString(vehicle.getOdometer());
        String vehiclePrice = vehicle.getPrice().toString();

        String result = String.format("%s|%s|%s|%s|%s|%s|%s|%s",
                vin,
                year,
                make,
                model,
                vehicleType,
                color,
                odometer,
                vehiclePrice);

        return result;
    }
}
