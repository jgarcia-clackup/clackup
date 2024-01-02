package clackup.azure;

import java.io.File;
import java.io.UncheckedIOException;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

public class AzureUpload {


    static BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
        .connectionString("DefaultEndpointsProtocol=https;AccountName=clackupstoragetest;AccountKey=NbqKa8bmQGhLUY2eg3jupIGQiFKOHoEz7lumSeTwx6oARgKRL9VS7hp2od6570vtKaBiKbxpghzS+AStM+LclQ==;EndpointSuffix=core.windows.net")
        .buildClient();

    static BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient("data");

    public static void getBlob(String fileName) {

        BlobClient blobClient = blobContainerClient.getBlobClient(fileName);
        blobClient.downloadToFile("/tmp/"+fileName);
    }

    public static boolean uploadBlob(File folder, String fileName) {

        File file = new File(folder, fileName);

        BlobClient blobClient = blobContainerClient.getBlobClient(fileName.toString());
        try {
            System.out.println("Uploading file " + file + " to " + blobClient);
            blobClient.uploadFromFile(file.toString(), true);
            System.out.println("Uploaded file " + file + " to " + blobClient);
        } catch (UncheckedIOException ioe) {
            return false;
        }

        return true;

    }

    public static boolean deleteBlob(File folder, String fileName) {

        File file = new File(folder, fileName);

        BlobClient blobClient = blobContainerClient.getBlobClient(fileName.toString());
        try {
            System.out.println("Marking file " + file + " for deletion in 5 seconds");
            try {
                Thread.sleep(5000);
                if (!file.exists()) {
                    blobClient.deleteIfExists();
                    System.out.println("File " + file + " marked for deletion");
                } else {
                    System.out.println("File " + file + " is still there, so not deleting");
                }
            } catch (Throwable t) {
            }
        } catch (UncheckedIOException ioe) {
            return false;
        }

        return true;

    }
    
}
