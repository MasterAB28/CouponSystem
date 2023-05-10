package com.example.CouponSystem.repository;


import com.example.CouponSystem.beans.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    /**
     * the method check if there is customer with the email we want to check
     * @param email customer email
     * @return true or false
     */
    boolean existsByEmail(String email);

    /**
     * the method check if there is customer with the email and password we want to check
     * @param email customer email
     * @param password customer password
     * @return true or false
     */
    boolean existsByEmailAndPassword(String email, String password);

    /**
     * find a customer with the email and the password
     * @param email customer email
     * @param password customer password
     * @return customer
     */
    Customer findByEmailAndPassword(String email, String password);
}
