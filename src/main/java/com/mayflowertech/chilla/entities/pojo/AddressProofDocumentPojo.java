package com.mayflowertech.chilla.entities.pojo;


import com.mayflowertech.chilla.enums.DocumentExtension;
import com.mayflowertech.chilla.enums.DocumentType;

public class AddressProofDocumentPojo {

    private Long id;
    private DocumentType documentType;
    private DocumentExtension documentExtension;
    private String userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public DocumentExtension getDocumentExtension() {
        return documentExtension;
    }

    public void setDocumentExtension(DocumentExtension documentExtension) {
        this.documentExtension = documentExtension;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
