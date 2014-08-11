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

package org.gibello.zql.data;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.Vector;

import org.gibello.zql.ZConstant;
import org.gibello.zql.ZExp;
import org.gibello.zql.ZExpression;
import org.gibello.zql.ZqlParser;
import org.gibello.zql.utils.ZCommonConstants;

/**
 * Evaluate SQL expressions.
 * 
 * @author Bogdan Mariesan, Romania
 */
public class ZEval {

    /**
     * Default constructor.
     */
    public ZEval() {

    }

    /**
     * Evaluate a boolean expression to true or false (for example, SQL WHERE. clauses are boolean expressions)
     * 
     * @param tuple
     *            The tuple on which to evaluate the expression
     * @param exp
     *            The expression to evaluate
     * @return true if the expression evaluate to true for this tuple, false if not.
     * @throws SQLException
     *             the sql exception.
     */
    public final boolean eval(final ZTuple tuple, final ZExp exp) throws SQLException {

        if (tuple == null || exp == null) {
            throw new SQLException(ZCommonConstants.ZEVAL_NULL_ARGUMENT_OR_OPERATOR);
        }

        if (!(exp instanceof ZExpression)) {
            throw new SQLException(ZCommonConstants.ZEVAL_ONLY_EXPRESSIONS_ARE_SUPPORTED);
        }

        final ZExpression pred = (ZExpression) exp;
        final String op = pred.getOperator();

        if (op.equals(ZCommonConstants.AND)) {
            boolean and = true;
            for (int i = 0; i < pred.nbOperands(); i++) {
                and &= this.eval(tuple, pred.getOperand(i));
            }
            return and;
        } else if (op.equals(ZCommonConstants.OR)) {
            boolean or = false;
            for (int i = 0; i < pred.nbOperands(); i++) {
                or |= this.eval(tuple, pred.getOperand(i));
            }
            return or;
        } else if (op.equals(ZCommonConstants.NOT)) {
            return !this.eval(tuple, pred.getOperand(0));

        } else if (op.equals(ZCommonConstants.EQUALS)) {
            return evalCmp(tuple, pred.getOperands()) == 0;
        } else if (op.equals(ZCommonConstants.NOT_EQUALS)) {
            return evalCmp(tuple, pred.getOperands()) != 0;
        } else if (op.equals(ZCommonConstants.EXCLUDING)) {
            return evalCmp(tuple, pred.getOperands()) != 0;
        } else if (op.equals(ZCommonConstants.DIEZ)) {
            throw new SQLException(ZCommonConstants.ZEVAL_OPERATOR_NOT_SUPPORTED);
        } else if (op.equals(ZCommonConstants.GREATER_THAN)) {
            return evalCmp(tuple, pred.getOperands()) > 0;
        } else if (op.equals(ZCommonConstants.GREATER_THAN + ZCommonConstants.EQUALS)) {
            return evalCmp(tuple, pred.getOperands()) >= 0;
        } else if (op.equals(ZCommonConstants.LESSER_THAN)) {
            return evalCmp(tuple, pred.getOperands()) < 0;
        } else if (op.equals(ZCommonConstants.LESSER_THAN + ZCommonConstants.EQUALS)) {
            return evalCmp(tuple, pred.getOperands()) <= 0;

        } else if (op.equals(ZCommonConstants.BETWEEN) || op.equals(ZCommonConstants.NOT_BETWEEN)) {

            // Between: borders included
            final ZExpression newexp = new ZExpression(ZCommonConstants.AND, new ZExpression(ZCommonConstants.GREATER_THAN + ZCommonConstants.EQUALS, pred.getOperand(0),
                    pred.getOperand(1)), new ZExpression(ZCommonConstants.LESSER_THAN + ZCommonConstants.EQUALS, pred.getOperand(0), pred.getOperand(2)));

            if (op.equals(ZCommonConstants.NOT_BETWEEN)) {
                return !this.eval(tuple, newexp);
            } else {
                return this.eval(tuple, newexp);
            }

        } else if (op.equals(ZCommonConstants.LIKE) || op.equals(ZCommonConstants.NOT_LIKE)) {
            final boolean like = this.evalLike(tuple, pred.getOperands());
            return op.equals(ZCommonConstants.LIKE) ? like : !like;

        } else if (op.equals(ZCommonConstants.IN) || op.equals(ZCommonConstants.NOT_IN)) {

            final ZExpression newexp = new ZExpression(ZCommonConstants.OR);

            for (int i = 1; i < pred.nbOperands(); i++) {
                newexp.addOperand(new ZExpression(ZCommonConstants.EQUALS, pred.getOperand(0), pred.getOperand(i)));
            }

            if (op.equals(ZCommonConstants.NOT_IN)) {
                return !this.eval(tuple, newexp);
            } else {
                return this.eval(tuple, newexp);
            }

        } else if (op.equals(ZCommonConstants.IS_NULL)) {

            if (pred.nbOperands() <= 0 || pred.getOperand(0) == null) {
                return true;
            }
            final ZExp x = pred.getOperand(0);
            if (x instanceof ZConstant) {
                return ((ZConstant) x).getType() == ZConstant.NULL;
            } else {
                throw new SQLException(ZCommonConstants.ZEVAL_CAN_T_EVAL_IS_NOT_NULL);
            }

        } else if (op.equals(ZCommonConstants.IS_NOT_NULL)) {

            final ZExpression x = new ZExpression(ZCommonConstants.IS_NULL);
            x.setOperands(pred.getOperands());
            return !this.eval(tuple, x);

        } else {
            throw new SQLException(ZCommonConstants.ZEVAL_UNKNOWN_OPERATOR + op);
        }

    }

