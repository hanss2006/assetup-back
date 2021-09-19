package com.hanss.assetup.webservices.restservices.currency;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    List<Currency> findByName(String name);
}
