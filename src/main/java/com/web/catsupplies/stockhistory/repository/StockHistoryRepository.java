package com.web.catsupplies.stockhistory.repository;

import com.web.catsupplies.stockhistory.domain.StockHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockHistoryRepository extends JpaRepository<StockHistory, Long> {
    // 제품의 모든 재고 기록을 조회한다.
    List<StockHistory> findAllByStock_IdOrderByCreatedAtDesc(Long stockId);
}
