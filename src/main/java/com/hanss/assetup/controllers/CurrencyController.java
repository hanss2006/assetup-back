package com.hanss.assetup.controllers;

import com.hanss.assetup.models.Asset;
import com.hanss.assetup.models.Currency;
import com.hanss.assetup.repository.CurrencyRepository;
import com.hanss.assetup.security.SecuredRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<Currency> currencyPage;
            if (name == null)
                currencyPage = currencyRepository.findAll(paging);
            else
                currencyPage = currencyRepository.findByNameContainingIgnoreCase(name, paging);
            return new ResponseEntity<>(currencyPage, HttpStatus.OK);
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
