package clackup.watcher;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import clackup.azure.AzureUpload;

public class BackgroundHandler {

    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void handleChange(File folder, Path fileName) {
        executorService.submit(() -> {
            boolean ret = AzureUpload.uploadBlob(folder, fileName.toString());
            if (ret) {
                System.out.println("File " + fileName + " uploaded successfully");
            } else {
                System.out.println("File " + fileName + " upload failed");
                if (new File(folder, fileName.toString()).exists()) {
                    BackgroundHandler.handleChange(folder, fileName);
                } else {
                    System.out.println("File " + fileName + " is missing.");
                }
            }
        });
    }

    public static void handleDeletion(File folder, Path fileName) {
        AzureUpload.deleteBlob(folder, fileName.toString());
    }
    
}
