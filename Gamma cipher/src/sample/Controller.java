package sample;

import javafx.fxml.FXML;

public class Controller {
    private static String keyString;
    private static String message;

    @FXML
    void init() {

        message = text.getText().toLowerCase();
        keyString = keyField.getText();
        int a = 3, b = 2, m = 40692;
        int currentKey = Integer.parseInt(keyString);
        char[] result = new char[message.length()];

        for (int i = 0; i < message.length(); i++) {
            result[i] = (char) (message.toCharArray()[i] ^ currentKey);
            currentKey = (currentKey * a + b) % m;
        }

    }

}
