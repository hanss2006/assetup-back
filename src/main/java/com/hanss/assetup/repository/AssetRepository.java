package com.hanss.assetup.repository;

import java.util.List;

import com.hanss.assetup.models.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long>{
	List<Asset> findByUserId(Long userId);
}
