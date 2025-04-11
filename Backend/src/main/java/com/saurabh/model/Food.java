package com.saurabh.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    private String name ;

    private String description ;

    private Long price ;

    @ManyToOne // Many Food -> One Category
    private Category foodCategory ;

    @Column(length = 1000)
    @ElementCollection
    private List<String> images ;

    private boolean available ;

    @ManyToOne // Many Food -> One Restaurant
    private Restaurant restaurant ;

    private boolean isVegetarian ;

    private boolean isSeasonal ;

    @ManyToMany
    private List<IngredientsItem> ingredients = new ArrayList<>() ;

    private Date creationDate ;

}
