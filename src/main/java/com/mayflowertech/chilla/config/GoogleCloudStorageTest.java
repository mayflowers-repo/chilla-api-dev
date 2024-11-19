package com.mayflowertech.chilla.config;


import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;


public class GoogleCloudStorageTest {

    public static void main(String[] args) {
        // Set the environment variable programmatically for testing
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", "D:\\work\\chilla\\karuthal\\uploads\\chilla-karuthal-4336b3d6a3f1.json");

        String bucketName = "karuthal-docubucket1"; // Replace with your bucket name
        String objectName = "D:\\work\\chilla\\karuthal\\uploads\\membershipform.pdf"; // File name to upload in the bucket
        String filePath = "D:\\work\\chilla\\karuthal\\uploads\\chilla-karuthal-4336b3d6a3f1.json"; // Path to a local file for testing

        uploadObject(bucketName, objectName, filePath);
    }

    public static void uploadObject(String bucketName, String objectName, String filePath) {
        try {
            
            Storage storage = StorageOptions.newBuilder()
            	    .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(filePath)))
            	    .build()
            	    .getService();

            
            // Read the file content to upload
            Path path = Paths.get(filePath);
            byte[] content = Files.readAllBytes(path);
            System.out.println("File "+content.length);
            
            // Define the blob information (the object in the bucket)
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, objectName).build();

            // Upload the file
            storage.create(blobInfo, content);
            System.out.println("File uploaded to bucket " + bucketName + " as " + objectName);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error uploading file: " + e.getMessage());
        }
    }
}
