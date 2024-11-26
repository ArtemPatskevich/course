package by.bsuir.models.entities;

import by.bsuir.enums.BodyType;
import by.bsuir.enums.PetrolType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
}

//@Data
//@NoArgsConstructor
//@Entity
//@Table(name = "`group`")
//public class GroupEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    @Column(name = "number", columnDefinition = "INT")
//    private Integer number;
//
//    public GroupEntity(int number) {
//        this.number = number;
//    }
//}