    /**
     * Compares values on a given operand.
     * 
     * @param tuple
     *            the tuple.
     * @param operands
     *            list of operands.
     * @return the result.
     * @throws SQLException
     *             the sql exception.
     */
    final double evalCmp(final ZTuple tuple, final Vector<?> operands) throws SQLException {

        if (operands.size() < ZCommonConstants.MAGIC_NUMBER_2) {
            throw new SQLException(ZCommonConstants.ZEVAL_CMP_TRYING_TO_COMPARE_LESS_THAN_TWO_VALUES);
        }
        if (operands.size() > ZCommonConstants.MAGIC_NUMBER_2) {
            throw new SQLException(ZCommonConstants.ZEVAL_CMP_TRYING_TO_COMPARE_MORE_THAN_TWO_VALUES);
        }

        Object o1 = null;
        Object o2 = null;

        o1 = this.evalExpValue(tuple, (ZExp) operands.elementAt(0));
        o2 = this.evalExpValue(tuple, (ZExp) operands.elementAt(1));

        if (o1 instanceof String || o2 instanceof String) {
            return o1.equals(o2) ? 0 : -1;
        }

        if (o1 instanceof Number && o2 instanceof Number) {
            return ((Number) o1).doubleValue() - ((Number) o2).doubleValue();
        } else {
            throw new SQLException("ZConstans.evalCmp(): can't compare (" + o1.toString() + ") with (" + o2.toString() + ")");
        }
    }

    /**
     * evalLike evaluates the LIKE operand.
     * 
     * @param tuple
     *            the tuple to evaluate
     * @param operands
     *            the operands
     * @return true-> the expression matches
     * @throws SQLException
     *             the sql exception.
     */
    private boolean evalLike(final ZTuple tuple, final Vector<?> operands) throws SQLException {
        if (operands.size() < ZCommonConstants.MAGIC_NUMBER_2) {
            throw new SQLException(ZCommonConstants.ZEVAL_CMP_TRYING_TO_COMPARE_LESS_THAN_TWO_VALUES);
        }
        if (operands.size() > ZCommonConstants.MAGIC_NUMBER_2) {
            throw new SQLException(ZCommonConstants.ZEVAL_CMP_TRYING_TO_COMPARE_MORE_THAN_TWO_VALUES);
        }

        final Object o1 = evalExpValue(tuple, (ZExp) operands.elementAt(0));
        final Object o2 = evalExpValue(tuple, (ZExp) operands.elementAt(1));

        if ((o1 instanceof String) && (o2 instanceof String)) {
            final String s1 = (String) o1;
            final String s2 = (String) o2;
            if (s2.startsWith(ZCommonConstants.PREFIX)) {
                return s1.endsWith(s2.substring(1));
            } else if (s2.endsWith(ZCommonConstants.PREFIX)) {
                return s1.startsWith(s2.substring(0, s2.length() - 1));
            } else {
                return s1.equalsIgnoreCase(s2);
            }
        } else {
            throw new SQLException(ZCommonConstants.ZEVAL_LIKE_LIKE_CAN_ONLY_COMPARE_STRINGS);
        }

    }

