package com.senacor.intermission.carsharing.models;

public class Car {
    
    public Car(String name, String color, String licensePlate){
        this.name = name;
        this.color = color;
        this.licensePlate = licensePlate;
    }

    public Car() {
    }

    private String name;
    private String color;
    private String licensePlate;

    public String getName(){
        return name;
    }

    public String getColor(){
        return color;
    }

    public String getLicensePlate(){
        return licensePlate;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setColor(String color){
        this.color = color;
    }

    public void setLicensePlate(String licensePlate){
        this.licensePlate = licensePlate;
    }

    @Override
    public String toString(){
        return "{" + "name='" + name + '\'' + ", color='" + color + '\'' + ", licensePlate='" + licensePlate + '\'' + '}';
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(!(o instanceof Car))
            return false;

        Car car = (Car) o;

        if(name != null ? !name.equals(car.name) : car.name != null)
            return false;
        if(color != null ? !color.equals(car.color) : car.color != null)
            return false;
        return licensePlate != null ? licensePlate.equals(car.licensePlate) : car.licensePlate == null;
    }

    @Override
    public int hashCode(){
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (licensePlate != null ? licensePlate.hashCode() : 0);
        return result;
    }

}
