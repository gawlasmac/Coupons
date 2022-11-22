package com.example.coupons;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CouponsRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> getOneByCustomerIdAndBarcodeAndStatus(Long customerId, String barcode, String status);
}
