package clackup;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;

import clackup.watcher.Watcher;

public class App {
    private static final SystemTray tray = SystemTray.getSystemTray();
    private static final PopupMenu popup = new PopupMenu();
    private static TrayIcon trayIcon;

    public static void main(String[] args) throws Exception {

        System.out.println("aaaa");

        if (!SystemTray.isSupported()) {
            // SystemTray is not supported
        }

        trayIcon = new TrayIcon(createImage("icon.ico", "tray icon"));
        trayIcon.setImageAutoSize(true);

        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MenuItem item = (MenuItem) e.getSource();

                String s = (String) JOptionPane.showInputDialog(null, "Report "
                        + item.getLabel(), "Create Report",
                        JOptionPane.PLAIN_MESSAGE, null, null, "");

                // Do something with the string...
            }
        };
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });


        Menu reportMenu = new Menu("Report");
        MenuItem menuItem = new MenuItem("Item");
        reportMenu.add(menuItem);
        menuItem.addActionListener(listener);
        popup.add(reportMenu);
        popup.add(exitItem);
        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            // TrayIcon could not be added
        }

        Watcher.watch(new File("/Users/jgarcia/Downloads/"));

    }

    // Obtain the image URL
    protected static Image createImage(String path, String description)  {
        InputStream a = App.class.getClassLoader().getResourceAsStream(path);
        try {
            IOUtils.copy(a, new FileOutputStream("/tmp/a")); 
        } catch(Exception e) {
            e.printStackTrace();
        }

        URL imageURL = App.class.getClassLoader().getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
}