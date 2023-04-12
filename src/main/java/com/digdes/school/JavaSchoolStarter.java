package com.digdes.school;

import java.util.*;

import static com.digdes.school.KeyWords.*;
import static com.digdes.school.ParseRequest.*;
import static com.digdes.school.Operators.*;

public class JavaSchoolStarter {
    private final List<Row> table = new ArrayList<>();

    // Default constructor
    public JavaSchoolStarter() {

    }

    public List<Map<String, Object>> execute(String request) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        request = ParseRequest.keyWordsToLowerCase(request);
        String action = findFirstWord(request); // this variable stores command (INSERT, UPDATE, DELETE, SELECT)
        request = request.replaceAll(action, "").trim();

        switch (action) {
            case INSERT_COMMAND -> {
                Map<String, Object> valuesToInsert = ParseRequest.getValues(request);
                result = insertValues(valuesToInsert);
            }
            case UPDATE_COMMAND -> {
                Map<String, Object> newValues = ParseRequest.getValues(request);
                result = updateValues(request, newValues);
            }
            case SELECT_COMMAND -> result = selectValues(request, false);
            case DELETE_COMMAND -> result = selectValues(request, true);
            default -> System.out.println("Не существует такой команды");
        }

        return result;
    }

    /**
     * Метод для добавления новой строки с заданными значениями values в таблицу
     * @param values значения столбцов в новой строке в виде Map
     * @return Таблица со значениями в виде List<Map<String, Object>>
     * @throws Exception в случае передачи пустого values
     */
    private List<Map<String, Object>> insertValues(Map<String, Object> values) throws Exception {
        if (values.isEmpty()) {
            throw new Exception("Insert Error: не переданы значения для добавления");
        }

        Row row = new Row(values);
        table.add(row);

        return convertFromRowToMap(table);
    }

    /**
     * Метод для обновления значений в существующей таблице
     * @param request запрос в строковом виде
     * @param newValues новые значения в формате Map
     * @return возвращает строки из таблицы с обновленными значениями
     */
    private List<Map<String, Object>> updateValues(String request, Map<String, Object> newValues) throws Exception {
        List<Long> indOfRowsToUpdate = getAllIds();

        if (request.contains(WHERE_OPERATOR)) {
            String conditions = request.split(WHERE_OPERATOR)[1].trim();
            indOfRowsToUpdate = getIdsByWhere(conditions);
        }

        for (Long id : indOfRowsToUpdate) {
            for (Map.Entry<String, Object> newValue : newValues.entrySet()) {
                table.get(Math.toIntExact(id)).setNewVal(newValue.getKey(), newValue.getValue());
            }
            if (!table.get(Math.toIntExact(id)).canExist()) {
                table.remove(Math.toIntExact(id));
            }
        }

        return convertFromRowToMap(table);
    }

    /**
     * Метод выбирает значения из таблицы, если необходимо удаялет (toDelete = true), используется для выборки и удаления строк из таблицы
     * @param request запрос (String)
     * @param toDelete выполняем ли операцию delete (boolean)
     * @return строки из таблицы, которые подходят по условию из request'a или удаленные сроки
     * @throws Exception вылетает при ошибки приведения типов при сравнении
     */
    private List<Map<String, Object>> selectValues(String request, boolean toDelete) throws Exception {
        List<Map<String, Object>> selectedRows = new ArrayList<>();
        List<Long> indOfRowsToSelect = getAllIds();

        if (request.contains(WHERE_OPERATOR)) {
            String conditions = request.split(WHERE_OPERATOR)[1].trim();
            indOfRowsToSelect = getIdsByWhere(conditions);
        }

        for (Long index : indOfRowsToSelect) {
            selectedRows.add(table.get(Math.toIntExact(index)).getRow());
            if (toDelete) {
                table.remove(Math.toIntExact(index));
            }
        }

        return selectedRows;
    }

    /**
     * Возвращает все Id из таблицы в виде List<Long>
     * @return List<Long> из Id
     */
    private List<Long> getAllIds() {
        List<Long> allIds = new ArrayList<>();

        for (Row row : table) {
            allIds.add((long) row.getVal(ID_COLUMN));
        }

        return allIds;

    }

    /**
     * Возвращает все подхдящие под условие в request строки
     * @param request запрос в строковом формате
     * @return список Id в формате List<Long>
     * @throws Exception вылетает при ошибки приведения типов при сравнении
     */
    private List<Long> getIdsByWhere(String request) throws Exception {
        List<Long> ids = new ArrayList<>();

        for (int i = 0; i < table.size(); i++) {
            Row row = table.get(i);
            if (compareValues(request, row)) {
                ids.add((long) i);
            }
        }

        return ids;
    }

    /**
     * Сравнивает строку с полученным условием из запроса
     * @param conditions условие в виде строки
     * @param row строка из таблицы
     * @return true, если строка подходит под условия
     * @throws Exception вылетает при ошибки приведения типов при сравнении
     */
    private boolean compareValues(String conditions, Row row) throws Exception {
        boolean isUpdateRes = false;
        String[] conditionsOr = new String[]{conditions};

        if (conditions.contains(OR_OPERATOR)) {
             conditionsOr = conditions.split(OR_OPERATOR);
        }

        ValueToCompare comparing;
        for (String conditionsArr : conditionsOr) {
            if (conditionsArr.contains(AND_OPERATOR)) {
                String[] condition = conditionsArr.split(AND_OPERATOR);
                boolean isUpdateAnd = true;
                for (String cond : condition) {
                    comparing = getConditionFromString(cond);
                    switch (comparing.operator()) {
                        case "=" -> isUpdateAnd = isUpdateAnd && equalValues(row, comparing.column(), comparing.value());//isUpdateStack.add(equalValues(row, comparing.column(), comparing.value()));
                        case "!=" -> isUpdateAnd = isUpdateAnd && !equalValues(row, comparing.column(), comparing.value());
                        case ">" -> isUpdateAnd = isUpdateAnd && moreThen(row, comparing.column(), comparing.value());
                        case ">=" -> isUpdateAnd = isUpdateAnd && moreThenOrEqual(row, comparing.column(), comparing.value());
                        case "<" -> isUpdateAnd = isUpdateAnd && lessThen(row, comparing.column(), comparing.value());
                        case "<=" -> isUpdateAnd = isUpdateAnd && lessThenOrEqual(row, comparing.column(), comparing.value());
                        case "like" -> isUpdateAnd = isUpdateAnd && like(row.getVal(LASTNAME_COLUMN), comparing.value(), true);
                        case "ilike" -> isUpdateAnd = isUpdateAnd && like(row.getVal(LASTNAME_COLUMN), comparing.value(), false);
                    }
                }
                isUpdateRes = isUpdateRes || isUpdateAnd;
            } else {
                comparing = getConditionFromString(conditionsArr);
                switch (comparing.operator()) {
                    case "=" -> isUpdateRes = isUpdateRes || equalValues(row, comparing.column(), comparing.value());
                    case "!=" ->isUpdateRes = isUpdateRes || !equalValues(row, comparing.column(), comparing.value());
                    case ">" -> isUpdateRes = isUpdateRes || moreThen(row, comparing.column(), comparing.value());
                    case ">=" -> isUpdateRes = isUpdateRes || moreThenOrEqual(row, comparing.column(), comparing.value());
                    case "<" -> isUpdateRes = isUpdateRes || lessThen(row, comparing.column(), comparing.value());
                    case "<=" -> isUpdateRes = isUpdateRes || lessThenOrEqual(row, comparing.column(), comparing.value());
                    case "like" -> isUpdateRes = isUpdateRes || like(row.getVal(LASTNAME_COLUMN), comparing.value(), true);
                    case "ilike" -> isUpdateRes = isUpdateRes || like(row.getVal(LASTNAME_COLUMN), comparing.value(), false);
                }
            }
        }

        return isUpdateRes;
    }

    /**
     * Конвертирует строки в таблице из Row в Map.
     * @param listOfRows таблица со строками в виде класса Row
     * @return result - таблица со строкаи в формате Map
     */
    private List<Map<String, Object>> convertFromRowToMap(List<Row> listOfRows) {
        List<Map<String, Object>> result = new ArrayList<>();

        for (Row row : listOfRows) {
            result.add(row.getRow());
        }

        return result;
    }


    /**
     * Метод достает команду их запроса и возвращает ее (Insert, update, delete, select).
     * @param request запрос в строковом виде
     * @return команда в формате String
     */
    private String findFirstWord(String request) {
        request = request.trim(); // to not find space before request
        int indexOfFirstSpace = request.indexOf(' ');

        return request.substring(0, indexOfFirstSpace);
    }
}