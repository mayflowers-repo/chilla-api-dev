package com.mayflowertech.chilla.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mayflowertech.chilla.entities.AddressProofDocument;

@Repository
public interface IDocumentRepository extends JpaRepository<AddressProofDocument, Long> {
    
    /**
     * Find all documents associated with a specific user ID.
     *
     * @param userId The ID of the user whose documents are to be retrieved.
     * @return A list of AddressProofDocuments associated with the specified user.
     */
	List<AddressProofDocument> findByUserId(UUID userId);
}