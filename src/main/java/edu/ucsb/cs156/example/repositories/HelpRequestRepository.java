package edu.ucsb.cs156.example.repositories;

import org.springframework.data.repository.CrudRepository;

import edu.ucsb.cs156.example.entities.HelpRequest;

public interface HelpRequestRepository extends CrudRepository<HelpRequest, Long>  {

}
