package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.entities.UCSBOrganization;
import edu.ucsb.cs156.example.errors.EntityNotFoundException;
import edu.ucsb.cs156.example.repositories.UCSBOrganizationRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
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

@Tag(name = "UCSBOrganizations")
@RequestMapping("/api/ucsborganizations")
@RestController
@Slf4j
public class UCSBOrganizationController extends ApiController {
    @Autowired
    UCSBOrganizationRepository ucsbOrganizationRepository;

    /**
     * This method returns a list of all ucsborganizations.
     * @return a list of all ucsborganizations
     */
    @Operation(summary= "List all ucsb organizations")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<UCSBOrganization> allOrganizations() {
        Iterable<UCSBOrganization> orgs = ucsbOrganizationRepository.findAll();
        return orgs;
    }

    /**
     * This method returns a single organization.
     * @param orgCode code of the organization
     * @return a single organization
     */
    @Operation(summary= "Get a single organization")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public UCSBOrganization getById(
            @Parameter(name="orgCode") @RequestParam String orgCode) {
        UCSBOrganization org = ucsbOrganizationRepository.findById(orgCode)
                .orElseThrow(() -> new EntityNotFoundException(UCSBOrganization.class, orgCode));

        return org;
    }

    /**
     * This method creates a new organiation. Accessible only to users with the role "ROLE_ADMIN".
     * @param orgCode code of the organization
     * @param orgTranslationShort short translation of the organization
     * @param orgTranslation translation of the organization
     * @param inactive whether the organization is inactive
     * @return the newly created organization
     */
    @Operation(summary= "Create a new organization")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/post")
    public UCSBOrganization postOrgs(
        @Parameter(name="orgCode") @RequestParam String orgCode,
        @Parameter(name="orgTranslationShort") @RequestParam String orgTranslationShort,
        @Parameter(name="orgTranslation") @RequestParam String orgTranslation,
        @Parameter(name="inactive") @RequestParam boolean inactive
        )
        {

        UCSBOrganization org = new UCSBOrganization();
        org.setOrgCode(orgCode);
        org.setOrgTranslationShort(orgTranslationShort);
        org.setOrgTranslation(orgTranslation);
        org.setInactive(inactive);

        UCSBOrganization savedOrg = ucsbOrganizationRepository.save(org);

        return savedOrg;
    }

    /**
     * Delete an organization. Accessible only to users with the role "ROLE_ADMIN".
     * @param orgCode code of the organization
     * @return a message indiciating the organization was deleted
     */
    @Operation(summary= "Delete a UCSBOrganization")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("")
    public Object deleteOrgs(
            @Parameter(name="orgCode") @RequestParam String orgCode) {
        UCSBOrganization orgs = ucsbOrganizationRepository.findById(orgCode)
                .orElseThrow(() -> new EntityNotFoundException(UCSBOrganization.class, orgCode));

        ucsbOrganizationRepository.delete(orgs);
        return genericMessage("UCSBOrganization with id %s deleted".formatted(orgCode));
    }

    /**
     * Update a single org. Accessible only to users with the role "ROLE_ADMIN".
     * @param orgCode the code of the org to update
     * @param incoming the new org contents
     * @return the updated org object
     */
    @Operation(summary= "Update a single organization")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("")
    public UCSBOrganization updateOrgs(
            @Parameter(name="orgCode") @RequestParam String orgCode,
            @RequestBody @Valid UCSBOrganization incoming) {

        UCSBOrganization orgs = ucsbOrganizationRepository.findById(orgCode)
                .orElseThrow(() -> new EntityNotFoundException(UCSBOrganization.class, orgCode));

        orgs.setOrgCode(incoming.getOrgCode());
        orgs.setOrgTranslationShort(incoming.getOrgTranslationShort());
        orgs.setOrgTranslation(incoming.getOrgTranslation());
        orgs.setInactive(incoming.getInactive());

        ucsbOrganizationRepository.save(orgs);

        return orgs;
    }
}