    /**
     * Evaluates a numeric expression.
     * 
     * @param tuple
     *            the tuple
     * @param exp
     *            the expression
     * @return the result
     * @throws SQLException
     *             the sql exception
     */
    final double evalNumericExp(final ZTuple tuple, final ZExpression exp) throws SQLException {

        if (tuple == null || exp == null || exp.getOperator() == null) {
            throw new SQLException(ZCommonConstants.ZEVAL_NULL_ARGUMENT_OR_OPERATOR);
        }

        final String op = exp.getOperator();

        final Object o1 = this.evalExpValue(tuple, (ZExp) exp.getOperand(0));
        if (!(o1 instanceof Double)) {
            throw new SQLException(ZCommonConstants.ZEVAL_NUMERIC_EXP_EXPRESSION_NOT_NUMERIC);
        }

        final Double dobj = (Double) o1;

        if (op.equals(ZCommonConstants.PLUS)) {

            double val = dobj.doubleValue();
            for (int i = 1; i < exp.nbOperands(); i++) {
                final Object obj = this.evalExpValue(tuple, (ZExp) exp.getOperand(i));
                val += ((Number) obj).doubleValue();
            }
            return val;

        } else if (op.equals(ZCommonConstants.MINUS)) {

            double val = dobj.doubleValue();
            if (exp.nbOperands() == 1) {
                return -val;
            }
            for (int i = 1; i < exp.nbOperands(); i++) {
                final Object obj = this.evalExpValue(tuple, (ZExp) exp.getOperand(i));
                val -= ((Number) obj).doubleValue();
            }
            return val;

        } else if (op.equals(ZCommonConstants.MULTIPLICATION)) {

            double val = dobj.doubleValue();
            for (int i = 1; i < exp.nbOperands(); i++) {
                final Object obj = this.evalExpValue(tuple, (ZExp) exp.getOperand(i));
                val *= ((Number) obj).doubleValue();
            }
            return val;

        } else if (op.equals(ZCommonConstants.SLASH)) {

            double val = dobj.doubleValue();
            for (int i = 1; i < exp.nbOperands(); i++) {
                final Object obj = this.evalExpValue(tuple, (ZExp) exp.getOperand(i));
                val /= ((Number) obj).doubleValue();
            }
            return val;

        } else if (op.equals(ZCommonConstants.MULTIPLICATION + ZCommonConstants.MULTIPLICATION)) {

            double val = dobj.doubleValue();
            for (int i = 1; i < exp.nbOperands(); i++) {
                final Object obj = this.evalExpValue(tuple, (ZExp) exp.getOperand(i));
                val = Math.pow(val, ((Number) obj).doubleValue());
            }
            return val;

        } else {
            throw new SQLException("ZConstans.evalNumericExp(): Unknown operator " + op);
        }
    }

    /**
     * Evaluate a numeric or string expression (example: a+1).
     * 
     * @param tuple
     *            The tuple on which to evaluate the expression
     * @param exp
     *            The expression to evaluate
     * @return The expression's value
     * @throws SQLException
     *             the sql exception
     */
    public Object evalExpValue(final ZTuple tuple, final ZExp exp) throws SQLException {

        Object o2 = null;

        if (exp instanceof ZConstant) {

            final ZConstant c = (ZConstant) exp;

            switch (c.getType()) {

            case ZConstant.COLUMNNAME:

                final Object o1 = tuple.getAttValue(c.getValue());
                if (o1 == null) {
                    throw new SQLException(ZCommonConstants.ZEVAL_EXP_VALUE_UNKNOWN_COLUMN + c.getValue());
                }
                try {
                    o2 = new Double(o1.toString());
                } catch (final NumberFormatException e) {
                    o2 = o1;
                }
                break;

            case ZConstant.NUMBER:
                o2 = new Double(c.getValue());
                break;

            case ZConstant.STRING:
            default:
                o2 = c.getValue();
                break;
            }
        } else if (exp instanceof ZExpression) {
            o2 = new Double(this.evalNumericExp(tuple, (ZExpression) exp));
        }
        return o2;
    }

    // CHECKSTYLE:OFF
    // FIXME extract to proper test module!!!
    // test
    public static void main(String args[]) {
        try {
            BufferedReader db = new BufferedReader(new FileReader("test.db"));
            String tpl = db.readLine();
            ZTuple t = new ZTuple(tpl);

            ZqlParser parser = new ZqlParser();
            ZEval evaluator = new ZEval();

            while ((tpl = db.readLine()) != null) {
                t.setRow(tpl);
                BufferedReader sql = new BufferedReader(new FileReader("test.sql"));
                String query;
                while ((query = sql.readLine()) != null) {
                    parser.initParser(new ByteArrayInputStream(query.getBytes()));
                    ZExp exp = parser.readExpression();
                    System.out.print(tpl + ", " + query + ", ");
                    System.out.println(evaluator.eval(t, exp));
                }
                sql.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // CHECKSTYLE:ON
}
