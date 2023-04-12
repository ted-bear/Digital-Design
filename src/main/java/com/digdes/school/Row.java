package com.digdes.school;

import java.util.HashMap;
import java.util.Map;

import static com.digdes.school.KeyWords.*;

/**
 * Класс для представления строки Map<String, Object> в виде Row с добалением неоходиимых методов
 * setNewVal и getVal, которые облегчают изменение, доавление новых элементов, удаление и выбор элементов из таблицы
 */
public class Row {
    private final Map<String, Object> row;

    public Row() {
        row = getDefaultMap();
    }

    public Row(Map<String, Object> row) {
        this.row = getDefaultMap();
        this.row.putAll(row);
    }

    public Map<String, Object> getRow() {
        return row;
    }

    /**
     * Устанавливает новое значение в строке определенной колонке
     * @param key колонка для обновления значения в ней
     * @param val новое значение
     */
    public void setNewVal(String key, Object val) {
        switch (key) {
            case ID_COLUMN -> {
                setId(val);
            }
            case AGE_COLUMN -> setAge( val);
            case LASTNAME_COLUMN -> setLastName(val);
            case COST_COLUMN -> setCost(val);
            case ACTIVE_COLUMN -> setActive(val);
        }
    }

    /**
     * Позволяет достать значение из строки по имени колонки
     * @param key имя колонки
     * @return значение в переданной колонке
     */
    public Object getVal(String key) {
        switch (key) {
            case ID_COLUMN -> {
                return getId();
            }
            case AGE_COLUMN -> {
                return getAge();
            }
            case LASTNAME_COLUMN -> {
                return getLastName();
            }
            case COST_COLUMN -> {
                return getCost();
            }
            case ACTIVE_COLUMN -> {
                return getActive();
            }
            default -> {
                return null;
            }
        }
    }

    public boolean canExist() {
        for (Map.Entry<String, Object> entry : row.entrySet()) {
            if (entry.getValue() != null) {
                return true;
            }
        }
        return false;
    }

    private long getId() {
        return row.get(ID_COLUMN) == null ? (long) -1 : (long) row.get(ID_COLUMN);
    }

    private long getAge() {
        return row.get(AGE_COLUMN) == null ? (long) -1 : (long) row.get(AGE_COLUMN);
    }

    private String getLastName() {
        return (String) row.get(LASTNAME_COLUMN);
    }

    private Double getCost() {
        return row.get(COST_COLUMN) != null ? (double) row.get(COST_COLUMN) : -1.;
    }

    private boolean getActive() {
        return (boolean) row.get(ACTIVE_COLUMN);
    }

    private void setId(Object id) {
        id = id == null ? null : (Long) id;
        row.put(ID_COLUMN, id);
    }

    private void setAge(Object age) {
        age = age == null ? null : (Long) age;
        row.put(AGE_COLUMN, age);
    }

    private void setLastName(Object lastName) {
        lastName = lastName == null ? null : (String) lastName;
        row.put(LASTNAME_COLUMN, lastName);
    }

    private void setCost(Object cost) {
        cost = cost == null ? null : (Double) cost;
        row.put(COST_COLUMN, cost);
    }

    private void setActive(Object active) {
        active = active == null ? null : (boolean) active;
        row.put(ACTIVE_COLUMN, active);
    }

    private Map<String, Object> getDefaultMap() {
        Long defaultNum = (long) -1;
        Double defDouble = -1.0;
        Map<String, Object> map = new HashMap<>();
        map.put(ID_COLUMN, null);
        map.put(LASTNAME_COLUMN, null);
        map.put(AGE_COLUMN, null);
        map.put(COST_COLUMN, null);
        map.put(ACTIVE_COLUMN, null);

        return map;
    }
}
