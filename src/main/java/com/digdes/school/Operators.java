package com.digdes.school;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.digdes.school.KeyWords.*;

public class Operators {
    /**
     * Метод больше или равно (>=) для корректного сравнения Long и Double
     * @param row строка, значение из которой сравниаваем
     * @param column колонка в строке, из которой достаем значение
     * @param value значение для сравнения
     * @return true, если значение в строке больше или равно value
     */
    public static boolean moreThenOrEqual(Row row, String column, Object value) {
        // Если по условию value не может быть null, то при равенстве значения в строке null всегда возвращается false
        if (row.getVal(column) == null) {
            return false;
        }

        if (column.equals(AGE_COLUMN) || column.equals(ID_COLUMN)) {
            return (Long) row.getVal(column) >= (Long) value;
        }

        return (Double) row.getVal(column) >= (Double) value;
    }

    /**
     * Метод больше (>) для корректного сравнения Long и Double
     * @param row строка, значение из которой сравниаваем
     * @param column колонка в строке, из которой достаем значение
     * @param value значение для сравнения
     * @return true, если значение в строке больше value
     */
    public static boolean moreThen(Row row, String column, Object value) {
        if (row.getVal(column) == null) {
            return false;
        }

        if (column.equals(AGE_COLUMN) || column.equals(ID_COLUMN)) {
            return (Long) row.getVal(column) > (Long) value;
        }

        return (Double) row.getVal(column) > (Double) value;
    }

    /**
     * Метод меньше или равно (<=) для корректного сравнения Long и Double
     * @param row строка, значение из которой сравниаваем
     * @param column колонка в строке, из которой достаем значение
     * @param value значение для сравнения
     * @return true, если значение в строке меньше или равно value
     */
    public static boolean lessThenOrEqual(Row row, String column, Object value) {
        if (row.getVal(column) == null) {
            return false;
        }

        if (column.equals(AGE_COLUMN) || column.equals(ID_COLUMN)) {
            return (Long) row.getVal(column) <= (Long) value;
        }

        return (Double) row.getVal(column) <= (Double) value;
    }

    /**
     * Метод меньше (<) для корректного сравнения Long и Double
     * @param row строка, значение из которой сравниаваем
     * @param column колонка в строке, из которой достаем значение
     * @param value значение для сравнения
     * @return true, если значение в строке меньше value
     */
    public static boolean lessThen(Row row, String column, Object value) {
        if (row.getVal(column) == null) {
            return false;
        }

        if (column.equals(AGE_COLUMN) || column.equals(ID_COLUMN)) {
            return (Long) row.getVal(column) < (Long) value;
        }

        return (Double) row.getVal(column) < (Double) value;
    }

    public static boolean like(Object rowValue, Object value, boolean caseSensitive) {
        if (rowValue == null) {
            return false;
        }

        String rowValueStr = String.valueOf(rowValue).replace("'", "");
        String valueStr = String.valueOf(value).replace("'", "");
        StringBuilder regExp = new StringBuilder();

        for (Character character : valueStr.toCharArray()) {
            if (character == '%') {
                regExp.append("(.*)");
                continue;
            }
            regExp.append(character);
        }

        Pattern wordPattern;
        if (!caseSensitive) {
            rowValueStr = rowValueStr.toLowerCase();
            wordPattern = Pattern.compile(regExp.toString(), Pattern.CASE_INSENSITIVE);
        } else {
            wordPattern = Pattern.compile(regExp.toString());
        }

        Matcher wordMatcher = wordPattern.matcher(rowValueStr);

        return wordMatcher.matches();
    }

    /**
     * Проверяет переменные на равенство их типов и значений
     * @param row строка, значение из которой достается
     * @param column колонка в строке с нужным значением
     * @param value значение для сравнения
     * @return true, если тип и значения элементов одинаковы
     */
    public static boolean equalValues(Row row, String column, Object value) {
        if (row.getVal(column) == null) {
            return false;
        }

        if (column.equals(LASTNAME_COLUMN)) {
            value = String.valueOf(value).replace("'", "");
            return row.getVal(column).equals(value);
        }

        boolean isLong = column.equals(AGE_COLUMN) || column.equals(ID_COLUMN);

        if (String.valueOf(value).contains(".") && isLong) {
            return false;
        }

        return row.getVal(column) == value;
    }
}
