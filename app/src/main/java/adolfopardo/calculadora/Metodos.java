package adolfopardo.calculadora;

import android.widget.EditText;

import java.util.Locale;

/**
 * Created by Alain Nicolás Tello on 20/06/2017.
 * Todos los derechos reservados.
 */

public class Metodos {
    public static String getOperator(String operation) {
        String operator = operation.contains(Constantes.OPERATOR_MULTI) ? Constantes.OPERATOR_MULTI :
                operation.contains(Constantes.OPERATOR_BETWEN) ? Constantes.OPERATOR_BETWEN :
                        operation.contains(Constantes.OPERATOR_SUM) ? "\\" + Constantes.OPERATOR_SUM : Constantes.OPERATOR_NULL;

        if (operator.equals(Constantes.OPERATOR_NULL) && operation.lastIndexOf(Constantes.OPERATOR_SUB) > 0) {
            operator = Constantes.OPERATOR_SUB;
        }
        return operator;
    }

    public static void tryResolve(boolean fromResult, EditText etInput, OnResolveCallback callback) {
        String operation = etInput.getText().toString();

        if (operation.isEmpty()) {
            return;
        }

        if (operation.contains(Constantes.POINT) &&
                operation.lastIndexOf(Constantes.POINT) == operation.length() - 1) {
            operation = operation.substring(0, operation.length() - 1);
        }

        String operator = Metodos.getOperator(operation);
        String[] values = new String[0];
        if (!operator.equals(Constantes.OPERATOR_NULL)) {
            if (operator.equals(Constantes.OPERATOR_SUB)) {
                final int index = operation.lastIndexOf(Constantes.OPERATOR_SUB);
                values = new String[2];
                values[0] = operation.substring(0, index);
                values[1] = operation.substring(index + 1);
            } else {
                values = operation.split(operator);
            }
        }

        if (values.length > 1) {
            try {
                final double numberOne = Double.valueOf(values[0]);
                final double numberTwo = Double.valueOf(values[1]);
                etInput.getText().clear();
                callback.onIsEditing();
                etInput.append(String.format(Locale.ROOT, "%.2f",
                        Metodos.getResult(numberOne, operator, numberTwo)));
            } catch (NumberFormatException e) {
                if (fromResult) {
                    callback.onShowMessage(R.string.message_exp_incorrect);
                }
            }
        } else {
            if (fromResult) {
                if (!operator.equals(Constantes.OPERATOR_NULL)){
                    callback.onShowMessage(R.string.message_exp_incorrect);
                }
            }
        }
    }

    public static double getResult(double numberOne, String operator, double numberTwo) {
        double result = 0;

        operator = operator.replace("\\", "");

        switch (operator) {
            case Constantes.OPERATOR_MULTI:
                result = numberOne * numberTwo;
                break;
            case Constantes.OPERATOR_BETWEN:
                result = numberOne / numberTwo;
                break;
            case Constantes.OPERATOR_SUM:
                result = numberOne + numberTwo;
                break;
            case Constantes.OPERATOR_SUB:
                result = numberOne - numberTwo;
                break;
        }

        return result;
    }

    public static boolean canReplaceOperator(CharSequence s) {
        if (s.length() < 2) {
            return false;
        }
        final String ultimoCaracter = String.valueOf(s.charAt(s.length()-1));
        final String penultimoCaracter = String.valueOf(s.charAt(s.length()-2));

        return (ultimoCaracter.equals(Constantes.OPERATOR_MULTI) ||
                ultimoCaracter.equals(Constantes.OPERATOR_BETWEN) ||
                ultimoCaracter.equals(Constantes.OPERATOR_SUM)) &&
                (String.valueOf(s.charAt(s.length() - 2)).equals(Constantes.OPERATOR_MULTI) ||
                penultimoCaracter.equals(Constantes.OPERATOR_BETWEN) ||
                penultimoCaracter.equals(Constantes.OPERATOR_SUM) ||
                penultimoCaracter.equals(Constantes.OPERATOR_SUB));
    }
}
