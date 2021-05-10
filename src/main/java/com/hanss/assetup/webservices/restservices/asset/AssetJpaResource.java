package com.hanss.assetup.webservices.restservices.asset;
import java.net.URI;
import java.util.List;

import com.hanss.assetup.user.User;
import com.hanss.assetup.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@CrossOrigin("*")
@RestController
public class AssetJpaResource {

    @Autowired
    private AssetJpaRepository assetJpaRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/api/assets")
    public List<Asset> getAllAssets(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByEmail(currentPrincipalName);
        return assetJpaRepository.findByUserId(user.getId());
    }

    @GetMapping("/api/assets/{id}")
    public Asset getAsset(@PathVariable long id){
        Asset asset =  assetJpaRepository.findById(id).get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByEmail(currentPrincipalName);

        if (asset != null && asset.getUserId() == user.getId()){
            return asset;
        }
        return null;
    }

    // DELETE /users/{username}/todos/{id}
    @DeleteMapping("/api/assets/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable long id) {

        Asset asset =  assetJpaRepository.findById(id).get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByEmail(currentPrincipalName);

        if (asset != null && asset.getUserId() == user.getId()){
            assetJpaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }


    //Edit/Update a Asset
    //PUT /users/{user_name}/todos/{todo_id}
    @PutMapping("/api/assets/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable long id, @RequestBody Asset asset){

        Asset assetOld =  assetJpaRepository.findById(id).get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByEmail(currentPrincipalName);
        if (assetOld != null && assetOld.getUserId() == user.getId()){
            asset.setId(id);
            asset.setUserId(user.getId());
            Asset assetUpdated = assetJpaRepository.save(asset);
            return new ResponseEntity<Asset>(asset, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/api/assets")
    public ResponseEntity<Void> createAsset(@RequestBody Asset asset){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User user = userRepository.findByEmail(currentPrincipalName);
        asset.setId(-1L);
        asset.setUserId(user.getId());
        Asset createdAsset = assetJpaRepository.save(asset);
        //Location
        //Get current resource url
        ///{id}
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(createdAsset.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
