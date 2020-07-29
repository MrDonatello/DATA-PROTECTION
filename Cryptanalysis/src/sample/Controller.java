package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;

public class Controller {

    private static String alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    private static String keyString;
    private static String message;
    private Key key = new Key(alphabet);

    @FXML
    private TextArea text;

    @FXML
    private Button selection;

    @FXML
    private ChoiceBox keyChoiceBox;

    @FXML
    void initialize() {
        selection.setOnAction(event -> {
            StringBuilder encode = new StringBuilder();
            message = text.getText();

            String keyString = key.getKey(message);
            System.out.println("key length is " + keyString);
           /* keyString = keyField.getText();
            index = 0;
            if (keyString.length() != 0) {
                for (char symbol : message.toCharArray()) {
                    if (index == keyString.toCharArray().length) {
                        index = 0;
                    }
                    encode.append(alphabet.charAt((alphabet.indexOf(symbol) + alphabet.indexOf(keyString.charAt(index))) % alphabet.length()));
                    index++;
                }
                text.setText(encode.toString());
            } else {
                alert();
            }*/
        });
    }

    @FXML
    void open() {
        FileChooser fileChooser = new FileChooser();
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
    }

    @FXML
    void save() {
        FileChooser fileChooser = new FileChooser();
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
    }

    private void alert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("");
        alert.setContentText("");
        alert.showAndWait();
    }
}

