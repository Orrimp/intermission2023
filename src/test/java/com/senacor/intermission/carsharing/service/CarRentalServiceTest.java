package com.senacor.intermission.carsharing.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.senacor.intermission.carsharing.models.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CarRentalServiceTest {
    
    private CarRentalService carRentalService;


    @BeforeEach
    public void setUp() {
        carRentalService = new CarRentalService();
    }

    @Test
    public void testGetCarPool() {
        int carsInPool = carRentalService.getCarPool().size();
        assertEquals(6, carsInPool);
    }

    @Test
    public void testCarAvailable(){
        boolean carAvailable = carRentalService.getNextCarFromPool().isAvaible();
        assertEquals(false, carAvailable);
    }

    @Test
    public void testNextCarNotNull(){
        Car car = carRentalService.getNextCarFromPool();
        assertEquals(true, car != null);
    }

    @Test
    public void testCalculateBillReturns1200(){
        Car car = new Car("Tesla", "Model 3", "red", "M-IJ-345", "electric");
        int bill = carRentalService.calculateBill(car, true, true);
        assertEquals(12000, bill);
    }

    @Test
    public void testCalculateBillReturns50000(){
        Car car = new Car("Tesla", "Model 3", "red", "M-IJ-345", "electric");
        int bill = carRentalService.calculateBill(car, true, false);
        assertEquals(50000, bill);
    }

    @Test
    public void testCalculateBillReturns1050(){
        Car car = new Car("Tesla", "Model 3", "red", "M-IJ-345", "electric");
        int bill = carRentalService.calculateBill(car, false, true);
        assertEquals(1050, bill);
    }

    @Test
    public void testCalculateBillReturns100000(){
        Car car = new Car("Tesla", "Model 3", "red", "M-IJ-345", "electric");
        int bill = carRentalService.calculateBill(car, false, false);
        assertEquals(100000, bill);
    }
}
