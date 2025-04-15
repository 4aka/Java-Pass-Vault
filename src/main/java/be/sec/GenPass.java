package be.sec;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.lang.Character.*;

public class GenPass {
    private final String ERROR_MESSAGE = "Incorrect";

    /**
     *
     * @param length
     * @return
     */
    public String GeneratePassword(int length) {
        return genPassV2(length);
    }

    /**
     *
     * @param len
     * @return
     */
    private String genPassV2(int len) {
        String pass;
        do {
            pass = generateSymbols(len);
        } while (pass.equals(ERROR_MESSAGE));
        return pass;
    }

    /**
     *
     * @return
     */
    private List<Integer> specSymbols() {
        return Arrays
                .asList(33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45,
                        46, 47, 58, 59, 60, 61, 62, 63, 64, 91, 92, 93, 94,
                        95, 96, 123, 124, 125, 126);
    }

    /**
     *
     * @param password
     * @return
     */
    private boolean isPassContainsAllImportantParts(String password) {
        boolean isLower = false, isUpper = false, isDigit = false, isSpec = false;
        char[] chars = password.toCharArray();

        for (int i = 0; i < password.length(); i++) {
            if (isLowerCase(chars[i])) isLower = true;
            if (isUpperCase(chars[i])) isUpper = true;
            if (isDigit(chars[i])) isDigit = true;
            if (specSymbols().contains((int) chars[i])) isSpec = true;
        }
        return isLower && isUpper && isDigit && isSpec;
    }

    /**
     *
     * @param len
     * @return
     */
    private String generateSymbols(int len) {
        StringBuilder pass = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < len; i++) {
            int action = random.nextInt(4);
            int asciiSymbol = 0;

            switch (action) {
                case 0: asciiSymbol = random.nextInt(25) + 97; break;  // a - z. 97 - 122
                case 1: asciiSymbol = random.nextInt(25) + 65; break;  // A - Z. 65 - 90
                case 2: asciiSymbol = random.nextInt(9) + 48;  break;  // 0 - 9. 48 - 57
                case 3: asciiSymbol = specSymbols()
                        .get(random.nextInt(specSymbols().size())); break;    // special symbol
            }
            pass.append((char) asciiSymbol);
        }
        if (isPassContainsAllImportantParts(pass.toString())) return pass.toString();
        else return ERROR_MESSAGE;
    }
}
