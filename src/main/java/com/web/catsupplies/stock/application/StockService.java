package com.web.catsupplies.stock.application;

import com.web.catsupplies.common.exception.NotFoundException;
import com.web.catsupplies.stock.domain.Stock;
import com.web.catsupplies.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StockService {

    private final StockRepository stockRepository;

    private Stock findStockByProductIdAndValidate(Long productId, Long companyId) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new NotFoundException("해당 제품의 재고가 존재하지 않습니다."));
        if (!stock.getProduct().getCompany().getId().equals(companyId)) {
            throw new SecurityException("해당 권한이 없습니다.");
        }
        return stock;
    }

    // 재고 조회
    @Transactional(readOnly = true)
    public StockResponse getStock(Long productId, Long companyId) {
        Stock stock = findStockByProductIdAndValidate(productId, companyId);
        return StockResponse.from(stock);
    }

    // 입고
    @Transactional
    public void inboundStock(Long productId, int amount, Long companyId) {
        Stock stock = findStockByProductIdAndValidate(productId, companyId);
        stock.inboundStock(amount);
    }

    // 출고
    @Transactional
    public void outboundStock(Long productId, int amount, Long companyId) {
        Stock stock = findStockByProductIdAndValidate(productId, companyId);
        stock.outboundStock(amount);
    }

    // 재고 증가
    @Transactional
    public void increaseStock(Long productId, int amount, Long companyId) {
        Stock stock =findStockByProductIdAndValidate(productId, companyId);
        stock.increaseStock(amount);
    }

    // 재고 감소
    @Transactional
    public void decreaseStock(Long productId, int amount, Long companyId) {
        Stock stock =findStockByProductIdAndValidate(productId, companyId);
        stock.decreaseStock(amount);
    }

    // 품절처리
    @Transactional
    public void soldOut(Long productId, Long companyId) {
        Stock stock = findStockByProductIdAndValidate(productId, companyId);
        stock.soldOut();
    }
}
