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

import org.gibello.zqlparser.ZConstant;
import org.gibello.zqlparser.ZExp;
import org.gibello.zqlparser.ZExpression;
import org.gibello.zqlparser.ZqlParser;

/**
 * Evaluate SQL expressions.
 * 
 * @author Bogdan Mariesan, Romania
 */
public class ZEval {

	/**
	 * Unknown column error.
	 */
	private static final String	ZEVAL_EXP_VALUE_UNKNOWN_COLUMN						= "ZEval.evalExpValue(): unknown column ";

	/**
	 * Slash string.
	 */
	private static final String	SLASH_STRING										= "/";

	/**
	 * Multiplication string.
	 */
	private static final String	MULTIPLICATION_STRING								= "*";

	/**
	 * Minus string.
	 */
	private static final String	MINUS_STRING										= "-";

	/**
	 * Plus string.
	 */
	private static final String	PLUS_STRING											= "+";

	/**
	 * Expression not numeric error.
	 */
	private static final String	ZEVAL_NUMERIC_EXP_EXPRESSION_NOT_NUMERIC			= "ZEval.evalNumericExp(): expression not numeric";

	/**
	 * Like can only compare strings error.
	 */
	private static final String	ZEVAL_LIKE_LIKE_CAN_ONLY_COMPARE_STRINGS			= "ZEval.evalLike(): LIKE can only compare strings";

	/**
	 * Prefix string.
	 */
	private static final String	PREFIX												= "%";

	/**
	 * Trying to compare more than two values error.
	 */
	private static final String	ZEVAL_CMP_TRYING_TO_COMPARE_MORE_THAN_TWO_VALUES	= "ZEval.evalCmp(): Trying to compare more than two values";

	/**
	 * TWO string.
	 */
	private static final int	TWO													= 2;

	/**
	 * Trying to compare less than two values error.
	 */
	private static final String	ZEVAL_CMP_TRYING_TO_COMPARE_LESS_THAN_TWO_VALUES	= "ZEval.evalCmp(): Trying to compare less than two values";

	/**
	 * Unknown operator error.
	 */
	private static final String	ZEVAL_UNKNOWN_OPERATOR								= "ZEval.eval(): Unknown operator ";

	/**
	 * Is not null string.
	 */
	private static final String	IS_NOT_NULL_STRING									= "IS NOT NULL";

	/**
	 * Can't evaluate IS NOT NULL error.
	 */
	private static final String	ZEVAL_CAN_T_EVAL_IS_NOT_NULL						= "ZEval.eval(): can't eval IS (NOT) NULL";

	/**
	 * Is null string.
	 */
	private static final String	IS_NULL_STRING										= "IS NULL";

	/**
	 * Not in string.
	 */
	private static final String	NOT_IN_STRING										= "NOT IN";

	/**
	 * In string.
	 */
	private static final String	IN_STRING											= "IN";

	/**
	 * Not like string.
	 */
	private static final String	NOT_LIKE_STRING										= "NOT LIKE";

	/**
	 * Like string.
	 */
	private static final String	LIKE_STRING											= "LIKE";

	/**
	 * Not between string.
	 */
	private static final String	NOT_BETWEEN											= "NOT BETWEEN";

	/**
	 * Between string.
	 */
	private static final String	BETWEEN												= "BETWEEN";

	/**
	 * Lesser than string.
	 */
	private static final String	LESSER_THAN_STRING									= "<";

	/**
	 * Greather than string.
	 */
	private static final String	GREATHER_THAN_STRING								= ">";

	/**
	 * Operator # not supported error.
	 */
	private static final String	ZEVAL_OPERATOR_NOT_SUPPORTED						= "ZEval.eval(): Operator # not supported";

	/**
	 * Diez string.
	 */
	private static final String	DIEZ_STRING											= "#";

	/**
	 * Exclusion string.
	 */
	private static final String	EXCLUDING_STRING									= "<>";

	/**
	 * Not equals string.
	 */
	private static final String	NOT_EQUALS_STRING									= "!=";

	/**
	 * Equals string.
	 */
	private static final String	EQUALS_STRING										= "=";

	/**
	 * Not string.
	 */
	private static final String	NOT_STRING											= "NOT";

	/**
	 * ZEval null argument or operator.
	 */
	private static final String	ZEVAL_NULL_ARGUMENT_OR_OPERATOR						= "ZEval.eval(): null argument or operator";

	/**
	 * ZEval only expressions are supported.
	 */
	private static final String	ZEVAL_ONLY_EXPRESSIONS_ARE_SUPPORTED				= "ZEval.eval(): only expressions are supported";

