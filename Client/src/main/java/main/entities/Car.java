package main.entities;

import main.enums.BodyType;
import main.enums.PetrolType;

public class Car {
    private int id;
    private String brand;
    private double cost;
    private PetrolType petrolType;
    private BodyType bodyType;
    private String imagePath;

    Car() {}

    public Car(String brand, double cost, PetrolType petrolType, BodyType bodyType, String imagePath) {
        this.brand = brand;
        this.cost = cost;
        this.petrolType = petrolType;
        this.bodyType = bodyType;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public PetrolType getPetrolType() {
        return petrolType;
    }

    public void setPetrolType(PetrolType petrolType) {
        this.petrolType = petrolType;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
