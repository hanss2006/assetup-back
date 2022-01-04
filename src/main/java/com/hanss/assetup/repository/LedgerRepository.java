package com.hanss.assetup.repository;

import com.hanss.assetup.models.Ledger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LedgerRepository extends JpaRepository<Ledger, Long> {
    Page<Ledger> findByUserId(Long userId, Pageable pageable);
    Page<Ledger> findByUserIdAndNameContainingIgnoreCase(Long userId, String name, Pageable pageable);
}
