package com.digdes.school;

import java.util.HashMap;
import java.util.Map;

import static com.digdes.school.KeyWords.*;

public class ParseRequest {

    /**
     * Достает из запроса строку со значениями для вставки / обновления, приводит к нужному типу
     * с помощью метода getValueFromString(String) и собирает все в Map<String, Object>.
     * @param request исходный запрос
     * @return словарь из значений для добавления / удаления
     * @throws Exception если тип введеного праматера не соответсвует типу перменной
     */
    public static Map<String, Object> getValues(String request) throws Exception {
        int startOfValues = VALUES_OPERATOR.length() + 1;
        int endOfValues = request.length();

        if (request.contains(WHERE_OPERATOR)) {
            endOfValues = request.indexOf(WHERE_OPERATOR);
        }

        request = request.substring(startOfValues, endOfValues);
        String[] valuesToInsert = request.split(",");

        Map<String, Object> map = new HashMap<>();
        Object[] valueToInsert;

        for (String pair : valuesToInsert) {
            valueToInsert = getValueFromString(pair);
            map.put((String) valueToInsert[0], valueToInsert[1]);
        }

        return map;
    }

    /**
     * Приводит все ключевые слова к нижнему регистру
     * @param request запрос в формате строки
     * @return запрос в формате строки с ключевыми словами в нижнем регистре
     */
    public static String keyWordsToLowerCase(String request) {
        String[] words = request.split(" ");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (ALL_WORDS.contains(words[i].toLowerCase())) {
                words[i] = words[i].toLowerCase();
            }
            result.append(words[i]).append(" ");
        }

        return result.toString();
    }

    /**
     * Метод необходим для операции Insert и Update, чтобы после ключевого слова Values
     * прочитать параметры, которые присваиваются переменными. Также определеяет тип переменной
     * и преобразует к нему (double, boolean, long, String).
     * @param str строка с присвоением
     * @throws Exception если тип введеного праматера не соответсвует типу перменной
     * @return Object[] содержит key и value
     */
    public static Object[] getValueFromString(String str) throws Exception {
        String[] variable = str.split("=");

        return castValuesToInsert(variable);
    }

    /**
     * Метод предназачен для того чтобы доставать условие сравнения из строки после
     * ключевого слова where
     * @param str строка состоящая из одного и более условий (пременной, оператора, значения для сравнения)
     * @return map ключ, которой является парой параметр сраавнения - значение, с которым сравниваем,
     * а значением - оператор сравнения
     */
    public static ValueToCompare getConditionFromString(String str) throws Exception {
        String operator = findOperator(str);

        Object[] pairKeyVal = castValuesToCompare(str, operator);

        return new ValueToCompare((String) pairKeyVal[0], pairKeyVal[1], operator);
    }

    /**
     * Получает на вход строку вида "*переменная* *оператор* *значение для сравнения*" и находит
     * оператор (like, ilike, = , !=, >=, <=, >, <)
     * @param condition строка состоящая из пременной, оператора, значения для сравнения
     * @return оператор представленный в строков типе
     */
    public static String findOperator(String condition) {
        StringBuilder result = new StringBuilder();
        condition = condition.trim();

        if (condition.contains(" ")) {
            String[] splitedCond = condition.split(" ");
            if (splitedCond.length > 1 && (splitedCond[1].equals("like") || splitedCond[1].equals("ilike"))) {
                return condition.split(" ")[1];
            }
        }

        for (int i = 0; i < condition.length(); i++) {
            if (!Character.isDigit(condition.charAt(i)) &&
                !Character.isAlphabetic(condition.charAt(i)) &&
                condition.charAt(i) != ' ' &&
                condition.charAt(i) != '\'' &&
                condition.charAt(i) != '.') {

                result.append(condition.charAt(i));
            }
        }

        return result.toString();
    }

    /**
     * Приводит типы для сравнения, учитывая, что при использовании операторов равно и неравно должны проверяться типы перменных
     * @param condition условие в строковом представлении
     * @param operator оператор сравнения
     * @return массив из пары ключ (String) - значение (String, Long, Double, Boolean)
     * @throws Exception генерируется при несоответсвие типов переменных
     */
    private static Object[] castValuesToCompare(String condition, String operator) throws Exception {
        String[] values =  condition.split(operator);
        String key = values[0].replace("'", "").trim();
        String val = values[1].trim();
        Object v;

        if (val.charAt(0) == '\'') {
            v = val;
            return new Object[] {key,v};
        }

        boolean isLong = key.equals(AGE_COLUMN) || key.equals(ID_COLUMN);

        if ((operator.equals("=") || operator.equals("!=")) && val.contains(".") && isLong) {
            v = Double.parseDouble(val);
        } else if (val.contains(".") && isLong) {
            val = val.substring(0, val.indexOf('.'));
            v = Long.parseLong(val);
        } else if (isLong) {
            v = Long.parseLong(val);
        } else if (val.contains(".")) {
            v = Double.parseDouble(val);
        } else if (val.equalsIgnoreCase("true") && key.equals(ACTIVE_COLUMN)) {
            v = true;
        } else if (val.equalsIgnoreCase("false") && key.equals(ACTIVE_COLUMN)) {
            v = false;
        } else {
            throw new Exception("Update Error: Тип введенного значения не соответсвует типу перменной или не существует такого значения");
        }

        return new Object[]{key, v};
    }

    /**
     * Метод получая пару ключ - значение в строковом типе приводит значение к необходимому типу в соответсвии со значением ключа
     * @param values ключ и значение в строковых типах
     * @return пару ключ значение, где значение приведенно к необходимому типу
     * @throws Exception генерируется при несоответсвие типов переменных или при отсутсвии введенной колонки
     */
    private static Object[] castValuesToInsert(String[] values) throws Exception {
        String key = values[0].replace("'", "").trim();
        String val = values[1].trim();
        Object v;

        if (val.equals("null")) {
            return new Object[]{key, null};
        }

        if (val.contains(".") && key.equals(COST_COLUMN)) {
            v = Double.parseDouble(val);
        } else if (val.equalsIgnoreCase("true") && key.equals(ACTIVE_COLUMN)) {
            v = true;
        } else if (val.equalsIgnoreCase("false") && key.equals(ACTIVE_COLUMN)) {
            v = false;
        } else if (isNumber(val) && (key.equals(AGE_COLUMN) || key.equals(ID_COLUMN))) {
            v = Long.parseLong(val);
        } else if (!isNumber(val) && key.equals(LASTNAME_COLUMN)) {
            v = val.replace("'", "");
        } else {
            throw new Exception("Insert Error: Тип введенного значения не соответсвует типу перменной или не существует такого значения");
        }

        return new Object[]{key, v};
    }

    /**
     * Метод проверяет, что строка является числом типа long
     * @param str исходная строка
     * @return true, если строка является числом; false, если не является числом
     */
    private static Boolean isNumber(String str) {
        int currentChar;
        for (int i = 0; i < str.length(); i++) {
            currentChar = str.charAt(i);
            if (!Character.isDigit(currentChar)) {
                return false;
            }
        }
        return true;
    }
}
