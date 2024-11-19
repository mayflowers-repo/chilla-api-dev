package com.mayflowertech.chilla.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.AddressProofDocument;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.enums.DocumentType;

public interface IDocumentService {
    
    /**
     * Adds a new document for the specified user.
     *
     * @param user         The user to whom the document belongs.
     * @param file         The file to be uploaded.
     * @param documentType The type of document being uploaded.
     * @return The added AddressProofDocument.
     * @throws CustomException if there is an error during the upload process.
     */
    AddressProofDocument addDocument(String userId, MultipartFile file, DocumentType documentType) throws CustomException;

    /**
     * Retrieves a specific document by its ID.
     *
     * @param documentId The ID of the document to be retrieved.
     * @return The AddressProofDocument with the specified ID.
     * @throws CustomException if the document is not found or there is another error.
     */
    AddressProofDocument getDocumentById(Long documentId) throws CustomException;

    /**
     * Retrieves all documents associated with a specific user.
     *
     * @param userId The ID of the user whose documents are to be retrieved.
     * @return A list of AddressProofDocuments associated with the user.
     * @throws CustomException if the user is not found or there is another error.
     */
    List<AddressProofDocument> getDocumentsByUser(String userId) throws CustomException;

    /**
     * Updates an existing document for the specified user.
     *
     * @param user         The user who owns the document.
     * @param documentId   The ID of the document to be updated.
     * @param file         The new file to be uploaded.
     * @param documentType The type of document being updated.
     * @return The updated AddressProofDocument.
     * @throws CustomException if the document is not found or there is another error.
     */
    AddressProofDocument updateDocument(User user, Long documentId, MultipartFile file, DocumentType documentType) throws CustomException;

    /**
     * Deletes a document for the specified user.
     *
     * @param user       The user who owns the document.
     * @param documentId The ID of the document to be deleted.
     * @throws CustomException if the document is not found or there is another error.
     */
    void deleteDocument(User user, Long documentId) throws CustomException;
}
