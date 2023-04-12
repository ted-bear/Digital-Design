package com.digdes.school;

/**
 * Запись для удобного хранения частей условия хранения вида *переменная* *оператор* *значение*
 * @param column переменная, которую будем сравнивать
 * @param value значение, с которым сравниваем переменную
 * @param operator оператор сравнения (=, !=, <, >, <=, >=, like, ilike)
 */
public record ValueToCompare(String column, Object value, String operator) {
}
