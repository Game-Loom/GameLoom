/**
 * The NotificationManager class provides a centralized mechanism for displaying system status messages 
 * to the user in the form of notifications within the application. Notifications are intended to convey 
 * success, error, or informational messages in a consistent, non-intrusive manner.
 * 
 * This class supports:
 * - Displaying styled notifications based on the type (e.g., success, error, or info).
 * - Automatically hiding notifications after a set duration (default is 3 seconds).
 * - Modular integration with various parts of the application by initializing the notification area 
 *   with a reusable HBox.
 * 
 * Key functionalities include:
 * - Displaying notifications with distinct text colors for different types: success (Green), error (Red), info (Blue).
 * - Managing the visibility and clearing of the notification area after a defined duration (3 sec).
 * - Ensuring notifications do not block the application's main thread using `Platform.runLater`.
 * 
 * Example Usage:
 *     - Initialize the NotificationManager with a shared HBox:
 *     NotificationManager.initialize(notificationArea);
 *     
 *     - Show a success notification:
 *     NotificationManager.showNotification("Game saved successfully!", "success");
 *     
 *     - Show an error notification:
 *     NotificationManager.showNotification("Failed to delete the game.", "error");
 * 
 * Note: The NotificationManager must be initialized with an HBox before showing any notifications.
 * Failure to do so will result in an IllegalStateException.
 * 
 * @author GameLoom Team
 * @version 1.0
 */

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class NotificationManager {
    /**
     * The shared HBox container for displaying notifications.
     * This area is initialized once using the initialize(HBox) method and reused across the application.
     */
    private static HBox notificationArea;


    /**
     * Initializes the NotificationManager with a reusable HBox that serves as the container for notifications.
     * 
     * This method must be called before using showNotification(String, String) to ensure the notification 
     * area is available. Failure to initialize will result in an IllegalStateException.
     * 
     * @param notificationBox The HBox container where notifications will be displayed.
     */
    public static void initialize(HBox notificationBox) {
        notificationArea = notificationBox;
    }

    
    /**
     * Displays a notification with the specified message and type in the initialized notification area.
     * The notification is styled based on the provided type and automatically disappears after 3 seconds.
     * 
     * Supported notification types:
     *   success: Displays the message in green with bold styling.
     *   error: Displays the message in red with bold styling.
     *   info: Displays the message in blue with bold styling.
     * 
     * Example Usage:
     *     NotificationManager.showNotification("Game saved successfully!", "success");
     * 
     * @param message The text of the notification to display.
     * @param type    The type of notification (success, error, or info). Defaults to info if not recognized.
     * 
     * @throws IllegalStateException If the NotificationManager has not been initialized using {@link #initialize(HBox)}.
     */
    public static void showNotification(String message, String type) {
        if (notificationArea == null) {
            throw new IllegalStateException("NotificationManager is not initialized with a notification area.");
        }

        // Create a notification label
        Label notification = new Label(message);

        // Apply styles based on the notification type
        switch (type.toLowerCase()) {
            case "success":
                notification.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                break;
            case "error":
                notification.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                break;
            case "info":
            default:
                notification.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");
                break;
        }

        // Clear existing children, show the notification, and set it visible
        Platform.runLater(() -> {
            notificationArea.getChildren().clear();
            notificationArea.getChildren().add(notification);
            notificationArea.setVisible(true);
        });

        // Schedule the notification to disappear after 3 seconds
        new Thread(() -> {
            try {
                Thread.sleep(3000); // Wait for 3 seconds
                Platform.runLater(() -> {
                    notificationArea.getChildren().clear();
                    notificationArea.setVisible(false); // Hide the area when done
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
