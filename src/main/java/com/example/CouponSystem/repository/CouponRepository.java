package com.example.CouponSystem.repository;



import com.example.CouponSystem.beans.Category;
import com.example.CouponSystem.beans.Coupon;
import com.example.CouponSystem.exception.ExceptionCoupons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Integer> {
    /**
     * the method get all the company coupons from DB
     * @param companyId the company id
     * @return list of coupons that the company have
     */
    List<Coupon> findByCompanyId(int companyId);

    /**
     * the method get all the company coupons that have the category we want from DB
     * @param companyId the company id
     * @param category category we want to get coupons
     * @return list of coupons that the company have on the same category that we want
     */
    List<Coupon> findByCompanyIdAndCategory(int companyId, Category category);

    /**
     * the method get all the company coupons that the price is less or equal than the price we want from DB
     * @param companyId the company id
     * @param maxPrice the price we want
     * @return list of coupons that the company have and less or equal than the price we want
     */
    List<Coupon> findByCompanyIdAndPriceLessThanEqual(int companyId, double maxPrice);

    /**
     * check if the coupon is exist by the company id and the title
     * @param title coupon title
     * @param companyId the company id
     * @return true or false
     */
    boolean existsByTitleAndCompanyId(String title,int companyId);

    /**
     * the method delete coupon by company id from DB
     * @param companyId the company id
     * @throws ExceptionCoupons if the company not exists
     */
    void deleteByCompanyId(int companyId) throws ExceptionCoupons;

}
