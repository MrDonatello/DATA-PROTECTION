package sample;

import java.util.HashMap;
import java.util.Map;

class Key {

    private String alphabet;
    private int keyLength;

    Key(String alphabet) {
        this.alphabet = alphabet;
    }

    String getKey(String message) {
        String updatedMessage = message.toLowerCase().replaceAll(" ", ""); //пробелы, заглавные
        StringBuilder stringBuilder = new StringBuilder();
        int k;
        long countChar;
        double index;
        double[] indexes = new double[updatedMessage.length()];
        for (int i = 1; i < updatedMessage.length(); i++) { //перебор строк
            k = i;
            while (k < updatedMessage.length()) { // формирование i-ой строки
                stringBuilder.append(updatedMessage.charAt(k - 1));
                k = k + i;// берем каждый i-й символ
            }
            index = 0.0;
            for (int j = 0; j < alphabet.length(); j++) {
                int finalJ = j;
                countChar = stringBuilder.codePoints().filter(ch -> ch == alphabet.charAt(finalJ)).count(); //количество вхождений символа в строку
                index += countChar * (countChar - 1);
            }
            indexes[i] = index / (stringBuilder.length() * (stringBuilder.length() - 1));
            stringBuilder.setLength(0);
        }
        for (int i = 0; i < updatedMessage.length(); i++) {
            if (indexes[i] > 0.048) {
                keyLength = i;
                break;
            }
        }
        return getKeyWord(updatedMessage);
    }

    private String getKeyWord(String message) {
        String[] strings = new String[keyLength];
        StringBuilder stringBuilder = new StringBuilder();
        int k;
        for (int i = 0; i < keyLength; i++) { //перебор строк
            k = i;
            while (k < message.length()) { // формирование i-ой строки
                stringBuilder.append(message.charAt(k));
                k = k + keyLength;
            }
            strings[i] = stringBuilder.toString();
            stringBuilder.setLength(0);
        }
        long countChar, oIs;
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < keyLength; i++) {
            oIs = 0;
            stringBuilder.setLength(0);
            for (int j = 0; j < alphabet.length(); j++) {
                int finalJ = j;
                countChar = strings[i].codePoints().filter(ch -> ch == alphabet.charAt(finalJ)).count();
                if (countChar > oIs) {
                    oIs = countChar;
                    if (finalJ - 15 < 0) {
                        map.put(i, alphabet.length() + (finalJ - 15));
                    } else {
                        map.put(i, finalJ - 15);
                    }
                }
            }
        }
        stringBuilder.setLength(0);
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            stringBuilder.append(alphabet.charAt(entry.getValue()));
        }
        return stringBuilder.toString();
    }
}
