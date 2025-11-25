package ContractImovel.util;

public class CpfValidator {
    public static boolean isValidCPF(String cpf) {
        cpf = unformat(cpf);

        if (cpf == null || cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
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

        int soma1 = 0, soma2 = 0;

        for (int i = 0; i < 9; i++) {
            soma1 += digits[i] * (10 - i);
            soma2 += digits[i] * (11 - i);
        }

        int digito1 = (soma1 * 10) % 11;
        if (digito1 == 10) digito1 = 0;

        soma2 += digito1 * 2;
        int digito2 = (soma2 * 10) % 11;
        if (digito2 == 10) digito2 = 0;

        return digito1 == digits[9] && digito2 == digits[10];
    }

    /* ------------------------ CNPJ ------------------------ */

    public static boolean isValidCNPJ(String cnpj) {
        cnpj = unformat(cnpj);

        if (cnpj == null || cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        try {
            Long.parseLong(cnpj);
        } catch (NumberFormatException e) {
            return false;
        }

        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int soma1 = 0, soma2 = 0;
        int[] dig = new int[14];

        for (int i = 0; i < 14; i++) {
            dig[i] = Character.getNumericValue(cnpj.charAt(i));
        }

        for (int i = 0; i < 12; i++) soma1 += dig[i] * pesos1[i];

        int dv1 = soma1 % 11;
        dv1 = (dv1 < 2) ? 0 : 11 - dv1;

        for (int i = 0; i < 13; i++) soma2 += dig[i] * pesos2[i];

        int dv2 = soma2 % 11;
        dv2 = (dv2 < 2) ? 0 : 11 - dv2;

        return dv1 == dig[12] && dv2 == dig[13];
    }

    /* -------------------- Formatadores -------------------- */

    public static boolean isValid(String documento) {
        documento = unformat(documento);

        if (documento.length() == 11) {
            return isValidCPF(documento);
        } else if (documento.length() == 14) {
            return isValidCNPJ(documento);
        } else {
            return false;
        }
    }

    public static String formatCPF(String cpf) {
        cpf = unformat(cpf);
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})",
                                "$1.$2.$3-$4");
    }

    public static String formatCNPJ(String cnpj) {
        cnpj = unformat(cnpj);
        if (cnpj == null || cnpj.length() != 14) return cnpj;
        return cnpj.replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})",
                                 "$1.$2.$3/$4-$5");
    }

    public static String unformat(String value) {
        if (value == null) return null;
        return value.replaceAll("\\D", "");
    }
}
