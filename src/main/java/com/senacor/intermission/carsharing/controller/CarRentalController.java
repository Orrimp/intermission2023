package com.senacor.intermission.carsharing.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.senacor.intermission.carsharing.models.Car;
import com.senacor.intermission.carsharing.service.CarRentalService;

@Controller
@RequestMapping("/rental")
public class CarRentalController {

    @Autowired
    CarRentalService carRentalService;

    @GetMapping(value = "/pool", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public HashMap<String, Car> getCarPool(){
        return carRentalService.getCarPool();
    }

    @GetMapping(value = "/rent", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Car rentNextCar(){
        Car car = carRentalService.getNextCarFromPool();
        return car;
    }

    @GetMapping(value = "/return", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public int returnCar(Car car){
        boolean carOnTime = carRentalService.carOnTime(car);
        boolean carcheckOk = carRentalService.carCheckOk(car);
        long timestamp =  System.currentTimeMillis();
        int billValue = carRentalService.calculateBill(car, timestamp, carOnTime, carcheckOk);

        return billValue;
    }

    
}
