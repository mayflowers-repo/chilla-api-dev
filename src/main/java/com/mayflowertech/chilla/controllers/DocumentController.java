package com.mayflowertech.chilla.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mayflowertech.chilla.config.JacksonFilterConfig;
import com.mayflowertech.chilla.config.custom.CustomException;
import com.mayflowertech.chilla.entities.AddressProofDocument;
import com.mayflowertech.chilla.entities.ApiResult;
import com.mayflowertech.chilla.entities.User;
import com.mayflowertech.chilla.entities.pojo.AddressProofDocumentPojo;
import com.mayflowertech.chilla.enums.DocumentExtension;
import com.mayflowertech.chilla.enums.DocumentType;
import com.mayflowertech.chilla.services.GoogleCloudStorageService;
import com.mayflowertech.chilla.services.IDocumentService;
import com.mayflowertech.chilla.services.IUserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/karuthal/api/v1/documents")
public class DocumentController {
    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    private IDocumentService documentService;

    @Autowired
    private IUserService userService;

    @Autowired
    private JacksonFilterConfig jacksonFilterConfig;
    
    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResult<AddressProofDocumentPojo> uploadDocument(
            @RequestParam("userId") String userId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("documentType") DocumentType documentType) {
        logger.info("uploading a doc");
        try {
            AddressProofDocument document = documentService.addDocument(userId, file, documentType);
            logger.info("Uploaded file size: " + file.getSize());
            AddressProofDocumentPojo pojo = convertToPojo(document);
            return new ApiResult<>(HttpStatus.OK.value(), "Document uploaded successfully", pojo);
        } catch (CustomException e) {
            e.printStackTrace();
            return new ApiResult<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
        }
    }
    
    private AddressProofDocumentPojo convertToPojo(AddressProofDocument document) {
        AddressProofDocumentPojo pojo = new AddressProofDocumentPojo();
        pojo.setId(document.getId());
        pojo.setDocumentType(document.getDocumentType());
        pojo.setDocumentExtension(document.getDocumentExtension());
        pojo.setDocumentName(document.getFilePath());
        pojo.setUserId(document.getUser().getId().toString()); 
        return pojo;
    }

    @GetMapping("/users/{userId}/documents")
    public ApiResult<List<AddressProofDocumentPojo>> getUserDocuments(@PathVariable String userId) {
        logger.info("fetching docs for user " + userId);
        try {
            List<AddressProofDocument> documents = documentService.getDocumentsByUser(userId);
            if (documents == null || documents.isEmpty()) {
                throw new CustomException("No documents found for the specified user ID.");
            }
            List<AddressProofDocumentPojo> documentPojos = documents.stream()
                    .map(this::convertToPojo)
                    .collect(Collectors.toList());
            jacksonFilterConfig.applyFilters("UserFilter", "id", "email");
            return new ApiResult<>(HttpStatus.OK.value(), "Documents retrieved successfully", documentPojos);
        } catch (CustomException e) {
            return new ApiResult<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred", null);
        } finally {
            jacksonFilterConfig.clearFilters();
        }
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<Resource> getDocument(@PathVariable Long documentId) {
        try {
            AddressProofDocument document = documentService.getDocumentById(documentId);
            if (document == null || document.getFilePath() == null) {
                throw new CustomException("Document not found");
            }
            byte[] fileContent = googleCloudStorageService.downloadObject(document.getFilePath());
            Resource resource = new ByteArrayResource(fileContent);
            String contentType = determineContentType(document.getDocumentExtension());
            String fileExtension = determineFileExtension(document.getDocumentExtension());
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=download_file" + fileExtension);
            headers.setContentType(MediaType.parseMediaType(contentType));
            return ResponseEntity.ok().headers(headers).contentLength(fileContent.length).body(resource);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    private String determineContentType(DocumentExtension extension) {
        switch (extension) {
            case PDF: return "application/pdf";
            case DOC: return "application/msword";
            case DOCX: return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case JPEG:
            case JPG: return "image/jpeg";
            case PNG: return "image/png";
            default: return "application/octet-stream";
        }
    }
    
    private String determineFileExtension(DocumentExtension extension) {
        switch (extension) {
            case PDF: return ".pdf";
            case DOC: return ".doc";
            case DOCX: return ".docx";
            case JPEG:
            case JPG: return ".jpg";
            case PNG: return ".png";
            default: return ".bin";
        }
    }
}
