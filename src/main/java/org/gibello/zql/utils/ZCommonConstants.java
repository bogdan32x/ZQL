/*
 * This file is part of Zql.
 *
 * Zql is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Zql is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zql.  If not, see http://www.gnu.org/licenses.
 */

package org.gibello.zql.utils;

/**
 * Common constants class designed to have lightweight code and free of a large amount of code hiding functionality.
 * 
 * @author Bogdan Mariesan, Romania
 *
 */
public class ZCommonConstants {

    /**
     * Equals and whitespace string.
     */
    public static final String EQUALS_WITH_WHITESPACE_STRING = " = ";

    /**
     * Nullable string.
     */
    public static final String NULLABLE = "(null)";

    /**
     * Unknown column error.
     */
    public static final String ZEVAL_EXP_VALUE_UNKNOWN_COLUMN = "ZEval.evalExpValue(): unknown column ";

    /**
     * Slash string.
     */
    public static final String SLASH = "/";

    /**
     * Multiplication string.
     */
    public static final String MULTIPLICATION = "*";

    /**
     * Minus string.
     */
    public static final String MINUS = "-";

    /**
     * Plus string.
     */
    public static final String PLUS = "+";

    /**
     * Expression not numeric error.
     */
    public static final String ZEVAL_NUMERIC_EXP_EXPRESSION_NOT_NUMERIC = "ZEval.evalNumericExp(): expression not numeric";

    /**
     * Like can only compare strings error.
     */
    public static final String ZEVAL_LIKE_LIKE_CAN_ONLY_COMPARE_STRINGS = "ZEval.evalLike(): LIKE can only compare strings";

    /**
     * Prefix string.
     */
    public static final String PREFIX = "%";

    /**
     * Trying to compare more than two values error.
     */
    public static final String ZEVAL_CMP_TRYING_TO_COMPARE_MORE_THAN_TWO_VALUES = "ZEval.evalCmp(): Trying to compare more than two values";

    /**
     * Trying to compare less than two values error.
     */
    public static final String ZEVAL_CMP_TRYING_TO_COMPARE_LESS_THAN_TWO_VALUES = "ZEval.evalCmp(): Trying to compare less than two values";

    /**
     * Unknown operator error.
     */
    public static final String ZEVAL_UNKNOWN_OPERATOR = "ZEval.eval(): Unknown operator ";

    /**
     * Is not null string.
     */
    public static final String IS_NOT_NULL = "IS NOT NULL";

    /**
     * Can't evaluate IS NOT NULL error.
     */
    public static final String ZEVAL_CAN_T_EVAL_IS_NOT_NULL = "ZEval.eval(): can't eval IS (NOT) NULL";

    /**
     * Is null string.
     */
    public static final String IS_NULL = "IS NULL";

    /**
     * Not in string.
     */
    public static final String NOT_IN = "NOT IN";

    /**
     * In string.
     */
    public static final String IN = "IN";

    /**
     * Not like string.
     */
    public static final String NOT_LIKE = "NOT LIKE";

    /**
     * Like string.
     */
    public static final String LIKE = "LIKE";

    /**
     * Not between string.
     */
    public static final String NOT_BETWEEN = "NOT BETWEEN";

    /**
     * Between string.
     */
    public static final String BETWEEN = "BETWEEN";

    /**
     * Lesser than string.
     */
    public static final String LESSER_THAN = "<";

    /**
     * Greater than string.
     */
    public static final String GREATER_THAN = ">";

    /**
     * Operator # not supported error.
     */
    public static final String ZEVAL_OPERATOR_NOT_SUPPORTED = "ZEval.eval(): Operator # not supported";

    /**
     * Diez string.
     */
    public static final String DIEZ = "#";

    /**
     * Exclusion string.
     */
    public static final String EXCLUDING = "<>";

    /**
     * Not equals string.
     */
    public static final String NOT_EQUALS = "!=";

    /**
     * Equals string.
     */
    public static final String EQUALS = "=";

    /**
     * Not string.
     */
    public static final String NOT = "NOT";

    /**
     * ZEval null argument or operator.
     */
    public static final String ZEVAL_NULL_ARGUMENT_OR_OPERATOR = "ZEval.eval(): null argument or operator";

    /**
     * ZEval only expressions are supported.
     */
    public static final String ZEVAL_ONLY_EXPRESSIONS_ARE_SUPPORTED = "ZEval.eval(): only expressions are supported";

    /**
     * And string.
     */
    public static final String AND = "AND";

    /**
     * Or string.
     */
    public static final String OR = "OR";

    /**
     * The count operator.
     */
    public static final String COUNT_OPERATOR = "COUNT";

    /**
     * The min operator.
     */
    public static final String MIN_OPERATOR = "MIN";

    /**
     * The max operator.
     */
    public static final String MAX_OPERATOR = "MAX";

    /**
     * The average operator.
     */
    public static final String AVG_OPERATOR = "AVG";

    /**
     * The sum operator.
     */
    public static final String SUM_OPERATOR = "SUM";

    /**
     * Empty string.
     */
    public static final String EMPTY_STRING = " ";

    /**
     * Comma.
     */
    public static final String COMMA = ",";

    /**
     * The parse exception error message.
     */
    public static final String PARSE_EXCEPTION = "Parser not initialized: use initParser(InputStream);";

    /**
     * Right bracket.
     */
    public static final String RIGHT_BRACKET = ")";

    /**
     * Left bracket.
     */
    public static final String LEFT_BRACKET = "(";
    
    /**
     * Magic number.
     */
    public static final int MAGIC_NUMBER_3 = 3;

    /**
     * Magic number.
     */
    public static final int MAGIC_NUMBER_2 = 2;
    
    /**
     * Magic number.
     */
    public static final int MAGIC_NUMBER_1 = 1;

}
