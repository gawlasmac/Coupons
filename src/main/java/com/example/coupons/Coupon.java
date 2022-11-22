package com.example.coupons;

import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    private String barcode;
    private String status;

    public Coupon(final String barcode, final Long customerId, final String status) {
        this.barcode = barcode;
        this.customerId = customerId;
        this.status = status;
    }
}
