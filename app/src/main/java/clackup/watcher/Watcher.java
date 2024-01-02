package clackup.watcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.processing.FilerException;
 
public final class Watcher { 
    
    public static void watch(File folder) throws FilerException, IOException {

        if (!folder.isDirectory()) {
            throw new FilerException(folder + " is not a directory.");
        }

        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path directory = folder.toPath();
        WatchKey watchKey = directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

        Runnable helloRunnable = new Runnable() {
            public void run() {

                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                    Path fileName = pathEvent.context();
                    WatchEvent.Kind<?> kind = event.kind();

                    if (kind == StandardWatchEventKinds.ENTRY_CREATE || kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        BackgroundHandler.handleChange(folder, fileName);
                    }

                    if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
                        BackgroundHandler.handleDeletion(folder, fileName);
                    }

                }
                
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        executor.scheduleAtFixedRate(helloRunnable, 0, 30, TimeUnit.SECONDS);

    }

}
