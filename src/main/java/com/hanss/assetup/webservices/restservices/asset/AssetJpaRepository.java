package com.hanss.assetup.webservices.restservices.asset;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetJpaRepository extends JpaRepository<Asset, Long>{
	List<Asset> findByUsername(String username);
}
