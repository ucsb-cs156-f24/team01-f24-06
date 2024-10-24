package edu.ucsb.cs156.example.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 * This is a JPA entity that represents a UCSBDiningCommonsMenuItems
 * 
 * 
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "ucsbdiningcommonsmenuitems")
public class UCSBDiningCommonsMenuItems {
  @Id
  private long id;
  private String diningCommonsCode;
  private String name;
  private String station;
}