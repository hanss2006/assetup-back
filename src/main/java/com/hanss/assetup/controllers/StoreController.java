package com.hanss.assetup.controllers;

import com.hanss.assetup.models.Store;
import com.hanss.assetup.models.User;
import com.hanss.assetup.repository.StoreRepository;
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
public class StoreController implements SecuredRestController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/stories")
    public ResponseEntity<Page<Store>> getStories(
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

            Page<Store> storePage;
            if (name == null)
                storePage = storeRepository.findByUserId(user.getId(), paging);
            else
                storePage = storeRepository.findByUserIdAndNameContainingIgnoreCase(user.getId(), name, paging);
            return new ResponseEntity<>(storePage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/stories/{id}")
    public ResponseEntity<Store> getStore(@PathVariable long id) {
        try {
            Store store = storeRepository.findById(id).get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userRepository.findByUsername(currentPrincipalName).get();
            if (store != null && store.getUserId() == user.getId()) {
                return new ResponseEntity<>(store, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE /users/{username}/todos/{id}
    @DeleteMapping("/stories/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable long id) {
        try {
            Store store = storeRepository.findById(id).get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userRepository.findByUsername(currentPrincipalName).get();

            if (store != null && store.getUserId() == user.getId()) {
                storeRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //Edit/Update a Asset
    //PUT /users/{user_name}/todos/{todo_id}
    @PutMapping("/stories/{id}")
    public ResponseEntity<Store> updateStore(@PathVariable long id, @RequestBody Store store) {
        try {
            Store storeOld = storeRepository.findById(id).get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userRepository.findByUsername(currentPrincipalName).get();
            if (storeOld != null && storeOld.getUserId() == user.getId()) {
                store.setId(id);
                store.setUserId(user.getId());
                Store storeUpdated = storeRepository.save(store);
                return new ResponseEntity<Store>(storeUpdated, HttpStatus.OK);
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/stories")
    public ResponseEntity<Void> createStore(@RequestBody Store store) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userRepository.findByUsername(currentPrincipalName).get();
            store.setId(-1L);
            store.setUserId(user.getId());
            Store createdStore = storeRepository.save(store);
            //Location
            //Get current resource url
            ///{id}
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(createdStore.getId()).toUri();
            return ResponseEntity.created(uri).build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
