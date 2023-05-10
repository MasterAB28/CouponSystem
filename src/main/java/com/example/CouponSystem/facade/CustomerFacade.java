package com.example.CouponSystem.facade;

import com.example.CouponSystem.beans.Category;
import com.example.CouponSystem.beans.Coupon;
import com.example.CouponSystem.beans.Customer;
import com.example.CouponSystem.exception.ExceptionCoupons;
import com.example.CouponSystem.repository.CompanyRepository;
import com.example.CouponSystem.repository.CouponRepository;
import com.example.CouponSystem.repository.CustomerRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@Service
@Scope("prototype")
public class CustomerFacade extends ClientFacade{
    private int customerId;


    public CustomerFacade(CustomerRepository customerRepository, CouponRepository couponRepository, CompanyRepository companyRepository) {
        super(customerRepository, couponRepository, companyRepository);
    }

    /**
     * The method receives customer's email and password and checks if the email and password are correct,
     * by method 'iscustomerExists' from the dao
     * @param email customer's email
     * @param password customer's password
     * @return true or false
     */
    @Override
    public boolean login(String email, String password) {
        if (customerRepo.existsByEmailAndPassword(email, password)) {
            customerId = customerRepo.findByEmailAndPassword(email, password).getId();
            return true;
        }
        return false;
    }

    /**
     * The method receives a coupon object and checks if this customer has the same coupon,
     * if not,checks if this amount of coupons bigger of 0,
     * if yes checks if this end date of coupons is not over yet
     * if not the method adds it to 'coupons' db by the method 'addCouponPurchase' from the dao
     * @param coupon a coupon object
     * @throws ExceptionCoupons if the purchase is  exists for this customer or if the amount of coupons is not greater than 0 or if the end date of coupons is over
     */
    public void purchaseCoupon(Coupon coupon) throws ExceptionCoupons {
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        if (!isPurchaseExists(coupon.getId())){
            if (coupon.getAmount()>0){
                if (coupon.getEndDate().after(date)){
                    Customer customerToUpdate=getCustomerDetails();
                    Set<Coupon>coupons=customerToUpdate.getCoupons();
                    coupons.add(coupon);
                    customerToUpdate.setCoupons(coupons);
                    customerRepo.save(customerToUpdate);
                    coupon.setAmount(coupon.getAmount()-1);
                    couponRepo.save(coupon);
                } else
                    throw new ExceptionCoupons("The coupon is expired");
            } else
                throw new ExceptionCoupons("The coupon is out of stock");
        } else
            throw new ExceptionCoupons("you already bought the coupon");
    }

    /**
     * The method receives a coupon object and checks if the customer have the purchase
     * if not, throw exception
     * if yes delete the purchase from DB and add 1 to the coupon amount and update it in DB
     * @param coupon coupon to delete purchase
     * @throws ExceptionCoupons if the customer don't have the coupon purchase
     */
    public void deletePurchaseCoupon(Coupon coupon) throws ExceptionCoupons {
        if (isPurchaseExists(coupon.getId())){
            Customer customerToUpdate = getCustomerDetails();
            Set<Coupon> coupons = customerToUpdate.getCoupons();
            coupons.removeIf(coupon1 -> coupon1.getId() == coupon.getId());
            customerToUpdate.setCoupons(coupons);
            customerRepo.save(customerToUpdate);
            coupon.setAmount(coupon.getAmount()+1);
            couponRepo.save(coupon);
        }else {
            throw new ExceptionCoupons("The purchase is not exists");
        }
    }

    /**
     * The method returns all the coupons from the db of the customer that performed the login
     * @return a list of coupons object of this customer`
     * @throws ExceptionCoupons if the customer not exists
     */
    public Set<Coupon> getCustomerCoupons() throws ExceptionCoupons {
      return customerRepo.findById(customerId).orElseThrow(()->new ExceptionCoupons("The customer not exist")).getCoupons();
    }

    /**
     * The method receives category of coupon and returns a list of coupons object from this category
     * of the customer that performed the login
     * @param category coupon's category
     * @return coupons object from this category of this customer
     * @throws ExceptionCoupons if the customer not exists
     */
    public Set<Coupon> getCustomerCoupons(Category category) throws ExceptionCoupons {
        Set<Coupon> couponList = getCustomerCoupons();
        couponList.removeIf(cou -> !cou.getCategory().equals(category));
        return couponList;
    }

    /**
     *The method receives  maximum price of coupons and returns a list of coupons object up to the maximum price
     *  of the customer that performed the login
     * @param maxPrice maximum price of coupons
     * @return a list of coupons object up to the maximum price  of this customer
     * @throws ExceptionCoupons if the customer not exists
     */
    public Set<Coupon> getCustomerCoupons(double maxPrice) throws ExceptionCoupons {
        Set<Coupon> couponList = getCustomerCoupons();
        couponList.removeIf(cou -> cou.getPrice()>(maxPrice));
        return couponList;
    }

    /**
     *The method returns the details of the customer that performed the login
     * @return the details of the customer that performed the login
     * @throws ExceptionCoupons if the customer not exists
     */
    public Customer getCustomerDetails() throws ExceptionCoupons {
        return customerRepo.findById(customerId).orElseThrow(()->new ExceptionCoupons("The customer not exist"));
    }

    /**
     *The method return all the coupons in the DB
     * @return List of all coupons in DB
     */
    public List<Coupon> getAllCoupons(){
        return couponRepo.findAll();
    }

    /**
     * The method checks if the purchase  exists in the db by customer id and coupon id
     * @param couponId coupon id
     * @return true or false
     * @throws ExceptionCoupons if the customer not exists
     */
    private boolean isPurchaseExists(int couponId) throws ExceptionCoupons {
        Set<Coupon> coupons = customerRepo.findById(customerId).orElseThrow(()->new ExceptionCoupons("The customer not exist")).getCoupons();
        for (Coupon coupon:coupons){
            if (coupon.getId() == couponId)
                return true;
        }
        return false;
    }



}
