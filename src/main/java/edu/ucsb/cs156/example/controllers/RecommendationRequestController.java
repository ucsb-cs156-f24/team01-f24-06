package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.entities.RecommendationRequest;
import edu.ucsb.cs156.example.errors.EntityNotFoundException;
import edu.ucsb.cs156.example.repositories.RecommendationRequestRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.checkerframework.checker.units.qual.g;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.IterableConfigurationPropertySource;
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
 * This is a REST controller for RecommendationRequest
 */

@Tag(name = "RecommendationRequest")
@RequestMapping("/api/recommendationrequest")
@RestController
@Slf4j
public class RecommendationRequestController extends ApiController{
    
    @Autowired
    RecommendationRequestRepository recommendationRequestRepository;

    /**
     * List all recommendation requests
     * 
     * @return an iterable of RecommendationRequest
     */
    @Operation(summary= "List all recommendation requests")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public Iterable<RecommendationRequest> allRecommendationRequests() {
        Iterable<RecommendationRequest> requests = recommendationRequestRepository.findAll();
        return requests;
    }

    /**
     * Create a new recommendation request
     * 
     * @param requesterEmail the email of the requester
     * @param professorEmail the email of the professor
     * @param explanation the explanation of the request
     * @param dateRequested the date the request was made
     * @param dateNeeded the date the request is needed
     * @return the created RecommendationRequest
    */
    
    @Operation(summary= "Create a new recommendation request")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/post")
    public RecommendationRequest postRecommendationRequest(
        @Parameter(name="requesterEmail") @RequestParam String requesterEmail,
        @Parameter(name="professorEmail") @RequestParam String professorEmail,
        @Parameter(name="explanation") @RequestParam String explanation,
        @Parameter(name="dateRequested") @RequestParam("dateRequested") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateRequested,
        @Parameter(name="dateNeeded") @RequestParam("dateNeeded") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateNeeded)
        throws JsonProcessingException {

            log.info("date_Requested{}", dateRequested);

            RecommendationRequest recommendationRequest = new RecommendationRequest();
            recommendationRequest.setRequesterEmail(requesterEmail);
            recommendationRequest.setProfessorEmail(professorEmail);
            recommendationRequest.setExplanation(explanation);
            recommendationRequest.setDateRequested(dateRequested);
            recommendationRequest.setDateNeeded(dateNeeded);
            recommendationRequest.setDone(false);

            RecommendationRequest savedRecommendationRequest = recommendationRequestRepository.save(recommendationRequest);

            return savedRecommendationRequest;
        }
}
