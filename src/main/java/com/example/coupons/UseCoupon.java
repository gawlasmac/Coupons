package com.example.coupons;

import lombok.Data;

@Data
public class UseCoupon {
    private Long customerId;
    private String barcode;
}
