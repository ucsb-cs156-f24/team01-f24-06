package edu.ucsb.cs156.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "ucsborganizations")
public class UCSBOrganization {
    @Id
    @Column(name = "ORGCODE")
    private String orgCode;
    @Column(name = "ORGTRANSLATIONSHORT")
    private String orgTranslationShort;
    @Column(name = "ORGTRANSLATION")
    private String orgTranslation;
    @Column(name = "INACTIVE")
    private boolean inactive;
}
