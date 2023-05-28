package com.example.CouponSystem.facade;


import com.example.CouponSystem.beans.Category;
import com.example.CouponSystem.beans.Company;
import com.example.CouponSystem.beans.Coupon;
import com.example.CouponSystem.beans.Customer;
import com.example.CouponSystem.exception.ExceptionCoupons;
import com.example.CouponSystem.repository.CompanyRepository;
import com.example.CouponSystem.repository.CouponRepository;
import com.example.CouponSystem.repository.CustomerRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Scope("prototype")
public class CompanyFacade extends ClientFacade {
    private int companyId;

    public CompanyFacade(CustomerRepository customerRepository, CouponRepository couponRepository, CompanyRepository companyRepository) {
        super(customerRepository, couponRepository, companyRepository);
    }

    /**
     * The method receives company's email and password and checks if the email and password are correct,
     * by method 'isCompanyExists' from the dao
     *
     * @param email    company's email
     * @param password company's password
     * @return true or false
     */
    @Override
    public boolean login(String email, String password) {
        if (companyRepo.existsByEmailAndPassword(email, password)) {
            companyId = companyRepo.findByEmailAndPassword(email, password).getId();
            return true;
        }
        return false;
    }

    /**
     * The method receives a coupon object and checks if this company has the same title to another coupon,
     * if not, the method adds it to 'coupons' db by the method 'addCoupon' from the dao
     *
     * @param coupon a coupon object
     * @throws ExceptionCoupons
     */
    public void addCoupon(Coupon coupon) throws ExceptionCoupons {
        if (coupon.getEndDate().after(coupon.getStartDate())) {
            coupon.setCompany(getCompanyDetails());
            if (couponRepo.existsByTitleAndCompanyId(coupon.getTitle(), coupon.getCompany().getId())) {
                throw new ExceptionCoupons("This title is exists for your company");
            }
            couponRepo.save(coupon);
        } else {
            throw new ExceptionCoupons("End date must be after start date");
        }
    }

    /**
     * The method receives a coupon object, checks if the coupon is exists in 'coupons' db,
     * and then updates it in the db
     *
     * @param coupon a coupon object
     * @throws ExceptionCoupons if the coupon is not exist
     */
    public void updateCoupon(Coupon coupon) throws ExceptionCoupons {
        if (coupon.getEndDate().after(coupon.getStartDate())) {
            if (!couponRepo.findById(coupon.getId()).orElseThrow(() -> new ExceptionCoupons("The id not found")).getTitle().equals(coupon.getTitle())) {
                if (couponRepo.existsByTitleAndCompanyId(coupon.getTitle(), coupon.getCompany().getId())) {
                    throw new ExceptionCoupons("This title is exists for your company");
                }
            }
            couponRepo.save(coupon);
        } else {
            throw new ExceptionCoupons("End date must be after start date");
        }
    }

    /**
     * The method receives coupon's id and checks f the coupon is exists in the 'coupons' db
     * and then deletes the coupon purchase history by remove the coupon from the customer coupons list,
     * and finally deletes the coupon
     *
     * @param couponId coupon's id
     * @throws ExceptionCoupons if the coupon is not exists
     */
    public void deleteCouponById(int couponId) throws ExceptionCoupons {
        if (couponRepo.existsById(couponId)) {
            List<Customer> customers = customerRepo.findAll();
            for (Customer cus : customers) {
                Set<Coupon> coupons = cus.getCoupons();
                coupons.removeIf(coup -> coup.getId() == couponId);
                cus.setCoupons(coupons);
                customerRepo.save(cus);
            }
            couponRepo.deleteById(couponId);
        } else
            throw new ExceptionCoupons("coupon is not exists");
    }

    /**
     * The method returns all the coupons from the db of the company that performed the login
     *
     * @return a list of coupons object of this company
     */
    public List<Coupon> getCompanyCoupons() {
        return couponRepo.findByCompanyId(companyId);
    }

    /**
     * The method receives category of coupon and returns a list of coupons object from this category
     * of the company that performed the login
     *
     * @param category coupon's category
     * @return coupons object from this category of this company
     */
    public List<Coupon> getCompanyCoupons(Category category) {
        return couponRepo.findByCompanyIdAndCategory(companyId, category);
    }

    /**
     * The method receives  maximum price of coupons and returns a list of coupons object up to the maximum price
     * of the company that performed the login
     *
     * @param maxPrice maximum price of coupons
     * @return a list of coupons object up to the maximum price  of this company
     */
    public List<Coupon> getCompanyCoupons(double maxPrice) {
        return couponRepo.findByCompanyIdAndPriceLessThanEqual(companyId, maxPrice);
    }

    /**
     * The method returns the details of the company that performed the login
     *
     * @return the details of the company that performed the login
     * @throws ExceptionCoupons if the company is not exists
     */
    public Company getCompanyDetails() throws ExceptionCoupons {
        return companyRepo.findById(companyId).orElseThrow(() -> new ExceptionCoupons("The id not found"));
    }

}
