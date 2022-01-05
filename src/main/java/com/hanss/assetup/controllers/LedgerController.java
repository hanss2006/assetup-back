package com.hanss.assetup.controllers;

import com.hanss.assetup.models.Ledger;
import com.hanss.assetup.models.User;
import com.hanss.assetup.repository.LedgerRepository;
import com.hanss.assetup.repository.UserRepository;
import com.hanss.assetup.security.SecuredRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import java.net.URI;

/*
@CrossOrigin(origins = "*", maxAge = 3600)
*/
@RestController
@RequestMapping("/api")
public class LedgerController implements SecuredRestController {

    @Autowired
    private LedgerRepository ledgerRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/ledgers")
    public ResponseEntity<Page<Ledger>> getLedgers(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        try {
            Pageable paging = PageRequest.of(page
                    , size
                    , sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userRepository.findByUsername(currentPrincipalName).get();

            Page<Ledger> ledgerPage;
            if (name == null)
                ledgerPage = ledgerRepository.findByUserId(user.getId(), paging);
            else
                ledgerPage = ledgerRepository.findByUserIdAndNameContainingIgnoreCase(user.getId(), name, paging);
            return new ResponseEntity<>(ledgerPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ledgers/{id}")
    public ResponseEntity<Ledger> getLedger(@PathVariable long id) {
        try {
            Ledger ledger = ledgerRepository.findById(id).get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userRepository.findByUsername(currentPrincipalName).get();
            if (ledger != null && ledger.getUserId() == user.getId()) {
                return new ResponseEntity<>(ledger, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE /users/{username}/todos/{id}
    @DeleteMapping("/ledgers/{id}")
    public ResponseEntity<Void> deleteLedger(@PathVariable long id) {
        try {
            Ledger ledger = ledgerRepository.findById(id).get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userRepository.findByUsername(currentPrincipalName).get();

            if (ledger != null && ledger.getUserId() == user.getId()) {
                ledgerRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //Edit/Update a Asset
    //PUT /users/{user_name}/todos/{todo_id}
    @PutMapping("/ledgers/{id}")
    public ResponseEntity<Ledger> updateLedger(@PathVariable long id, @RequestBody Ledger ledger) {
        try {
            Ledger ledgerOld = ledgerRepository.findById(id).get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userRepository.findByUsername(currentPrincipalName).get();
            if (ledgerOld != null && ledgerOld.getUserId() == user.getId()) {
                ledger.setId(id);
                ledger.setUserId(user.getId());
                Ledger ledgerUpdated = ledgerRepository.save(ledger);
                return new ResponseEntity<Ledger>(ledgerUpdated, HttpStatus.OK);
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/ledgers")
    public ResponseEntity<Void> createLedger(@RequestBody Ledger ledger) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userRepository.findByUsername(currentPrincipalName).get();
            ledger.setId(-1L);
            ledger.setUserId(user.getId());
            Ledger createdLedger = ledgerRepository.save(ledger);
            //Location
            //Get current resource url
            ///{id}
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(createdLedger.getId()).toUri();
            return ResponseEntity.created(uri).build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
