package com.nix.ecommerceapi.service.impl;

import com.nix.ecommerceapi.model.entity.Order;
import com.nix.ecommerceapi.model.entity.OrderDetail;
import com.nix.ecommerceapi.model.response.CartResponse;
import com.nix.ecommerceapi.repository.ModelRepository;
import com.nix.ecommerceapi.repository.OrderDetailRepository;
import com.nix.ecommerceapi.service.OrderDetailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final ModelRepository modelRepository;
    @Transactional
    @Override
    public void batchInsertOrderDetail(Order order, List<CartResponse> cartResponses) {
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (CartResponse cartResponse : cartResponses) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setModel(modelRepository.getReferenceById(cartResponse.getProduct().getModelId()));
            orderDetail.setQuantity(cartResponse.getQuantity());
            orderDetail.setPrice(cartResponse.getProduct().getPrice());
            orderDetail.setTotalPrice(cartResponse.getTotalCost());
            orderDetailList.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetailList);
    }
}
