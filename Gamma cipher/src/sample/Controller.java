package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Controller {
    private static String keyString;
    private static String message;
    private static final int A = 1664525;
    private static final int B = 2;
    private static final int M = 1664525;

    @FXML
    private TextArea text;

    @FXML
    private Button encrypt;

    @FXML
    private TextField keyField;

    @FXML
    void initialize() {
        encrypt.setOnAction(event -> {
            message = text.getText();
            keyString = keyField.getText();
            if (keyString.length() > 0 && keyString.matches("[-+]?\\d+") && keyString.length() < 19) {
                long currentKey = Long.parseLong(keyString);
                StringBuilder encodeDecode = new StringBuilder();
                char[] chars = message.toCharArray();
                for (int i = 0; i < message.length(); i++) {
                    encodeDecode.append((char) (chars[i] ^ currentKey));
                    currentKey = (currentKey * A + B) % M;
                }
                text.setText(encodeDecode.toString());
            } else {
                alert();
            }
        });
    }

    @FXML
    void open() {
        FileChooser fileChooser = new FileChooser();
        File fileSelected = fileChooser.showOpenDialog(null);
        StringBuilder text1 = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileSelected), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                text1.append(line);
            }
            br.close();
            text.setText(text1.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void save() {
        FileChooser fileChooser = new FileChooser();
        File fileSave = fileChooser.showSaveDialog(null);
        if (fileSave != null) {
            try {
                Writer out = new OutputStreamWriter(new FileOutputStream(fileSave), StandardCharsets.UTF_8);
                out.write(text.getText());
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void alert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("The key field cannot be empty and must only contain numbers");
        alert.setContentText("Enter the key");
        alert.showAndWait();
    }
}
