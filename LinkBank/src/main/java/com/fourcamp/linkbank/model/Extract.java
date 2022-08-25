package com.fourcamp.linkbank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Extract {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "extract_id", nullable = false)
    private Long extract_id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Double value;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long account_id;

}
