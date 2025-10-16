package com.example.todolistsf;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.concurrent.Task;

public class MainController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void goToCreatePage() {
        try {
            // Load the create.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("create.fxml"));
            AnchorPane root = loader.load();

            // Set the new scene with the loaded FXML
            Scene scene = new Scene(root);
            Stage stage = (Stage) welcomeText.getScene().getWindow(); // Get the current stage
            stage.setScene(scene); // Change the scene to the new one

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void goToRemovePage() {
        try {
            // Load the create.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("remove.fxml"));
            AnchorPane root = loader.load();

            // Set the new scene with the loaded FXML
            Scene scene = new Scene(root);
            Stage stage = (Stage) welcomeText.getScene().getWindow(); // Get the current stage
            stage.setScene(scene); // Change the scene to the new one

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void goToSortPage() {
        try {
            // Load the create.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sort.fxml"));
            AnchorPane root = loader.load();

            // Set the new scene with the loaded FXML
            Scene scene = new Scene(root);
            Stage stage = (Stage) welcomeText.getScene().getWindow(); // Get the current stage
            stage.setScene(scene); // Change the scene to the new one

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void goToSearchPage() {
        try {
            // Load the create.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("search.fxml"));
            AnchorPane root = loader.load();

            // Set the new scene with the loaded FXML
            Scene scene = new Scene(root);
            Stage stage = (Stage) welcomeText.getScene().getWindow(); // Get the current stage
            stage.setScene(scene); // Change the scene to the new one

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void goToManagePage() {
        try {
            // Load the create.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("manage.fxml"));
            AnchorPane root = loader.load();

            // Set the new scene with the loaded FXML
            Scene scene = new Scene(root);
            Stage stage = (Stage) welcomeText.getScene().getWindow(); // Get the current stage
            stage.setScene(scene); // Change the scene to the new one

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void goToEditPage() {
        try {
            // Load the create.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("edit.fxml"));
            AnchorPane root = loader.load();

            // Set the new scene with the loaded FXML
            Scene scene = new Scene(root);
            Stage stage = (Stage) welcomeText.getScene().getWindow(); // Get the current stage
            stage.setScene(scene); // Change the scene to the new one

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void goToDataPage() throws MessagingException {
        try {
            // Load the create.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("data.fxml"));
            AnchorPane root = loader.load();

            // Set the new scene with the loaded FXML
            Scene scene = new Scene(root);
            Stage stage = (Stage) welcomeText.getScene().getWindow(); // Get the current stage
            stage.setScene(scene); // Change the scene to the new one

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void goToDependencyPage() {
        try {
            // Load the create.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dependency.fxml"));
            AnchorPane root = loader.load();

            // Set the new scene with the loaded FXML
            Scene scene = new Scene(root);
            Stage stage = (Stage) welcomeText.getScene().getWindow(); // Get the current stage
            stage.setScene(scene); // Change the scene to the new one

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StringBuilder msg = new StringBuilder();
        String selectAllSQL = "SELECT * FROM tasks WHERE dueDate IS NOT NULL";
        String selectRecurringSQL = "SELECT * FROM tasks WHERE recurrence != 'None'";

        try (Connection connection = Cnx.getConnection();
                PreparedStatement allStmt = connection.prepareStatement(selectAllSQL);
                PreparedStatement recurringStmt = connection.prepareStatement(selectRecurringSQL);
                ResultSet allRs = allStmt.executeQuery();
                ResultSet recurringRs = recurringStmt.executeQuery()) {

            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Check tasks due today or tomorrow
            while (allRs.next()) {
                String title = allRs.getString("title");
                String dueDateString = allRs.getString("dueDate");

                if (dueDateString != null) {
                    LocalDate taskDate = LocalDate.parse(dueDateString, formatter);
                    if (!taskDate.isBefore(today) && taskDate.isBefore(tomorrow.plusDays(1))) {
                        System.out.println(title + " due already");
                        msg.append("The task '").append(title).append("' is due or going to be due within 24 hours!\n");
                    }
                }
            }
            if (String.valueOf(msg).isEmpty()) {

            } else {
                sendEmail(String.valueOf(msg));
            }

            // Handle recurring tasks
            while (recurringRs.next()) {
                String rcc = recurringRs.getString("recurrence");
                String rccDateString = recurringRs.getString("rccDate");
                LocalDate rccDate = LocalDate.parse(rccDateString);

                String title = recurringRs.getString("title");
                String description = recurringRs.getString("description");
                String date;
                String category = recurringRs.getString("category");
                String priority = recurringRs.getString("priority");
                String id = recurringRs.getString("no");
                String rccDatetoSave = String.valueOf(today);

                switch (rcc) {
                    case "Daily":
                        if (ChronoUnit.DAYS.between(rccDate, today) >= 1) {
                            date = today.plusDays(1).toString();
                            createNewTask(title, description, date, category, priority, rcc, id, rccDatetoSave);
                        }
                        break;

                    case "Weekly":
                        if (ChronoUnit.WEEKS.between(rccDate, today) >= 1) {
                            date = today.plusWeeks(1).toString();
                            createNewTask(title, description, date, category, priority, rcc, id, rccDatetoSave);
                        }
                        break;

                    case "Monthly":
                        if (ChronoUnit.MONTHS.between(rccDate, today) >= 1) {
                            date = today.plusMonths(1).toString();
                            createNewTask(title, description, date, category, priority, rcc, id, rccDatetoSave);
                        }
                        break;

                    default:
                        System.out.println("Unknown recurrence type: " + rcc);
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void sendEmail(String messages) {
        // Create a task to run the email sending process in the background
        Task<Void> sendEmailTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Email sending logic
                Properties properties = new Properties();
                String recipientEmail = ""; // Your recipient email
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");

                String myEmail = ""; // Your email
                String password = ""; // Use an app password (not your Gmail password)

                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(myEmail, password);
                    }
                });

                Message message = prepareMessage(session, myEmail, recipientEmail, messages);

                if (message != null) {
                    try {
                        // Send the email in the background
                        Transport.send(message);
                        // Show a success alert on the JavaFX thread
                        javafx.application.Platform.runLater(() -> {
                            new Alert(Alert.AlertType.INFORMATION, "Send Email Successfully").show();
                        });
                    } catch (MessagingException e) {
                        // Handle email sending failure
                        javafx.application.Platform.runLater(() -> {
                            new Alert(Alert.AlertType.ERROR, "Failed to send email. Please try again.").show();
                        });
                    }
                } else {
                    // Message preparation failure
                    javafx.application.Platform.runLater(() -> {
                        new Alert(Alert.AlertType.ERROR, "Failed to prepare the email message. Please try again.")
                                .show();
                    });
                }
                return null;
            }
        };

        // Start the task in a new thread
        new Thread(sendEmailTask).start();
    }

    private Message prepareMessage(Session session, String myAccountEmail, String recipientEmail, String msg) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[] {
                    new InternetAddress(recipientEmail)
            });
            message.setSubject("Oops! Complete them before Due Date!");
            message.setText(msg);
            return message;
        } catch (Exception e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }
        return null; // Return null in case of error
    }

    static void createNewTask(String title, String description, String date, String category, String priority,
            String recurrence, String dependency, String rccDatetoSave) {
        String completion = "Incomplete";
        // SQL query
        String insertSQL = "INSERT INTO tasks (title,description,dueDate,category,priority,completion, recurrence, dependencies,rccDate) VALUES(?,?,?,?,?,?,?,?,?)";

        // Insert Data
        try (Connection connection = Cnx.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            // Set parameters for the query
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);
            preparedStatement.setDate(3, java.sql.Date.valueOf(date));
            preparedStatement.setString(4, category);
            preparedStatement.setString(5, priority);
            preparedStatement.setString(6, completion);
            preparedStatement.setString(7, recurrence);
            preparedStatement.setString(8, dependency);
            preparedStatement.setString(9, rccDatetoSave);
            int rowsAffected = preparedStatement.executeUpdate();
            // Print log if no error
            System.out.print("\nTask \"" + title + "\" added successfully!");
        } catch (SQLException e) {
            // Catch any error
            System.out.println("Error inserting data: " + e.getMessage());
        }
    }
}
