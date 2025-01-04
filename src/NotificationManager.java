import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class NotificationManager {
    private static HBox notificationArea;

    // Initialize the NotificationManager with the notification area
    public static void initialize(HBox notificationBox) {
        notificationArea = notificationBox;
    }

    // Show a notification
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
