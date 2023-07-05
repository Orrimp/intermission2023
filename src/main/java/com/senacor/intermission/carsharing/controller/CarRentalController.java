package com.senacor.intermission.carsharing.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.senacor.intermission.carsharing.models.Car;
import com.senacor.intermission.carsharing.service.CarRentalService;

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

    /*
     * JSON example: {"avaible":false,"brand":"Porsche","model":"911","licensePlate":"M-AB-123","fuelType":"gasoline","color":"red","rentedTimeInMillseconds":0}
     */
    @GetMapping(value = "/rent", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> rentNextCar(){
        Car car = carRentalService.getNextCarFromPool();

        if (car == null){
            return ResponseEntity.notFound().build();
        } else {
            carRentalService.rentCar(car);
        }

        logger.info("Car rented: " + car.toString());
        return ResponseEntity.ok(car);
    }

    @PostMapping(value = "/return", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> returnCar(@RequestBody Car car){

        if (car == null ) {
            return ResponseEntity.badRequest().build();
        } else {
            carRentalService.returnCar(car);
        }

        boolean carOnTime = carRentalService.carOnTime(car);
        boolean carcheckOk = carRentalService.carCheckOk(car);
        int billValue = carRentalService.calculateBill(car, carOnTime, carcheckOk);
        
        if (billValue <= 0){
            return ResponseEntity.badRequest().build();
        }
        
        logger.info("Car returned: " + car.toString() + " with bill: " + billValue);

        return ResponseEntity.ok(billValue);
    }
}
