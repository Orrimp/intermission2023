package com.senacor.intermission.carsharing.controller;

import java.util.HashMap;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.senacor.intermission.carsharing.models.Car;

@Controller
@RequestMapping("/rental")
public class CarRentalController {

    private HashMap<String, Car> carPool = new HashMap<>();

    public CarRentalController(){
        carPool.put("1", new Car("Audi", "red", "M-AB 123"));
        carPool.put("2", new Car("BMW", "blue", "M-CD 456"));
        carPool.put("3", new Car("Mercedes", "green", "M-EF 789"));
        carPool.put("4", new Car("VW", "yellow", "M-GH 012"));
        
    }

    @GetMapping(value = "/pool", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public HashMap<String, Car> getCarPool(){
        return carPool;
    }

    @GetMapping(value = "/rent", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Car rentNextCar(){
        Car car = getNextCarFromPool();
        return car;
    }

    @GetMapping(value = "/return", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public int returnCar(Car car){
        boolean carOnTime = carOnTime(car);
        boolean carcheckOk = carCheckOk(car);
        long timestamp =  System.currentTimeMillis();
        int billValue = calculateBill(car, timestamp, carOnTime, carcheckOk);

        return billValue;
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

    private boolean carCheckOk(Car car){
        return true;
    }

    private boolean carOnTime(Car car){
        return true;
    }

    private Car getNextCarFromPool(){
        return carPool.get("1");
    }
}
