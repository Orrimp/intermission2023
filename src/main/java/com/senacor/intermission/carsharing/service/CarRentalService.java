package com.senacor.intermission.carsharing.service;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.senacor.intermission.carsharing.models.Car;

@Service
public class CarRentalService {

    private HashMap<String, Car> carPool = new HashMap<>();
    private HashMap<String, Car> rentedCars = new HashMap<>();

    private int poolCounter = 0;

    public CarRentalService() {
        carPool.put("1", new Car("Audi", "red", "M-AB 123"));
        carPool.put("2", new Car("BMW", "blue", "M-CD 456"));
        carPool.put("3", new Car("Mercedes", "green", "M-EF 789"));
        carPool.put("4", new Car("VW", "yellow", "M-GH 012"));
    }

    public HashMap<String, Car> getCarPool(){
        return carPool;
    }

    public int calculateBill(Car car, long timestamp, boolean carOnTime, boolean carcheckOk){
        int billValue = 0;
        if(carOnTime && carcheckOk){
            billValue = 100;
        } else if(carOnTime && !carcheckOk){
            billValue = 50;
        } else if(!carOnTime && carcheckOk){
            billValue = 50;
        } else if(!carOnTime && !carcheckOk){
            billValue = 0;
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
        poolCounter += 1;
        poolCounter = poolCounter % 4;
        return carPool.get("" + poolCounter);
    }

    public void addCars(HashMap<String, Car> carPool) {
        this.carPool.putAll(carPool);
    }
    
}
