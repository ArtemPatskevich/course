package main.models.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import main.enums.entityAttributes.BodyType;
import main.enums.entityAttributes.PetrolType;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Car implements Serializable {
    private int id;
    private String brand;
    private double cost;
    private PetrolType petrolType;
    private BodyType bodyType;
    private String imagePath;

    public Car(String brand, double cost, PetrolType petrolType, BodyType bodyType, String imagePath) {
        this.brand = brand;
        this.cost = cost;
        this.petrolType = petrolType;
        this.bodyType = bodyType;
        this.imagePath = imagePath;
    }
}
