package com.parjan.storage.azure.azuretablestorageblob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobContainerItem;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.microsoft.azure.storage.blob.*;
import com.microsoft.azure.storage.blob.models.*;
import java.io.*;
import java.util.Iterator;

@SpringBootApplication

public class AzureTableStorageBlobApplication {

	private static final String CONTAINER = (String)System.getenv("CONTAINER");
	private static final String STORAGE_ACCOUNT_URL = (String)System.getenv("STORAGE_ACCOUNT_URL");
	private static final String SAS_TOKEN = (String)System.getenv("SAS_TOKEN");

	public static void main(String[] args) {
		SpringApplication.run(AzureTableStorageBlobApplication.class, args);
		performOperationsOnStorageBlob();
		listContainers();
		uploadToStorageBlob();
		uploadImageToStorageBlob();
	}

	
	public static void listContainers() {
		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
				.endpoint(STORAGE_ACCOUNT_URL)
				.sasToken(SAS_TOKEN)
				.buildClient();
		 
		PagedIterable<BlobContainerItem> listBlobContainers = blobServiceClient.listBlobContainers();
		Iterator<BlobContainerItem> iterator = listBlobContainers.iterator();
		while(iterator.hasNext()) {
			BlobContainerItem next = iterator.next();
			System.out.println( "Container Name " + next.getName());
		}

	}
	
	
	public static void uploadToStorageBlob() {
		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
				.endpoint(STORAGE_ACCOUNT_URL)
				.sasToken(SAS_TOKEN)
				.buildClient();
		BlobContainerClient createBlobContainer = blobServiceClient.getBlobContainerClient(CONTAINER);
		
		BlockBlobClient blockBlobClient = createBlobContainer.getBlobClient("/data.txt").getBlockBlobClient();
		String dataSample = "This is sample text and should be written on file.";
		try (ByteArrayInputStream dataStream = new ByteArrayInputStream(dataSample.getBytes())) {
		    blockBlobClient.upload(dataStream, dataSample.length());
		} catch (IOException e) {
		    e.printStackTrace();
		}
 
		}
	
	
	public static void uploadImageToStorageBlob() {
		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
				.endpoint(STORAGE_ACCOUNT_URL)
				.sasToken(SAS_TOKEN)
				.buildClient();
		BlobContainerClient createBlobContainer = blobServiceClient.getBlobContainerClient(CONTAINER);
		
		BlobClient  blockBlobClient = createBlobContainer.getBlobClient("images/images.jpg");
		blockBlobClient.uploadFromFile("C:\\Users\\15083\\Pictures\\Saved Pictures\\images.jpg");
 
		}	
	
	
	public static void performOperationsOnStorageBlob() {
		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
				.endpoint(STORAGE_ACCOUNT_URL)
				.sasToken(SAS_TOKEN)
				.buildClient();
		blobServiceClient.createBlobContainer(CONTAINER);
 
		}
	 
}
