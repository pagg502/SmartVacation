package com.backend.ecommercespringbootbackend.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;
import java.util.Set;



@Entity
@Table(name="divisions")
@Getter
@Setter
public class Division {
    @Id
    @Column(name="division_id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="division", nullable = false)
    private String division_name;

    @Column(name="create_date")
    @CreationTimestamp
    private Date create_date;

    @Column(name="last_update")
    @UpdateTimestamp
    private Date last_update;

    @ManyToOne
    @JoinColumn(name="country_ID", nullable = false)
    private Country country;

    //In order to division to work, we have to explicit tell the json response to send the Country ID
    @Transient
    public Long getCountry_id() {
        return country != null ? country.getId() : null;
    }

    @OneToMany(mappedBy = "division")
    private Set<Customer> customers;
}
