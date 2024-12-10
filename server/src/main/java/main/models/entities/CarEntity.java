package main.models.entities;

import lombok.*;
import main.enums.entityAttributes.BodyType;
import main.enums.entityAttributes.PetrolType;
import main.models.dto.Car;
import jakarta.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "car")
public class CarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "brand", columnDefinition = "VARCHAR(100)")
    private String brand;

    @Column(name = "cost", columnDefinition = "DOUBLE")
    private double cost;

    @Enumerated(EnumType.STRING)
    @Column(name = "petrol_type", columnDefinition = "ENUM('GASOLINE','ELECTRIC','DIESEL')")
    private PetrolType petrolType;

    @Enumerated(EnumType.STRING)
    @Column(name = "body_type", columnDefinition = "ENUM('PASSENGER','COUPE','MINIVAN','SUV')")
    private BodyType bodyType;

    @Column(name = "image_path", columnDefinition = "VARCHAR(100)")
    private String imagePath;

    public CarEntity(Car car) {
        this.id = car.getId();
        this.brand = car.getBrand();
        this.cost = car.getCost();
        this.petrolType = car.getPetrolType();
        this.bodyType = car.getBodyType();
        this.imagePath = car.getImagePath();
    }

    public Car toCar() {
        return new Car(id, brand, cost, petrolType, bodyType, imagePath);
    }
}