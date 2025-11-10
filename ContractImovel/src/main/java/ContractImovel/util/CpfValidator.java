package ContractImovel.util;

public class CpfValidator {

    public static boolean isValid(String cpf) {
        if (cpf == null || cpf.length() != 11 || cpf.matches(cpf.charAt(0) + "{11}")) {
            return false;
        }

        try {
            Long.parseLong(cpf);
        } catch (NumberFormatException e) {
            return false;
        }

        int[] digits = new int[11];
        for (int i = 0; i < 11; i++) {
            digits[i] = Character.getNumericValue(cpf.charAt(i));
        }

        int v1 = 0, v2 = 0;
        for (int i = 0; i < 9; i++) {
            v1 += digits[i] * (10 - i);
            v2 += digits[i] * (11 - i);
        }

        v1 = (v1 * 10) % 11;
        if (v1 == 10) v1 = 0;

        v2 += v1 * 2;
        v2 = (v2 * 10) % 11;
        if (v2 == 10) v2 = 0;

        return v1 == digits[9] && v2 == digits[10];
    }

    public static String format(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + 
               cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    public static String unformat(String cpf) {
        if (cpf == null) return null;
        return cpf.replaceAll("[^0-9]", "");
    }
}