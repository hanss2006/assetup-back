package com.hanss.assetup.controllers;

import java.net.URI;
import com.hanss.assetup.models.Asset;
import com.hanss.assetup.models.User;
import com.hanss.assetup.repository.AssetRepository;
import com.hanss.assetup.repository.UserRepository;
import com.hanss.assetup.security.SecuredRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

/*
@CrossOrigin(origins = "*", maxAge = 3600)
*/
@RestController
@RequestMapping("/api")
public class AssetController implements SecuredRestController {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/assets")
    public ResponseEntity<Page<Asset>> getAllAssets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Pageable paging = PageRequest.of(page, size);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userRepository.findByUsername(currentPrincipalName).get();
            return new ResponseEntity<>(assetRepository.findByUserId(user.getId(), paging), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/assets/{id}")
    public ResponseEntity<Asset> getAsset(@PathVariable long id) {
        try {
            Asset asset = assetRepository.findById(id).get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userRepository.findByUsername(currentPrincipalName).get();
            if (asset != null && asset.getUserId() == user.getId()) {
                return new ResponseEntity<>(asset, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE /users/{username}/todos/{id}
    @DeleteMapping("/assets/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable long id) {
        try {
            Asset asset = assetRepository.findById(id).get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userRepository.findByUsername(currentPrincipalName).get();

            if (asset != null && asset.getUserId() == user.getId()) {
                assetRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //Edit/Update a Asset
    //PUT /users/{user_name}/todos/{todo_id}
    @PutMapping("/assets/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable long id, @RequestBody Asset asset) {
        try {
            Asset assetOld = assetRepository.findById(id).get();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userRepository.findByUsername(currentPrincipalName).get();
            if (assetOld != null && assetOld.getUserId() == user.getId()) {
                asset.setId(id);
                asset.setUserId(user.getId());
                Asset assetUpdated = assetRepository.save(asset);
                return new ResponseEntity<Asset>(assetUpdated, HttpStatus.OK);
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/assets")
    public ResponseEntity<Void> createAsset(@RequestBody Asset asset) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            User user = userRepository.findByUsername(currentPrincipalName).get();
            asset.setId(-1L);
            asset.setUserId(user.getId());
            Asset createdAsset = assetRepository.save(asset);
            //Location
            //Get current resource url
            ///{id}
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(createdAsset.getId()).toUri();
            return ResponseEntity.created(uri).build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
