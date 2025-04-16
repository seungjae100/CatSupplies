package com.web.catsupplies.stockhistory.application;

import com.web.catsupplies.common.exception.NotFoundException;
import com.web.catsupplies.stock.domain.Stock;
import com.web.catsupplies.stock.repository.StockRepository;
import com.web.catsupplies.stockhistory.domain.StockHistory;
import com.web.catsupplies.stockhistory.repository.StockHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockHistoryService {

    private StockHistoryRepository stockHistoryRepository;
    private StockRepository stockRepository;

    @Transactional(readOnly = true)
    public List<StockHistoryResponse> getHistory(Long productId, Long companyId) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new NotFoundException("해당 제품이 존재하지 않습니다."));

        if (!stock.getProduct().getCompany().getId().equals(companyId)) {
            throw new SecurityException("조회 권한이 없습니다.");
        }

        List<StockHistory> histories =
                stockHistoryRepository.findAllByStockProductIdOrderByCreatedAtDesc(productId);

        return histories.stream()
                .map(StockHistoryResponse::from)
                .collect(Collectors.toList());
    }
}
