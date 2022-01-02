package com.hanss.assetup.controllers;
import java.net.URI;
import java.util.List;

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
    public List<Asset> getAllAssets(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByUsername(currentPrincipalName).get();
        return assetRepository.findByUserId(user.getId());
    }

    @GetMapping("/assets/{id}")
    public Asset getAsset(@PathVariable long id){
        Asset asset =  assetRepository.findById(id).get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByUsername(currentPrincipalName).get();

        if (asset != null && asset.getUserId() == user.getId()){
            return asset;
        }
        return null;
    }

    // DELETE /users/{username}/todos/{id}
    @DeleteMapping("/assets/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable long id) {

        Asset asset =  assetRepository.findById(id).get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByUsername(currentPrincipalName).get();

        if (asset != null && asset.getUserId() == user.getId()){
            assetRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }


    //Edit/Update a Asset
    //PUT /users/{user_name}/todos/{todo_id}
    @PutMapping("/assets/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable long id, @RequestBody Asset asset){

        Asset assetOld =  assetRepository.findById(id).get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByUsername(currentPrincipalName).get();
        if (assetOld != null && assetOld.getUserId() == user.getId()){
            asset.setId(id);
            asset.setUserId(user.getId());
            Asset assetUpdated = assetRepository.save(asset);
            return new ResponseEntity<Asset>(asset, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/assets")
    public ResponseEntity<Void> createAsset(@RequestBody Asset asset){
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
    }
}
