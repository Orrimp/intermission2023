package com.senacor.intermission.carsharing.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.senacor.intermission.carsharing.models.Car;

@Service
public class CarRentalService {

    private HashMap<String, Car> carPool = new HashMap<>();

    Logger logger = LoggerFactory.getLogger(CarRentalService.class);

    private int poolCounter = 0;

    /**
     * 
     */
    public CarRentalService() {
        carPool.put("0", new Car("Porsche", "911", "red", "M-AB-123", "gasoline"));
        carPool.put("1", new Car("BMW", "i3", "white", "M-AB-123", "electric"));
        carPool.put("2", new Car("Audi", "A3", "black", "M-CD-456", "diesel"));
        carPool.put("3", new Car("Mercedes", "C-Class", "silver", "M-EF-789", "gasoline"));
        carPool.put("4", new Car("VW", "Golf", "blue", "M-GH-012", "gasoline"));
        carPool.put("5", new Car("Tesla", "Model 3", "red", "M-IJ-345", "electric"));
    }

    /** 
     * 
     * @param carPool
     * @return
     */
    public HashMap<String, Car> getCarPool(){
        return carPool;
    }

    public int calculateBill(Car car, boolean carOnTime, boolean carcheckOk) {
        logger.info("Calculating bill with carOnTime: " + carOnTime + " and carcheckOk: " + carcheckOk + " for car: " + car.toString());

        int billValue = 0;
        switch (getBillType(carOnTime, carcheckOk)) {
            case ON_TIME_AND_CHECK_OK:
                billValue += 12000;
                break;
            case ON_TIME_AND_CHECK_NOT_OK:
                billValue += 50000;
                break;
            case NOT_ON_TIME_AND_CHECK_OK:
                billValue += 1050;
                break;
            case NOT_ON_TIME_AND_CHECK_NOT_OK:
                billValue = 100000;
                break;
            default:
                billValue = -1;
                break;
        }
        return billValue;
    }

    //Check car for damages and fuel level and return bill additional 10000 if car is not ok 
    public int checkCar(Car car) {
        logger.info("Checking car: " + car.toString());
        int billValue = 0;
        if (carCheckOk(car)) {
            billValue += 0;
        } else {
            billValue += 10000;
        }
        return billValue;
    }

    private BillType getBillType(boolean carOnTime, boolean carcheckOk) {
        for (BillType billType : BillType.values()) {
            if (billType.isCarOnTime() == carOnTime && billType.isCarCheckOk() == carcheckOk) {
                return billType;
            }
        }
        return BillType.INVALID;
    }

    private enum BillType {
        ON_TIME_AND_CHECK_OK(true, true),
        ON_TIME_AND_CHECK_NOT_OK(true, false),
        NOT_ON_TIME_AND_CHECK_OK(false, true),
        NOT_ON_TIME_AND_CHECK_NOT_OK(false, false),
        INVALID(false, false);

        private final boolean carOnTime;
        private final boolean carCheckOk;

        BillType(boolean carOnTime, boolean carCheckOk) {
            this.carOnTime = carOnTime;
            this.carCheckOk = carCheckOk;
        }

        public boolean isCarOnTime() {
            return carOnTime;
        }

        public boolean isCarCheckOk() {
            return carCheckOk;
        }
    }

    public boolean carCheckOk(Car car){
        return true;
    }

    public boolean carOnTime(Car car){
        return true;
    }

    public Car getNextCarFromPool(){
        poolCounter = poolCounter % carPool.size();
        Car c =  carPool.get("" + poolCounter);
        if (c.isAvaible()){
            c.setAvaible(false);
            return c;
        } else {
            poolCounter += 1;
            return getNextCarFromPool();
        }
    }

    public void addCars(HashMap<String, Car> carPool) {
        this.carPool.putAll(carPool);
    }

    public void rentCar(Car car) {
        car.setAvaible(false);
    }

    public void returnCar(Car car) {
        car.setAvaible(true);
    }

    //Add method to allow customers to buy the rented car. Remove the car from the car pool and return the car to the customer
    public void sellCar(Car car) {
        car.setAvaible(true);
        carPool.remove(car);
    }


}
