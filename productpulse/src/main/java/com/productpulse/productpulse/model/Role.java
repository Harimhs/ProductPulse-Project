package com.productpulse.productpulse.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    protected Role() {
    }

    public Role(String name) {
        this.name= name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "display_name", nullable = false)
    private String name;
}
