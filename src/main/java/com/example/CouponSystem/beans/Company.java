package com.example.CouponSystem.beans;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name, email, password;
    @OneToMany(mappedBy = "company",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Coupon> coupons;

    public Company() {
    }

    public Company(String name, String email, String password)  {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Company(int id, String name, String email, String password, List<Coupon> coupons)  {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.coupons = coupons;
    }

    public Company(int id, String name, String email, String password)  {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", coupons=" + coupons +
                '}';
    }
}
