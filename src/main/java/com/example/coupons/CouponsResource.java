package com.example.coupons;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
@AllArgsConstructor
public class CouponsResource {

    CustomerApiClient customerApiClient;
    CouponsRepository couponsRepository;

    List<IssueCoupon> issueCouponRequests = new ArrayList<>();

    @PostMapping("/issue")
    @HystrixCommand(fallbackMethod = "fallbackIssueCoupon")
    public ResponseEntity<HttpStatus> issueCoupon(@RequestBody IssueCoupon issueCouponRequest) {
        ResponseEntity<Customer> response = customerApiClient.getCustomerById(issueCouponRequest.getCustomerId());
        if (response.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
        if (response.getBody() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        final Coupon coupon = new Coupon("random", response.getBody().getId(), "NEW");
        couponsRepository.save(coupon);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<HttpStatus> fallbackIssueCoupon(@RequestBody IssueCoupon issueCouponRequest) {
        issueCouponRequests.add(issueCouponRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    @PostMapping("/use")
    public ResponseEntity<HttpStatus> useCoupon(@RequestBody UseCoupon useCouponRequest) {
        try {
            ResponseEntity<List<Customer>> customers = customerApiClient.getAllCustomers();
            checkCustomerExists(useCouponRequest.getCustomerId(), customers);
            final Coupon coupon = couponsRepository.getOneByCustomerIdAndBarcodeAndStatus(useCouponRequest.getCustomerId(), useCouponRequest.getBarcode(), "NEW").get();
            final Coupon newCoupon = new Coupon(coupon.getId(), coupon.getCustomerId(), coupon.getBarcode(), "USED");
            couponsRepository.save(newCoupon);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (final NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private void checkCustomerExists(final Long customerId, final ResponseEntity<List<Customer>> customers) throws NoSuchElementException {
        Objects.requireNonNull(customers.getBody()).stream().filter(cust -> customerId.equals(cust.getId())).findFirst().get();
    }

}
