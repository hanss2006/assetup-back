package com.hanss.assetup.webservices.restservices.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin("*")
@RestController
public class CurrencyResource {
    @Autowired
    private CurrencyRepository currencyRepository;

    @GetMapping("/api/currencies")
    public List<Currency> getAllCurrencies(){
        return currencyRepository.findAll();
    }

    @GetMapping("/api/currencies/{id}")
    public Currency getCurrency(@PathVariable long id){
        return  currencyRepository.findById(id).get();
    }

    @DeleteMapping("/api/currencies/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCurrency(@PathVariable long id) {

        Currency currency =  currencyRepository.findById(id).get();
        if (currency != null){
            currencyRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/api/currencies/{id}")
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

    @PostMapping("/api/currencies")
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
