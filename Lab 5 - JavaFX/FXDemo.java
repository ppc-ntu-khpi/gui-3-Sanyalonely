package fxdemo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import com.mybank.domain.Bank;
import com.mybank.domain.Customer;
import com.mybank.domain.Account;
import com.mybank.data.DataSource;

public class FxDemo extends Application {

    private ComboBox<String> jComboBox1;
    private Button buttonShow;
    private Button buttonAbout;
    private TextArea textAreaOutput;

    @Override
    public void start(Stage primaryStage) {
        try {
            java.io.File file = new java.io.File("test.dat");
            if (file.exists()) {
                DataSource ds = new DataSource("test.dat");
                ds.loadData();
            }
        } catch (Exception e) {
        }

        primaryStage.setTitle("FxDemo Bank");
        primaryStage.setResizable(false);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        ObservableList<String> customerNames = FXCollections.observableArrayList();
        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            Customer customer = Bank.getCustomer(i);
            customerNames.add(customer.getFirstName() + " " + customer.getLastName());
        }

        jComboBox1 = new ComboBox<>(customerNames);
        jComboBox1.setPrefWidth(200);

        buttonShow = new Button("Show");
        buttonAbout = new Button("About");

        textAreaOutput = new TextArea();
        textAreaOutput.setEditable(false);
        textAreaOutput.setPrefHeight(150);

        HBox topBox = new HBox(10);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(0, 0, 15, 0));
        topBox.getChildren().addAll(jComboBox1, buttonShow);

        HBox bottomBox = new HBox();
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setPadding(new Insets(15, 0, 0, 0));
        bottomBox.getChildren().add(buttonAbout);

        root.setTop(topBox);
        root.setCenter(textAreaOutput);
        root.setBottom(bottomBox);

        buttonShow.setOnAction(evt -> buttonShowActionPerformed());
        buttonAbout.setOnAction(evt -> buttonAboutActionPerformed());

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void buttonShowActionPerformed() {
        int selectedIndex = jComboBox1.getSelectionModel().getSelectedIndex();
        
        if (selectedIndex >= 0) {
            Customer customer = Bank.getCustomer(selectedIndex);
            
            StringBuilder info = new StringBuilder();
            info.append("Клієнт: ").append(customer.getFirstName()).append(" ").append(customer.getLastName()).append("\n");
            info.append("Кількість рахунків: ").append(customer.getNumberOfAccounts()).append("\n");
            info.append("--------------------------------------\n");
            
            for (int i = 0; i < customer.getNumberOfAccounts(); i++) {
                Account account = customer.getAccount(i);
                info.append("Рахунок №").append(i + 1).append(": Баланс = $").append(account.getBalance()).append("\n");
            }
            
            textAreaOutput.setText(info.toString());
        }
    }

    private void buttonAboutActionPerformed() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText(null);
        alert.setContentText("Програма: FxDemo Bank\nРозробник: Олександр Пилипенко\nГрупа: 34\nРік: 2026");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        FxMainLauncher.main(args);
    }
}

class FxMainLauncher {
    public static void main(String[] args) {
        Application.launch(FxDemo.class, args);
    }
}
