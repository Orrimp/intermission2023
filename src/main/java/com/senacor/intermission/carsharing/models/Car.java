package com.senacor.intermission.carsharing.models;

public class Car {
    
    public Car(String brand, String model, String color, String licensePlate, String fuelType){
        this.avaible = true;
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
        this.fuelType = fuelType;
        this.color = color;
    }

    public Car() {
        this.avaible = false;
    }

    private boolean avaible;
    private String brand;
    private String model;
    private String licensePlate;
    private String fuelType;
    private String color;
    private long timestampStartRent;
    private long timestampEndRent;

    @Override
    public String toString(){
        return "{" + "name='" + brand + " " + + '\'' + ", color='" + color + '\'' + ", licensePlate='" + licensePlate + '\'' + ", fueltype='" + fuelType +  '\'' + "}";
    }

    public boolean isAvaible(){
        return avaible;
    }

    public void setAvaible(boolean avaible){
        this.avaible = avaible;
    }

    public void setRented(){
        this.avaible = false;
        this.timestampStartRent = System.currentTimeMillis();
    }

    public void setReturned(){
        this.avaible = true;
        this.timestampEndRent = System.currentTimeMillis();
    }

    public long getRentedTimeInMillseconds(){
        return this.timestampEndRent - this.timestampStartRent;
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(!(o instanceof Car))
            return false;

        Car car = (Car) o;

        if(brand != null ? !brand.equals(car.brand) : car.brand != null)
            return false;
        if(model != null ? !model.equals(car.model) : car.model != null)
            return false;
        return licensePlate != null ? licensePlate.equals(car.licensePlate) : car.licensePlate == null;
    }

    @Override
    public int hashCode(){
        int result = brand != null ? brand.hashCode() : 0;
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (licensePlate != null ? licensePlate.hashCode() : 0);
        return result;
    }

}
