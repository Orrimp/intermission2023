package com.senacor.intermission.carsharing.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
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

//@SpringBootTest(classes = CarRentalController.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WebMvcTest(CarRentalController.class)
//@AutoConfigureMockMvc
public class CarRentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarRentalService carRentalService;


    @BeforeEach
    private void setup() {
        HashMap<String, Car> carPool = new HashMap<>();

        carPool.put("1", new Car("Audi", "red", "M-AB 123"));
        carPool.put("2", new Car("BMW", "blue", "M-CD 456"));
        carPool.put("3", new Car("Mercedes", "green", "M-EF 789"));
        carPool.put("4", new Car("VW", "yellow", "M-GH 012"));

        carRentalService.addCars(carPool);
    }

    // @Test
    // public void testGetCarPool() throws Exception {
    //     HashMap<String, Car> carPool = new HashMap<>();
    //     carPool.put("1", new Car("Audi", "red", "M-AB 123"));
    //     carPool.put("2", new Car("BMW", "blue", "M-CD 456"));
    //     carPool.put("3", new Car("Mercedes", "green", "M-EF 789"));
    //     when(carRentalService.getCarPool()).thenReturn(carPool);
    //     mockMvc.perform(MockMvcRequestBuilders.get("/rental/pool")
    //             .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
    //             .andDo(System.out::println)
    //             .andExpect(MockMvcResultMatchers.status().isOk())
    //             .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value(carPool.get("1").getName()))
    //             .andExpect(MockMvcResultMatchers.jsonPath("$.[2].name").value(carPool.get("2").getName()))
    //             .andExpect(MockMvcResultMatchers.jsonPath("$.[3].name").value(carPool.get("3").getName()));
    // }

    @Test
    public void testRentNextCar() throws Exception {
        Car car = new Car("Audi", "red", "M-AB 123");
        when(carRentalService.getNextCarFromPool()).thenReturn(car);
        mockMvc.perform(MockMvcRequestBuilders.get("/rental/rent")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"name\":\"Audi\",\"color\":\"red\",\"licensePlate\":\"M-AB 123\"}"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Audi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value("red"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.licensePlate").value("M-AB 123"));
    }

    // @Test
    // public void testReturnCar() throws Exception {
    //     Car car = new Car("Audi", "red", "M-AB 123");
    //     String carJson = car.toString();
    //     when(carRentalService.calculateBill(car, 0L, true, true)).thenReturn(100);
    //     mockMvc.perform(MockMvcRequestBuilders.get("/rental/return")
    //             .content(carJson)
    //             .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
    //             .andDo(System.out::println)
    //             .andExpect(MockMvcResultMatchers.status().isOk())
    //             .andExpect(MockMvcResultMatchers.content().string("100"));
    // }

    // @Test
    // public void testReturnCarWithMissingParameters() throws Exception {
    //     mockMvc.perform(MockMvcRequestBuilders.get("/rental/return")
    //             .param("make", "Audi")
    //             .param("color", "red")
    //             .param("licensePlate", "M-AB 123")
    //             .param("rentalTime", "0")
    //             .param("damaged", "true")
    //             .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
    //             .andDo(System.out::println)
    //             .andExpect(MockMvcResultMatchers.status().isBadRequest());
    // }
}
