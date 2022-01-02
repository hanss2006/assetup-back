package com.hanss.assetup.controllers;

import com.hanss.assetup.models.Currency;
import com.hanss.assetup.repository.CurrencyRepository;
import com.hanss.assetup.security.SecuredRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class CurrencyController implements SecuredRestController {
    @Autowired
    private CurrencyRepository currencyRepository;

    @GetMapping("/currencies")
    public List<Currency> getAllCurrencies(){
        return currencyRepository.findAll();
    }

    @GetMapping("/currencies/{id}")
    public Currency getCurrency(@PathVariable long id){
        return  currencyRepository.findById(id).get();
    }

    @DeleteMapping("/currencies/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCurrency(@PathVariable long id) {

        Currency currency =  currencyRepository.findById(id).get();
        if (currency != null){
            currencyRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/currencies/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Currency> updateCurrency(@PathVariable long id, @RequestBody Currency currency){

        Currency currencyOld =  currencyRepository.findById(id).get();
        if (currencyOld != null){
            currencyOld.setName(currency.getName());
            currencyOld.setDescription(currency.getDescription());
            Currency currencyUpdated = currencyRepository.save(currencyOld);
            return new ResponseEntity<Currency>(currencyUpdated, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/currencies")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> createCurrency(@RequestBody Currency currency){
        currency.setId(-1L);
        Currency createdCurrency = currencyRepository.save(currency);
        //Location
        //Get current resource url
        ///{id}
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(createdCurrency.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

}
