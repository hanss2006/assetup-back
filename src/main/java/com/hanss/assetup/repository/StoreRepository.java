package com.hanss.assetup.repository;

import com.hanss.assetup.models.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Page<Store> findByUserId(Long userId, Pageable pageable);
    Page<Store> findByUserIdAndNameContainingIgnoreCase(Long userId, String name, Pageable pageable);
}