package com.senacor.intermission.carsharing.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.senacor.intermission.carsharing.models.Car;

import java.util.HashMap;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = CarRentalController.class)
@AutoConfigureMockMvc
public class CarRentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarRentalController carRentalController;

    @Test
    public void testGetCarPool() throws Exception {
        HashMap<String, Car> carPool = new HashMap<>();
        carPool.put("1", new Car("Audi", "red", "M-AB 123"));
        carPool.put("2", new Car("BMW", "blue", "M-CD 456"));
        carPool.put("3", new Car("Mercedes", "green", "M-EF 789"));
        when(carRentalController.getCarPool()).thenReturn(carPool);
        mockMvc.perform(MockMvcRequestBuilders.get("/rental/pool")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.1.make").value("Audi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.2.make").value("BMW"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.3.make").value("Mercedes"));
    }

    @Test
    public void testRentNextCar() throws Exception {
        Car car = new Car("Audi", "red", "M-AB 123");
        when(carRentalController.rentNextCar()).thenReturn(car);
        mockMvc.perform(MockMvcRequestBuilders.get("/rental/rent")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.make").value("Audi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value("red"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.licensePlate").value("M-AB 123"));
    }

    @Test
    public void testReturnCar() throws Exception {
        Car car = new Car("Audi", "red", "M-AB 123");
        when(carRentalController.calculateBill(car, 0L, true, true)).thenReturn(100);
        mockMvc.perform(MockMvcRequestBuilders.get("/rental/return")
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .param("make", "Audi")
                .param("color", "red")
                .param("licensePlate", "M-AB 123"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("100"));
    }

}
