package com.senacor.intermission.carsharing.models;

public class Customer {
    
    public Customer(String name, String email){
        this.name = name;
        this.email = email;
    }

    private String name;
    private String email;

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    @Override
    public String toString(){
        return "Customer{" + "name='" + name + '\'' + ", email='" + email + '\'' + '}';
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(!(o instanceof Customer))
            return false;

        Customer customer = (Customer) o;

        if(name != null ? !name.equals(customer.name) : customer.name != null)
            return false;
        return email != null ? email.equals(customer.email) : customer.email == null;
    }

    @Override
    public int hashCode(){
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
