package edu.ucsb.cs156.example.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * This is a JPA entity that represents a recommendation request.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "recommendationrequest")
public class RecommendationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "REQUESTEREMAIL")
    private String requesterEmail;
    @Column(name = "PROFESSOREMAIL")
    private String professorEmail;
    @Column(name = "EXPLANATION")
    private String explanation;
    @Column(name = "DATEREQUESTED")
    private LocalDateTime dateRequested;
    @Column(name = "DATENEEDED")
    private LocalDateTime dateNeeded;
    @Column(name = "DONE")
    private boolean done;
}