package com.saurabh.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.List;

@Data
@Embeddable // @Embeddable ka matlab hai ki ye class kisi doosri entity me embed ho sakti hai.
// Iska apna koi @Entity aur @Id nahi hota.
public class RestaurantDto {

    private String title ;

    @Column(length = 1000) //  Jab hume kisi column ka maximum character limit set karna ho
    private List<String> images ;

    private String description ;
    private Long id ;

}
