package com.hanss.assetup.repository;

import com.hanss.assetup.models.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long>{
	Page<Asset> findByUserId(Long userId, Pageable pageable);
	Page<Asset> findByUserIdAndNameContainingIgnoreCase(Long userId, String name, Pageable pageable);
}