	/**
	 * And string.
	 */
	private static final String	AND_STRING											= "AND";

	/**
	 * Or string.
	 */
	private static final String	OR_STING											= "OR";

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
			throw new SQLException(ZEval.ZEVAL_NULL_ARGUMENT_OR_OPERATOR);
		}

		if (!(exp instanceof ZExpression)) {
			throw new SQLException(ZEval.ZEVAL_ONLY_EXPRESSIONS_ARE_SUPPORTED);
		}

		final ZExpression pred = (ZExpression) exp;
		final String op = pred.getOperator();

		if (op.equals(ZEval.AND_STRING)) {
			boolean and = true;
			for (int i = 0; i < pred.nbOperands(); i++) {
				and &= this.eval(tuple, pred.getOperand(i));
			}
			return and;
		} else
			if (op.equals(ZEval.OR_STING)) {
				boolean or = false;
				for (int i = 0; i < pred.nbOperands(); i++) {
					or |= this.eval(tuple, pred.getOperand(i));
				}
				return or;
			} else
				if (op.equals(ZEval.NOT_STRING)) {
					return !this.eval(tuple, pred.getOperand(0));

				} else
					if (op.equals(ZEval.EQUALS_STRING)) {
						return evalCmp(tuple, pred.getOperands()) == 0;
					} else
						if (op.equals(ZEval.NOT_EQUALS_STRING)) {
							return evalCmp(tuple, pred.getOperands()) != 0;
						} else
							if (op.equals(ZEval.EXCLUDING_STRING)) {
								return evalCmp(tuple, pred.getOperands()) != 0;
							} else
								if (op.equals(ZEval.DIEZ_STRING)) {
									throw new SQLException(ZEval.ZEVAL_OPERATOR_NOT_SUPPORTED);
								} else
									if (op.equals(ZEval.GREATHER_THAN_STRING)) {
										return evalCmp(tuple, pred.getOperands()) > 0;
									} else
										if (op.equals(ZEval.GREATHER_THAN_STRING + ZEval.EQUALS_STRING)) {
											return evalCmp(tuple, pred.getOperands()) >= 0;
										} else
											if (op.equals(ZEval.LESSER_THAN_STRING)) {
												return evalCmp(tuple, pred.getOperands()) < 0;
											} else
												if (op.equals(ZEval.LESSER_THAN_STRING + ZEval.EQUALS_STRING)) {
													return evalCmp(tuple, pred.getOperands()) <= 0;

												} else
													if (op.equals(ZEval.BETWEEN) || op.equals(ZEval.NOT_BETWEEN)) {

														// Between: borders included
														final ZExpression newexp = new ZExpression(AND_STRING,
																new ZExpression(ZEval.GREATHER_THAN_STRING
																		+ ZEval.EQUALS_STRING, pred.getOperand(0),
																		pred.getOperand(1)), new ZExpression(
																		ZEval.LESSER_THAN_STRING + ZEval.EQUALS_STRING,
																		pred.getOperand(0), pred.getOperand(2)));

														if (op.equals(ZEval.NOT_BETWEEN)) {
															return !this.eval(tuple, newexp);
														} else {
															return this.eval(tuple, newexp);
														}

													} else
														if (op.equals(ZEval.LIKE_STRING)
																|| op.equals(ZEval.NOT_LIKE_STRING)) {
															final boolean like = this.evalLike(tuple,
																	pred.getOperands());
															return op.equals(ZEval.LIKE_STRING) ? like : !like;

														} else
															if (op.equals(ZEval.IN_STRING)
																	|| op.equals(ZEval.NOT_IN_STRING)) {

																final ZExpression newexp = new ZExpression(
																		ZEval.OR_STING);

																for (int i = 1; i < pred.nbOperands(); i++) {
																	newexp.addOperand(new ZExpression(
																			ZEval.EQUALS_STRING, pred.getOperand(0),
																			pred.getOperand(i)));
																}

																if (op.equals(ZEval.NOT_IN_STRING)) {
																	return !this.eval(tuple, newexp);
																} else {
																	return this.eval(tuple, newexp);
																}

															} else
																if (op.equals(ZEval.IS_NULL_STRING)) {

																	if (pred.nbOperands() <= 0
																			|| pred.getOperand(0) == null) {
																		return true;
																	}
																	final ZExp x = pred.getOperand(0);
																	if (x instanceof ZConstant) {
																		return ((ZConstant) x).getType() == ZConstant.NULL;
																	} else {
																		throw new SQLException(
																				ZEval.ZEVAL_CAN_T_EVAL_IS_NOT_NULL);
																	}

																} else
																	if (op.equals(ZEval.IS_NOT_NULL_STRING)) {

																		final ZExpression x = new ZExpression(
																				ZEval.IS_NULL_STRING);
																		x.setOperands(pred.getOperands());
																		return !this.eval(tuple, x);

																	} else {
																		throw new SQLException(
																				ZEval.ZEVAL_UNKNOWN_OPERATOR + op);
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

		if (operands.size() < ZEval.TWO) {
			throw new SQLException(ZEval.ZEVAL_CMP_TRYING_TO_COMPARE_LESS_THAN_TWO_VALUES);
		}
		if (operands.size() > ZEval.TWO) {
			throw new SQLException(ZEval.ZEVAL_CMP_TRYING_TO_COMPARE_MORE_THAN_TWO_VALUES);
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
			throw new SQLException("ZEval.evalCmp(): can't compare (" + o1.toString() + ") with (" + o2.toString()
					+ ")");
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
		if (operands.size() < ZEval.TWO) {
			throw new SQLException(ZEval.ZEVAL_CMP_TRYING_TO_COMPARE_LESS_THAN_TWO_VALUES);
		}
		if (operands.size() > ZEval.TWO) {
			throw new SQLException(ZEval.ZEVAL_CMP_TRYING_TO_COMPARE_MORE_THAN_TWO_VALUES);
		}

		final Object o1 = evalExpValue(tuple, (ZExp) operands.elementAt(0));
		final Object o2 = evalExpValue(tuple, (ZExp) operands.elementAt(1));

		if ((o1 instanceof String) && (o2 instanceof String)) {
			final String s1 = (String) o1;
			final String s2 = (String) o2;
			if (s2.startsWith(ZEval.PREFIX)) {
				return s1.endsWith(s2.substring(1));
			} else
				if (s2.endsWith(ZEval.PREFIX)) {
					return s1.startsWith(s2.substring(0, s2.length() - 1));
				} else {
					return s1.equalsIgnoreCase(s2);
				}
		} else {
			throw new SQLException(ZEval.ZEVAL_LIKE_LIKE_CAN_ONLY_COMPARE_STRINGS);
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
			throw new SQLException(ZEval.ZEVAL_NULL_ARGUMENT_OR_OPERATOR);
		}

		final String op = exp.getOperator();

		final Object o1 = this.evalExpValue(tuple, (ZExp) exp.getOperand(0));
		if (!(o1 instanceof Double)) {
			throw new SQLException(ZEval.ZEVAL_NUMERIC_EXP_EXPRESSION_NOT_NUMERIC);
		}

		final Double dobj = (Double) o1;

		if (op.equals(ZEval.PLUS_STRING)) {

			double val = dobj.doubleValue();
			for (int i = 1; i < exp.nbOperands(); i++) {
				final Object obj = this.evalExpValue(tuple, (ZExp) exp.getOperand(i));
				val += ((Number) obj).doubleValue();
			}
			return val;

		} else
			if (op.equals(ZEval.MINUS_STRING)) {

				double val = dobj.doubleValue();
				if (exp.nbOperands() == 1) {
					return -val;
				}
				for (int i = 1; i < exp.nbOperands(); i++) {
					final Object obj = this.evalExpValue(tuple, (ZExp) exp.getOperand(i));
					val -= ((Number) obj).doubleValue();
				}
				return val;

			} else
				if (op.equals(ZEval.MULTIPLICATION_STRING)) {

					double val = dobj.doubleValue();
					for (int i = 1; i < exp.nbOperands(); i++) {
						final Object obj = this.evalExpValue(tuple, (ZExp) exp.getOperand(i));
						val *= ((Number) obj).doubleValue();
					}
					return val;

				} else
					if (op.equals(ZEval.SLASH_STRING)) {

						double val = dobj.doubleValue();
						for (int i = 1; i < exp.nbOperands(); i++) {
							final Object obj = this.evalExpValue(tuple, (ZExp) exp.getOperand(i));
							val /= ((Number) obj).doubleValue();
						}
						return val;

					} else
						if (op.equals(ZEval.MULTIPLICATION_STRING + ZEval.MULTIPLICATION_STRING)) {

							double val = dobj.doubleValue();
							for (int i = 1; i < exp.nbOperands(); i++) {
								final Object obj = this.evalExpValue(tuple, (ZExp) exp.getOperand(i));
								val = Math.pow(val, ((Number) obj).doubleValue());
							}
							return val;

						} else {
							throw new SQLException("ZEval.evalNumericExp(): Unknown operator " + op);
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
						throw new SQLException(ZEval.ZEVAL_EXP_VALUE_UNKNOWN_COLUMN + c.getValue());
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
		} else
			if (exp instanceof ZExpression) {
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
