package com.hanss.assetup.controllers;

import com.hanss.assetup.models.Currency;
import com.hanss.assetup.repository.CurrencyRepository;
import com.hanss.assetup.security.SecuredRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.net.URI;

/*
@CrossOrigin(origins = "*", maxAge = 3600)
*/
@RestController
@RequestMapping("/api")
public class CurrencyController implements SecuredRestController {
    @Autowired
    private CurrencyRepository currencyRepository;

    @GetMapping("/currencies")
    public ResponseEntity<Page<Currency>> getAllCurrencies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable paging = PageRequest.of(page, size);
            return new ResponseEntity<>(currencyRepository.findAll(paging), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/currencies/{id}")
    public ResponseEntity<Currency> getCurrency(@PathVariable long id) {
        try {
            return new ResponseEntity<>(currencyRepository.findById(id).get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/currencies/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCurrency(@PathVariable long id) {
        Currency currency = currencyRepository.findById(id).get();
        if (currency != null) {
            currencyRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/currencies/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Currency> updateCurrency(@PathVariable long id, @RequestBody Currency currency) {
        Currency currencyOld = currencyRepository.findById(id).get();
        if (currencyOld != null) {
            currencyOld.setName(currency.getName());
            currencyOld.setDescription(currency.getDescription());
            Currency currencyUpdated = currencyRepository.save(currencyOld);
            return new ResponseEntity<Currency>(currencyUpdated, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/currencies")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createCurrency(@RequestBody Currency currency) {
        try {
            currency.setId(-1L);
            Currency createdCurrency = currencyRepository.save(currency);
            //Location
            //Get current resource url
            ///{id}
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(createdCurrency.getId()).toUri();
            return ResponseEntity.created(uri).build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
