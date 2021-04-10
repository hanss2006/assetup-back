package com.hanss.assetup.webservices.restservices.asset;
import java.net.URI;
import java.util.List;

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

@CrossOrigin(origins = {"http://localhost:3000", "http://hanss.strangled.net:8080", "http://192.168.5.45:9080", "http://192.168.5.44:3000"})
@RestController
public class AssetJpaResource {

    @Autowired
    private AssetJpaRepository assetJpaRepository;


    @GetMapping("/api/users/{username}/assets")
    public List<Asset> getAllAssets(@PathVariable String username){
        return assetJpaRepository.findByUsername(username);
    }

    @GetMapping("/api/users/{username}/assets/{id}")
    public Asset getAsset(@PathVariable String username, @PathVariable long id){
        return assetJpaRepository.findById(id).get();
    }

    // DELETE /users/{username}/todos/{id}
    @DeleteMapping("/api/users/{username}/assets/{id}")
    public ResponseEntity<Void> deleteAsset(
            @PathVariable String username, @PathVariable long id) {
        assetJpaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    //Edit/Update a Meal
    //PUT /users/{user_name}/todos/{todo_id}
    @PutMapping("/api/users/{username}/assets/{id}")
    public ResponseEntity<Asset> updateAsset(
            @PathVariable String username,
            @PathVariable long id, @RequestBody Asset meal){
        meal.setUsername(username);
        Asset mealUpdated = assetJpaRepository.save(meal);
        return new ResponseEntity<Asset>(meal, HttpStatus.OK);
    }

    @PostMapping("/api/users/{username}/assets")
    public ResponseEntity<Void> createAsset(
            @PathVariable String username, @RequestBody Asset meal){

        meal.setUsername(username);
        Asset createdTodo = assetJpaRepository.save(meal);
        //Location
        //Get current resource url
        ///{id}
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(createdTodo.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

}
