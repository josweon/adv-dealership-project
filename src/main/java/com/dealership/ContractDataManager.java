package com.dealership;

public class ContractDataManager {

    public static String saveContract(Contract contract){
        String output = "";
        String customerFields;
        String vehicleFields;
        String contractSpecificFields;
        String contractType;

        if (contract instanceof SalesContract) {
            contractType = "SALE";
        } else {
            contractType = "LEASE";
        }

        customerFields = customerInfo(contract);
        vehicleFields = vehicleString(contract.getVehicleSold());
        contractSpecificFields = contractSpecificInfo(contract);
        output = String.format("%s|%s|%s|%s",
                contractType,
                customerFields,
                vehicleFields,
                contractSpecificFields);
        return output;
    }

    private static String contractSpecificInfo(Contract contract){
        String contractSpecificInfo = "";
        if (contract instanceof SalesContract) {
            SalesContract salesContract = (SalesContract) contract;

            String salesTax = salesContract.getSalesTaxAmount().toString();
            String recordingFee = salesContract.getRecordingFee().toString();
            String processingFee = salesContract.getProcessingFee().toString();
            String totalPrice = salesContract.getTotalPrice().toString();
            String financeOption = (salesContract.getToFinance()) ? "YES" : "NO";
            String monthlyPayment = salesContract.getMonthlyPayment().toString();

            contractSpecificInfo = String.format("%s|%s|%s|%s|%s|%s",
                    salesTax,
                    recordingFee,
                    processingFee,
                    totalPrice,
                    financeOption,
                    monthlyPayment);

        } else {
            LeaseContract leaseContract = (LeaseContract) contract;
            String expectedEndingValue = leaseContract.getEndingValue().toString();
            String leaseFee = leaseContract.getLeaseFee().toString();
            String totalPrice = leaseContract.getTotalPrice().toString();
            String monthlyPayment = leaseContract.getMonthlyPayment().toString();

            contractSpecificInfo = String.format("%s|%s|%s|%s",
                    expectedEndingValue,
                    leaseFee,
                    totalPrice,
                    monthlyPayment);
        }
        return contractSpecificInfo;
    }

    private static String customerInfo(Contract contract){
        String date = contract.getDate();
        String customerName = contract.getCustomerName();
        String customerEmail = contract.getCustomerEmail();
        String customerInfo = String.format("%s|%s|%s", date, customerName, customerEmail);

        return customerInfo;
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
