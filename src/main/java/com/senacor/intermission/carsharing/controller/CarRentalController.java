package com.senacor.intermission.carsharing.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.senacor.intermission.carsharing.models.Car;
import com.senacor.intermission.carsharing.service.CarRentalService;

import io.micrometer.common.lang.NonNull;

@Controller
@RequestMapping("/rental")
public class CarRentalController {

    Logger logger = LoggerFactory.getLogger(CarRentalController.class);


    @Autowired
    CarRentalService carRentalService;

    @GetMapping(value = "/pool", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public HashMap<String, Car> getCarPool(){
        logger.info("Car pool requested");
        return carRentalService.getCarPool();
    }

    @GetMapping(value = "/rent", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Car rentNextCar(){
        Car car = carRentalService.getNextCarFromPool();

        logger.info("Car rented: " + car.toString());
        return car;
    }

    @GetMapping(value = "/return", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Integer> returnCar(@RequestBody Car car){

        if (car == null ) {
            return ResponseEntity.badRequest().build();
        }

        boolean carOnTime = carRentalService.carOnTime(car);
        boolean carcheckOk = carRentalService.carCheckOk(car);
        long timestamp =  System.currentTimeMillis();
        int billValue = carRentalService.calculateBill(car, timestamp, carOnTime, carcheckOk);
        
        logger.info("Car returned: " + car.toString() + " with bill: " + billValue);

        return ResponseEntity.ok(billValue);
    }
}
