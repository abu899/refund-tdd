package com.tdd.refund;

import com.tdd.customer.Customer;
import com.tdd.customer.CustomerService;
import com.tdd.shop.Shop;
import com.tdd.shop.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefundService {
    private final RefundRepository refundRepository;
    private final CustomerService customerService;
    private final ShopService shopService;

    @Transactional
    public void addRefund(RefundRequestDto refundDto) {
        Customer customer = customerService.findCustomerByPassport(refundDto.getEncPassportNum());
        double refundCost = refundDto.getPrice() * 0.1;
        Shop shop = shopService.findShopById(refundDto.getShopId());
        Refund refund = new Refund(refundDto.getPrice(), refundDto.getProductCategory(), refundCost, customer);
        refund.setShop(shop);
        refundRepository.save(refund);
    }

    public List<RefundResponseDto> findRefundByPassportNum(String encPassportNum) {
        String decryptPassportNumber = customerService.decryptPassportNumber(encPassportNum);
        List<Refund> refunds = refundRepository.findByDecPassportNum(decryptPassportNumber);
        return refunds.stream()
                .map(refund -> new RefundResponseDto(refund.getId(), refund.getPrice(), refund.getRefundCost()))
                .collect(Collectors.toList());
    }

    @Transactional
    public RefundCancelDto cancel(Long refundId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(IllegalArgumentException::new);

        refund.cancelRefund();

        return new RefundCancelDto(refund.getId(), refund.getRefundStatus());
    }
}
