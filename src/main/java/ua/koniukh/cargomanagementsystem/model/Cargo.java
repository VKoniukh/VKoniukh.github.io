package ua.koniukh.cargomanagementsystem.model;

import javax.persistence.Column;

import lombok.*;
import ua.koniukh.cargomanagementsystem.model.dto.CargoDTO;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Data
@Table(name = "cargos")
public class Cargo {

    @OneToOne(mappedBy = "cargo", cascade = CascadeType.ALL)
    private Order order;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "type")
    private String type;

    @Column(name = "weight")
    private int weight;

    @Column(name = "length")
    private int length;

    @Column(name = "height")
    private int height;

    @Column(name = "width")
    private int width;

    public Cargo(CargoDTO cargoDTO) {
        this.type = cargoDTO.getType();
        this.weight = cargoDTO.getWeight();
        this.length = cargoDTO.getLength();
        this.height = cargoDTO.getHeight();
        this.width = cargoDTO.getWidth();
    }
}
