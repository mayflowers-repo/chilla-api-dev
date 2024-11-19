package com.mayflowertech.chilla.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.AddressProofDocument;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.enums.DocumentExtension;
import com.mayflowertech.chilla.enums.DocumentType;
import com.mayflowertech.chilla.repositories.IDocumentRepository;
import com.mayflowertech.chilla.services.GoogleCloudStorageService;
import com.mayflowertech.chilla.services.IDocumentService;
import com.mayflowertech.chilla.services.IUserService;

@Service
public class DocumentServiceImpl implements IDocumentService {
	 private static final Logger logger = LoggerFactory.getLogger(DocumentServiceImpl.class);
	 
	@Value("${file.upload-dir}")
	private String uploadDir;

    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;
    
	
    @Autowired
    private IDocumentRepository documentRepository;

    @Autowired
    private IUserService userService;
   
    @Override
    public AddressProofDocument addDocument(String userId, MultipartFile file, DocumentType documentType) throws CustomException {
        try {
            User user = userService.getById(userId);

            if (user == null) {
                throw new CustomException("Invalid user ID");
            }

            if (file.getSize() > 3145728) { // Check if file size exceeds 3 MB
                throw new CustomException("File size must not exceed 3 MB");
            }

            // Determine file extension
            String originalFilename = file.getOriginalFilename();
            DocumentExtension documentExtension = null;
            if (originalFilename != null) {
                String lowerCaseFilename = originalFilename.toLowerCase();
                if (lowerCaseFilename.endsWith(".pdf")) {
                    documentExtension = DocumentExtension.PDF;
                } else if (lowerCaseFilename.endsWith(".doc")) {
                    documentExtension = DocumentExtension.DOC;
                } else if (lowerCaseFilename.endsWith(".docx")) {
                    documentExtension = DocumentExtension.DOCX;
                } else if (lowerCaseFilename.endsWith(".jpeg") || lowerCaseFilename.endsWith(".jpg")) {
                    documentExtension = DocumentExtension.JPEG;
                } else if (lowerCaseFilename.endsWith(".png")) {
                    documentExtension = DocumentExtension.PNG;
                } else {
                    throw new CustomException("Unsupported file extension");
                }
            } else {
                throw new CustomException("Invalid file name");
            }
            
            originalFilename = userId + "_" +originalFilename;
            
            logger.info("originalFilename "+originalFilename);
          
            // Upload file to Google Cloud Storage
            String bucketName = "karuthal-docubucket1"; // Replace with your bucket name

            logger.info("starting service method");
            googleCloudStorageService.uploadObject(file, originalFilename);
            logger.info("saved");

            // Create and save the document metadata
            AddressProofDocument document = new AddressProofDocument();
            document.setUser(user);
            document.setDocumentType(documentType);
            document.setFilePath(originalFilename);
            document.setDocumentExtension(documentExtension);

            return documentRepository.save(document);
        } catch (Exception e) {
            throw new CustomException("Error while uploading the document to Google Cloud Storage", e);
        }
    }

    
    @Override
    public AddressProofDocument getDocumentById(Long documentId) throws CustomException {
        AddressProofDocument document = documentRepository.findById(documentId)
            .orElseThrow(() -> new CustomException("Document not found with ID: " + documentId));
        
        
        return document;
    }

    @Override
    public List<AddressProofDocument> getDocumentsByUser(String userId) throws CustomException {
        try {
        	User user = userService.getById(userId); 
        	if(user == null) {
        		  throw new CustomException("No  user with ID: " + userId);
        	}
            List<AddressProofDocument> documents = documentRepository.findByUserId(user.getId());
            if (documents.isEmpty()) {
                throw new CustomException("No documents found for user with ID: " + userId);
            }
            return documents;
        } catch (CustomException e) {
        	e.printStackTrace();
            throw new CustomException(e.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
            throw new CustomException("Error while retrieving documents for user with ID: " + userId);
        }
    }

    @Override
    public AddressProofDocument updateDocument(User user, Long documentId, MultipartFile file, DocumentType documentType) throws CustomException {
        AddressProofDocument document = getDocumentById(documentId);

        try {
            if (file.getSize() > 3145728) {  // Check if file size exceeds 3 MB
                throw new CustomException("File size must not exceed 3 MB");
            }

            // Set document type
            document.setDocumentType(documentType);

            // Generate a file path based on the user ID and document type
            String fileExtension = getFileExtension(file.getOriginalFilename());
            String filePath = "uploads/" + user.getId() + "/" + documentType + "_" + System.currentTimeMillis() + "." + fileExtension;

            // Save file to the filesystem
            File targetFile = new File(filePath);
            targetFile.getParentFile().mkdirs(); // Ensure the directory exists
            file.transferTo(targetFile);

            // Update document with the file path
            document.setFilePath(filePath);

            return documentRepository.save(document);
        } catch (IOException e) {
            throw new CustomException("Error while updating the document", e);
        }
    }

    private String getFileExtension(String filename) {
        return filename != null && filename.contains(".") ? filename.substring(filename.lastIndexOf(".") + 1) : "";
    }

    @Override
    public void deleteDocument(User user, Long documentId) throws CustomException {
        AddressProofDocument document = getDocumentById(documentId); // Ensure the document exists
        documentRepository.delete(document);
    }
}
