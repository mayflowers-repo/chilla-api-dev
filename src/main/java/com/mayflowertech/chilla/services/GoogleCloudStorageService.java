package com.mayflowertech.chilla.services;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.mayflowertech.chilla.config.Constants;

@Service
public class GoogleCloudStorageService {

	 private static final Logger logger = LoggerFactory.getLogger(GoogleCloudStorageService.class);
	 
	@Value("${google.storage.json.file}")
	private String jsonKeyFile;
	
    private Storage storage ;
    
    

    @PostConstruct
    public void initializeStorage() throws IOException {
        logger.info("Loading Google Cloud Storage credentials from " + jsonKeyFile);

        // Set the system property for the Google credentials file
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", jsonKeyFile);

        // Load credentials and initialize the storage instance
        storage = StorageOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(jsonKeyFile)))
                .build()
                .getService();
    }


    public  void uploadObject(MultipartFile file, String fileName) {
        try {
        	logger.info("filePath is "+fileName);
            
            // Define the blob information (the object in the bucket)
            BlobInfo blobInfo = BlobInfo.newBuilder(Constants.GCP_BUCKET_NAME, fileName).build();
            
            logger.info("bobInfo created");
            // Upload the file
            storage.create(blobInfo, file.getBytes());
            
            logger.info("storage succ");

            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error uploading file: " + e.getMessage());
        }
    }
    
    
    public byte[] downloadObject(String fileName) {
        try {
            logger.info("Downloading file: " + fileName);

            // Define the blob ID using the bucket and file name
            BlobId blobId = BlobId.of(Constants.GCP_BUCKET_NAME, fileName);

            // Get the blob object
            Blob blob = storage.get(blobId);

            if (blob == null) {
                logger.error("File not found in the bucket: " + fileName);
                return null;
            }

            // Return the content of the blob as a byte array
            byte[] content = blob.getContent();
            logger.info("File downloaded successfully.");

            return content;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error downloading file: " + e.getMessage());
            return null;
        }
    }
    
}
