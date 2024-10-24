package edu.ucsb.cs156.example.controllers;
import edu.ucsb.cs156.example.entities.Restaurant;
import edu.ucsb.cs156.example.entities.UCSBDiningCommons;
import edu.ucsb.cs156.example.entities.UCSBDiningCommonsMenuItem;


import edu.ucsb.cs156.example.errors.EntityNotFoundException;
import edu.ucsb.cs156.example.repositories.UCSBDiningCommonsMenuItemRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;



import java.time.LocalDateTime;

/**
 * This is a REST controller for UCSBDates
 */

@Tag(name = "UCSBDiningCommonsMenuItem")
@RequestMapping("/api/ucsbdiningcommonsmenuitem")
@RestController
@Slf4j
public class UCSBDiningCommonsMenuItemController extends ApiController {

    @Autowired
    UCSBDiningCommonsMenuItemRepository repo;

    /**
     * List all UCSB Dining Commons Menu Items
     * 
     * @return an iterable of UCSB Dining Commons Menu Items
     */
    @Operation(summary= "List all ucsb Dinning Commons Menu items")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<UCSBDiningCommonsMenuItem> allUCSBDiningCommonsMenuItem() {
        Iterable<UCSBDiningCommonsMenuItem> items = repo.findAll();
        return items;
    }
    /**
     * This method returns a single UCSB Dining Commons Menu Item.
     * @param id id of the UCSB Dining Commons Menu Item to get
     * @return a single UCSB Dining Commons Menu Item
     */
    @Operation(summary = "Get a single restaurant")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public UCSBDiningCommonsMenuItem getById(
            @Parameter(name = "id") @RequestParam Long id) {
                UCSBDiningCommonsMenuItem item = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(UCSBDiningCommonsMenuItem.class, id));

        return item;
    }

    /**
     * This method creates a new UCSB Dining Commons Menu Item. Accessible only to users with the role "ROLE_ADMIN".
     * @param name name of the UCSB Dining Commons Menu Item
     * 
     * @param description description of the UCSB Dining Commons Menu Item
     * @return the save UCSB Dining Commons Menu Item (with it's id field set by the database)
     */
    @Operation(summary = "Create a new UCSB Dining Commons Menu Item")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/post")
    public UCSBDiningCommonsMenuItem postUCSBDiningCommonsMenuItem(
            @Parameter(name = "name") @RequestParam String name,
            @Parameter(name = "station") @RequestParam String station,
            @Parameter(name = "diningCommonsCode") @RequestParam String diningCommonsCode){
                UCSBDiningCommonsMenuItem item= new UCSBDiningCommonsMenuItem();

        item.setName(name);
        item.setStation(station);
        item.setDiningCommonsCode(diningCommonsCode);
        
        UCSBDiningCommonsMenuItem savedrestaurant = repo.save(item);
        return savedrestaurant;
    }

    /**
     * Deletes a  UCSB Dining Commons Menu Item. Accessible only to users with the role "ROLE_ADMIN".
     * @param id id of the  UCSB Dining Commons Menu Item to delete
     * @return a message indicating that the  UCSB Dining Commons Menu Item was deleted
     */
    @Operation(summary = "Delete a UCSB Dining Commons Menu Item")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("")
    public Object deleteUCSBDiningCommonsMenuItem(
            @Parameter(name = "id") @RequestParam Long id) {
                UCSBDiningCommonsMenuItem item = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(UCSBDiningCommonsMenuItem.class, id));

        repo.delete(item);
        return genericMessage("UCSBDiningCommonsMenuItem with id %s deleted".formatted(id));
    }

    /**
     * Update a single  UCSB Dining Commons Menu Item. Accessible only to users with the role "ROLE_ADMIN".
     * @param id id of the  UCSB Dining Commons Menu Item to update
     * @param incoming the new restaurant contents
     * @return the updated restaurant object
     */
    @Operation(summary = "Update a single  UCSB Dining Commons Menu Item")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("")
    public UCSBDiningCommonsMenuItem updateRestaurant(
            @Parameter(name = "id") @RequestParam Long id,
            @RequestBody @Valid UCSBDiningCommonsMenuItem incoming) {

                UCSBDiningCommonsMenuItem item = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Restaurant.class, id));

        item.setName(incoming.getName());
        item.setStation(incoming.getStation());
        item.setDiningCommonsCode(incoming.getDiningCommonsCode());

        repo.save(item);

        return item;
    }
}
