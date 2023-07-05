package com.senacor.intermission.carsharing.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.senacor.intermission.carsharing.models.Car;
import com.senacor.intermission.carsharing.service.CarRentalService;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.mockito.Mockito.when;

import java.util.HashMap;

@WebMvcTest(CarRentalController.class)
public class CarRentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarRentalService carRentalService;


    @BeforeEach
    private void setup() {
        HashMap<String, Car> carPool = new HashMap<>();

        carPool.put("0", new Car("Tesla", "Model 3", "white" , "M-AB 123", "electric"));
        carPool.put("1", new Car("Audi", "A4", "red", "M-CD 456", "diesel"));
        carPool.put("2", new Car("BMW", "X5", "blue", "M-EF 789", "diesel"));
        carPool.put("3", new Car("Mercedes", "C-Class", "green", "M-GH 012", "diesel"));
        carPool.put("4", new Car("VW", "Golf", "black", "M-IJ 345", "diesel"));
        carPool.put("5", new Car("Porsche", "911", "yellow", "M-KL 678", "diesel"));

        

        carRentalService.addCars(carPool);
    }

    @Test
    public void testGetCarPool() throws Exception {
        HashMap<String, Car> carPool = new HashMap<>();

        carPool.put("1", new Car("Audi", "A4", "red", "M-CD 456", "diesel"));
        carPool.put("2", new Car("BMW", "X5", "blue", "M-EF 789", "diesel"));
        carPool.put("3", new Car("Mercedes", "C-Class", "green", "M-GH 012", "diesel"));


        when(carRentalService.getCarPool()).thenReturn(carPool);
        mockMvc.perform(MockMvcRequestBuilders.get("/rental/pool")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andDo(System.out::println)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.1.brand").value(carPool.get("1").getBrand()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.2.brand").value(carPool.get("2").getBrand()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.3.brand").value(carPool.get("3").getBrand()));
    }

    @Test
    public void testRentNextCar() throws Exception {
        Car car = new Car("Audi", "A4", "red", "M-CD 456", "diesel");
        when(carRentalService.getNextCarFromPool()).thenReturn(car);
        mockMvc.perform(MockMvcRequestBuilders.get("/rental/rent")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.brand").value(car.getBrand()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.model").value(car.getModel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value(car.getColor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.licensePlate").value(car.getLicensePlate()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fuelType").value(car.getFuelType()));
    }

    @Test
    public void testReturnCar() throws Exception {
        Car car = new Car("Audi", "A4", "red", "M-CD 456", "diesel");
        String carJson = car.toJSON();
        when(carRentalService.carOnTime(car)).thenReturn(true);
        when(carRentalService.carCheckOk(car)).thenReturn(true);
        when(carRentalService.calculateBill(car, true, true)).thenReturn(100);
        
        mockMvc.perform(MockMvcRequestBuilders.post("/rental/return")
                .content(carJson)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.ALL))
                .andDo(System.out::println)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("100"));
    }

    // @Test
    // public void testReturnCarWithMissingParameters() throws Exception {
    //     mockMvc.perform(MockMvcRequestBuilders.get("/rental/return")
    //             .content("")
    //             .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
    //             .andDo(System.out::println)
    //             .andExpect(MockMvcResultMatchers.status().isBadRequest());
    // }

    // @Test
    // public void testReturnCarWithInvalidParameters() throws Exception {
    //     mockMvc.perform(MockMvcRequestBuilders.get("/rental/return")
    //             .content("invalid")
    //             .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
    //             .andDo(System.out::println)
    //             .andExpect(MockMvcResultMatchers.status().isBadRequest());
    // }

    // @Test
    // public void testReturnCarWithInvalidCar() throws Exception {
    //     Car car = new Car("Audi", "A4", "red", "M-CD 456", "diesel");
    //     String carJson = car.toString();
    //     when(carRentalService.calculateBill(car, true, true)).thenReturn(100);
    //     mockMvc.perform(MockMvcRequestBuilders.get("/rental/return")
    //             .content(carJson)
    //             .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
    //             .andDo(System.out::println)
    //             .andExpect(MockMvcResultMatchers.status().isOk())
    //             .andExpect(MockMvcResultMatchers.content().string("100"));
    // }

    // @Test
    // public void testReturnCarWithInvalidTimestamp() throws Exception {
    //     Car car =  new Car("Audi", "A4", "red", "M-CD 456", "diesel");
    //     String carJson = car.toString();
    //     when(carRentalService.calculateBill(car, true, true)).thenReturn(100);
    //     mockMvc.perform(MockMvcRequestBuilders.get("/rental/return")
    //             .content(carJson)
    //             .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
    //             .andDo(System.out::println)
    //             .andExpect(MockMvcResultMatchers.status().isOk())
    //             .andExpect(MockMvcResultMatchers.content().string("100"));
    // }   

    // @Test
    // public void testReturnCarWithInvalidDamages() throws Exception {
    //     Car car = new Car("Audi", "A4", "red", "M-CD 456", "diesel");
    //     String carJson = car.toString();
    //     when(carRentalService.calculateBill(car, true, true)).thenReturn(100);
    //     mockMvc.perform(MockMvcRequestBuilders.get("/rental/return")
    //             .content(carJson)
    //             .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
    //             .andDo(System.out::println)
    //             .andExpect(MockMvcResultMatchers.status().isOk())
    //             .andExpect(MockMvcResultMatchers.content().string("100"));
    // }

    // @Test
    // public void testReturnCarWithInvalidFuel() throws Exception {
    //     Car car = new Car("Audi", "A4", "red", "M-CD 456", "diesel");
    //     String carJson = car.toJSON();
    //     when(carRentalService.calculateBill(car, true, true)).thenReturn(100);
    //     mockMvc.perform(MockMvcRequestBuilders.get("/rental/return")
    //             .content(carJson)
    //             .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
    //             .andDo(System.out::println)
    //             .andExpect(MockMvcResultMatchers.status().isOk())
    //             .andExpect(MockMvcResultMatchers.content().string("100"));
    // }

    // @Test
    // public void testReturnCarWithInvalidCarAndTimestamp() throws Exception {
    //     Car car = new Car("Tesla", "Model 3", "white" , "M-AB 123", "electric");
    //     String carJson = car.toJSON();
    //     when(carRentalService.calculateBill(car, true, true)).thenReturn(100);
    //     mockMvc.perform(MockMvcRequestBuilders.get("/rental/return")
    //             .content(carJson)
    //             .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
    //             .andDo(System.out::println)
    //             .andExpect(MockMvcResultMatchers.status().isOk())
    //             .andExpect(MockMvcResultMatchers.content().string("100"));
    // }

    // @Test
    // public void testRentCarNotAvaiable() throws Exception {
    //     when(carRentalService.getNextCarFromPool()).thenReturn(null);
    //     mockMvc.perform(MockMvcRequestBuilders.get("/rental/rent")
    //             .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
    //             .andDo(print())
    //             .andExpect(MockMvcResultMatchers.status().isNotFound());
    // }

    // @Test
    // public void testReturnCarNotAvaiable() throws Exception {
    //     Car car = new Car("Audi", "A4", "red", "M-AB 123", "gasoline");
    //     String carJson = car.toJSON();
    //     when(carRentalService.calculateBill(car, true, true)).thenReturn(100);
    //     mockMvc.perform(MockMvcRequestBuilders.get("/rental/return")
    //             .content(carJson)
    //             .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
    //             .andDo(System.out::println)
    //             .andExpect(MockMvcResultMatchers.status().isNotFound());
    // }

    // @Test
    // public void testReturnCarWithInvalidBill() throws Exception {
    //     Car car = new Car("Audi", "A4", "red", "M-AB 123", "gasoline");
    //     String carJson = car.toJSON();
    //     when(carRentalService.calculateBill(car, true, true)).thenReturn(-1);
    //     mockMvc.perform(MockMvcRequestBuilders.get("/rental/return")
    //             .content(carJson)
    //             .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
    //             .andDo(System.out::println)
    //             .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    // }    
}
