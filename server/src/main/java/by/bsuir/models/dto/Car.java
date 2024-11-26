package by.bsuir.models.dto;


import by.bsuir.enums.BodyType;
import by.bsuir.enums.PetrolType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Car {
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
