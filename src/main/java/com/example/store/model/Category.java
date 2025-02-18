package com.example.store.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "products")
@ToString(exclude = "products")
@Entity
public class Category {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false,unique = true)
    private String name;
    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
