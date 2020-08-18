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
    private final int sizeOfBlock = 128; // размер блока 128 бит
    private final int sizeOfChar = 16; //размер одного символа (in Unicode 16 bit)
    private final int shiftKey = 2; //сдвиг ключа
    private final int quantityOfRounds = 16; //количество раундов
    private String[] blocks; //блоки в двоичном формате

    @FXML
    private TextArea text;

    @FXML
    private Button encrypt;

    @FXML
    private Button decrypt;

    @FXML
    private TextField keyField;

    @FXML
    void initialize() {
        encrypt.setOnAction(event -> {
            StringBuilder result = new StringBuilder();
            message = text.getText();
            keyString = keyField.getText();
            if (keyString.length() > 0) {//&& keyString.matches("[-+]?\\d+") && keyString.length() < 19) {
                message = stringToRightLength(message);
                cutStringIntoBlocks(message);
                keyString = correctKeyWord(keyString, message.length() / (2 * blocks.length));
                keyString = stringToBinaryFormat(keyString);
                for (int i = 0; i < quantityOfRounds; i++) {
                    for (int j = 0; j < blocks.length; j++) {
                        blocks[j] = encodeOneRound(blocks[j], keyString);
                    }
                    keyString = keyToNextRound(keyString);
                }
                keyString = keyToPrevRound(keyString);

                for (String block : blocks) {
                    result.append(block);
                }
                String result1 = stringFromBinaryToNormalFormat(result.toString());
                text.setText(result1);
            } else {
                alert();
            }
        });

        decrypt.setOnAction(event -> {
            StringBuilder result = new StringBuilder();
            message = text.getText();
            keyString = keyField.getText();
            if (keyString.length() > 0) {// && keyString.matches("[-+]?\\d+") && keyString.length() < 19) {
                keyString = stringToBinaryFormat(keyString);
                message = stringToBinaryFormat(message);
                cutBinaryStringIntoBlocks(message);
                for (int i = 0; i < quantityOfRounds; i++) {
                    for (int j = 0; j < blocks.length; j++) {
                        blocks[j] = decodeOneRound(blocks[j], keyString);
                    }
                    keyString = keyToPrevRound(keyString);
                }

            } else {
                alert();
            }
        });
    }

    //Метод, доводящий строку до такого размера, чтобы она делилась на sizeOfBlock. Размер увеличивается с помощью добавления к исходной строке символа “решетка”
    private String stringToRightLength(String input) {
        StringBuilder inputBuilder = new StringBuilder(input);
        while (((inputBuilder.length() * sizeOfChar) % sizeOfBlock) != 0)
            inputBuilder.append("#");
        input = inputBuilder.toString();
        return input;
    }

    //Метод, разбивающий строку в обычном (символьном) формате на блоки
    private void cutStringIntoBlocks(String input) {
        blocks = new String[(input.length() * sizeOfChar) / sizeOfBlock];
        System.out.println(blocks.length);
        int lengthOfBlock = input.length() / blocks.length;
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = input.substring(i * lengthOfBlock, i * lengthOfBlock + lengthOfBlock);
            blocks[i] = stringToBinaryFormat(blocks[i]);
        }
    }

    //Метод, разбивающий строку в двоичном формате на блоки
    private void cutBinaryStringIntoBlocks(String input) {
        blocks = new String[input.length() / sizeOfBlock];
        int lengthOfBlock = input.length() / blocks.length;
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = input.substring(i * lengthOfBlock, lengthOfBlock);
        }
    }

    //Метод, переводящий строку в двоичный формат
    private String stringToBinaryFormat(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            String charBinary = String.format("%16s", Integer.toBinaryString(input.charAt(i))).replace(' ', '0');
            output.append(charBinary);
        }
        return output.toString();
    }

    //Метод, доводящий длину ключа до нужной длины
    private String correctKeyWord(String input, int lengthKey) {
        if (input.length() > lengthKey)
            input = input.substring(0, lengthKey);
        else {
            StringBuilder inputBuilder = new StringBuilder(input);
            while (inputBuilder.length() < lengthKey)
                inputBuilder.insert(0, "0");
            input = inputBuilder.toString();
        }
        return input;
    }

    //Один раунд шифрования
    private String encodeOneRound(String input, String key) {
        String L = input.substring(0, input.length() / 2);
        String R = input.substring(input.length() / 2);
        return (R + xor(L, f(R, key)));
    }

    //Один раунд расшифровки
    private String decodeOneRound(String input, String key) {
        String L = input.substring(0, input.length() / 2);
        String R = input.substring(input.length() / 2, input.length() / 2);
        return (xor(f(L, key), R) + L);
    }

    //XOR двух строк с двоичными данными
    private String xor(String s1, String s2) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s1.length(); i++) {
            int a = Integer.parseInt(String.valueOf(s1.charAt(i)));
            int b = Integer.parseInt(String.valueOf(s2.charAt(i)));
            result.append(a ^ b);
        }
        return result.toString();
    }

    //Шифрующая функция f
    private String f(String s1, String s2) {
        return xor(s1, s2);
    }

    //Вычисление ключа для следующего раунда шифрования. Циклический сдвиг >> shiftKey.
    private String keyToNextRound(String key) {
        StringBuilder keyBuilder = new StringBuilder(key);
        for (int i = 0; i < shiftKey; i++) {
            keyBuilder.insert(0, keyBuilder.charAt(keyBuilder.length() - 1));
            keyBuilder.deleteCharAt(keyBuilder.length() - 1);
        }
        key = keyBuilder.toString();

        return key;
    }

    //Вычисление ключа для следующего раунда расшифровки. циклический сдвиг << shiftKey.
    private String keyToPrevRound(String key) {
        StringBuilder keyBuilder = new StringBuilder(key);

        for (int i = 0; i < shiftKey; i++) {
            keyBuilder.append(keyBuilder.charAt(0));
            keyBuilder.delete(0, 1);
        }
        key = keyBuilder.toString();

        return key;
    }

    //Метод, переводящий строку с двоичными данными в символьный формат.
    private String stringFromBinaryToNormalFormat(String input) {
        StringBuilder output = new StringBuilder();
        StringBuilder inputStringBuilder = new StringBuilder(input);
        while (inputStringBuilder.length() > 0) {
            String charBinary = inputStringBuilder.substring(0, sizeOfChar);
            inputStringBuilder.delete(0, sizeOfChar);
            int a = 0;
            int degree = charBinary.length() - 1;
            for (char c : charBinary.toCharArray()) {
                a += c * (int) Math.pow(2, degree--);
            }
            output.append(((char) a));
        }
        return output.toString();
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


