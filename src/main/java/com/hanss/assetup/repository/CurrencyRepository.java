package com.hanss.assetup.repository;

import com.hanss.assetup.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    List<Currency> findByName(String name);
}