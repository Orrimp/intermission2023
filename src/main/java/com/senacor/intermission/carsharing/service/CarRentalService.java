package com.senacor.intermission.carsharing.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.senacor.intermission.carsharing.models.Car;

@Service
public class CarRentalService {

    private HashMap<String, Car> carPool = new HashMap<>();

    private int poolCounter = 0;

    public CarRentalService() {
        carPool.put("0", new Car("Porsche", "911", "red", "M-AB-123", "gasoline"));
        carPool.put("1", new Car("BMW", "i3", "white", "M-AB-123", "electric"));
        carPool.put("2", new Car("Audi", "A3", "black", "M-CD-456", "diesel"));
        carPool.put("3", new Car("Mercedes", "C-Class", "silver", "M-EF-789", "gasoline"));
        carPool.put("4", new Car("VW", "Golf", "blue", "M-GH-012", "gasoline"));
        carPool.put("5", new Car("Tesla", "Model 3", "red", "M-IJ-345", "electric"));
    }

    public HashMap<String, Car> getCarPool(){
        return carPool;
    }

    public int calculateBill(Car car, boolean carOnTime, boolean carcheckOk){
        int billValue = 0;
        if(carOnTime && carcheckOk){
            billValue += 12000;
        } else if(carOnTime && !carcheckOk){
            billValue += 50000;
        } else if(!carOnTime && carcheckOk){
            billValue += 1050;
        } else if(!carOnTime && !carcheckOk){
            billValue = 100000;
        }
        return billValue;
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
}
