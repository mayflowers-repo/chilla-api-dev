package com.mayflowertech.chilla.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import com.mayflowertech.chilla.enums.DocumentExtension;
import com.mayflowertech.chilla.enums.DocumentType;

@Entity
@Table(name = "documents")
public class AddressProofDocument {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "document_type", nullable = false)
	private DocumentType documentType;

	   @Column(name = "file_path", nullable = false)
	    private String filePath;
	   
	   

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

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


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "document_extn", nullable = false)
	private DocumentExtension documentExtension;

	public DocumentExtension getDocumentExtension() {
		return documentExtension;
	}

	public void setDocumentExtension(DocumentExtension documentExtension) {
		this.documentExtension = documentExtension;
	}

	
	
}
