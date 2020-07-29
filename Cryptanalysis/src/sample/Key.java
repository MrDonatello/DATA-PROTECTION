package sample;

class Key {

    private String alphabet;
    private String key;

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
        for (int i = 1; i < updatedMessage.length(); i++) { //перебор солбцов
            k = i;
            while (k < updatedMessage.length()) { // формирование i-го столбца
                stringBuilder.append(updatedMessage.charAt(k - 1));
                k = k + i;
            }
            index = 0.0;
            for (int j = 0; j < alphabet.length(); j++) {
                int finalJ = j;
                countChar = stringBuilder.codePoints().filter(ch -> ch == alphabet.charAt(finalJ)).count(); //количество вхождений символа в столбец
                index += countChar * (countChar - 1);
            }
            indexes[i] = index / (stringBuilder.length() * (stringBuilder.length() - 1));
            stringBuilder.setLength(0);
        }
        int j[] = new int[3];
        int g =0;
        for (int i = 0; i < updatedMessage.length(); i++) {
            if (indexes[i] > 0.05) {
                j[g] = i;
                g++;
                if(g>2){
                    break;
                }
            }


        }
        if(j[0]==j[1]-j[0]){
            key = String.valueOf(j[0]);
        }
        return key;
    }

}
