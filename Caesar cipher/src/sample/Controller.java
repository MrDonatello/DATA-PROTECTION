package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.*;

public class Controller {

    private static String alphabet = "!@#$%^&*()`_+{}:|/<>№;%*-=+~?., abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЬЫЪЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    private static int key;
    private static String message;

    @FXML
    private TextArea text;

    @FXML
    private Button encrypt;

    @FXML
    private Button decrypt;

    @FXML
    private TextField keyField;

    @FXML
    private Button open;

    @FXML
    private Button save;

    @FXML
    void initialize() {
        encrypt.setOnAction(event -> {
            StringBuilder encode = new StringBuilder();
            message = text.getText();
            if (keyField.getText().matches("[\\d]+") && keyField.getText().length() < 10) {
                key = Integer.parseInt(keyField.getText());
                for (char symbol : message.toCharArray()) {
                    encode.append(alphabet.charAt((alphabet.indexOf(symbol) + key) % 160));
                }
                text.setText(encode.toString());
            } else {
                alert();
            }
        });

        decrypt.setOnAction(event -> {
            StringBuilder decode = new StringBuilder();
            message = text.getText();
            if (keyField.getText().matches("[\\d]+") && keyField.getText().length() < 10) {
                key = Integer.parseInt(keyField.getText()) % 160;
                for (char symbol : message.toCharArray()) {
                    decode.append(alphabet.charAt((alphabet.indexOf(symbol) - key + 160) % 160));
                }
                text.setText(decode.toString());
            } else {
                alert();
            }
        });

        open.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt", "*.txt"), new FileChooser.ExtensionFilter("ALL Files", "*.*"));
            File fileSelected = fileChooser.showOpenDialog(null);
            StringBuilder text1 = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileSelected), "Cp1251"));
                String line;
                while ((line = br.readLine()) != null) {
                    text1.append(line);
                }
                br.close();
                text.setText(text1.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        save.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("txt", "*.txt"));
            File fileSave = fileChooser.showSaveDialog(null);
            if (fileSave != null) {
                try {
                    Writer out = new OutputStreamWriter(new FileOutputStream(fileSave), "Cp1251");
                    out.write(text.getText());
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void alert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("The key field must meet the following requirements:\n 1. Cannot be empty\n 2. Contain no characters other than numbers\n 3. The key length is no more than 9 characters");
        alert.setContentText("Enter the key");
        alert.showAndWait();
    }
}
