package com.saurabh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.saurabh.dto.RestaurantDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity // kisi class ko database table banane ke liye use hoti hai
@Data // Yeh sab kuch automatically generate kar dega (getters, setters, toString, etc.)
@AllArgsConstructor // Sabhi fields ka ek constructor generate karega
@NoArgsConstructor // Default constructor generate karega
public class User {

    @Id // 'id' ko Primary Key bana raha hai
    @GeneratedValue(strategy = GenerationType.AUTO) // ID ko automatically generate karega
    private Long id ;

    private String fullName ;

    private String email ;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password ;

    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER ;

    @JsonIgnore // JSON response me exclude karne ke liye use hota hai.
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer") // one-to-many relationship define karta hai
    // i.e. One User -> Many Orders
    // cascade = CascadeType.ALL means jitne bhi changes parent entity pe honge
    // (save, delete, update, etc.), wo automatically child entity pe bhi apply ho jayenge.
    // Yaha mappedBy = "customer" ka matlab hai ki Order table ke andar
    // customer field already exist karti hai jo foreign key ka kaam karegi.
    private List<Order> orders = new ArrayList<>() ;

    @ElementCollection // JPA automatically ek separate table create kar leta hai jisme ye list ya set store hoti hai.
    // Hame manually alag entity banane ki jarurat nahi padegi is list ke liye
    private List<RestaurantDto> favourites = new ArrayList<>() ;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) // One User -> Many Addresses
    // orphanRemoval = true ka matlab hai ki agar parent list se child ka reference remove ho gaya,
    // toh wo child database se bhi remove ho jayega.
    private List<Address> addresses = new ArrayList<>() ;
}
