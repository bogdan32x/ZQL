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
 * along with Zql.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.gibello.zqlparser;

import java.util.Vector;

/**
 * ZExpression: an SQL Expression An SQL expression is an operator and one or more operands Example: a AND b AND c ->
 * operator = AND, operands = (a, b, c).
 * 
 * @author Bogdan Mariesan, Romania
 */
public class ZExpression implements ZExp {

	/**
	 * Serial version UID.
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * The operator.
	 */
	private String				operator			= null;

	/**
	 * The list of operands.
	 */
	private Vector<ZExp>		operands			= null;

	/**
	 * Create an SQL Expression given the operator.
	 * 
	 * @param operator
	 *            The operator
	 */
	public ZExpression(final String operator) {
		this.operator = new String(operator);
	}

	/**
	 * Create an SQL Expression given the operator and 1st operand.
	 * 
	 * @param operator
	 *            The operator
	 * @param firstOperand
	 *            The 1st operand
	 */
	public ZExpression(final String operator, final ZExp firstOperand) {
		this.operator = new String(operator);
		this.addOperand(firstOperand);
	}

	/**
	 * Create an SQL Expression given the operator, 1st and 2nd operands.
	 * 
	 * @param operator
	 *            The operator
	 * @param secondOperand
	 *            The 1st operand
	 * @param thirdOperand
	 *            The 2nd operand
	 */
	public ZExpression(final String operator, final ZExp secondOperand, final ZExp thirdOperand) {
		this.operator = new String(operator);
		this.addOperand(secondOperand);
		this.addOperand(thirdOperand);
	}

	/**
	 * Get this expression's operator.
	 * 
	 * @return the operator.
	 */
	public String getOperator() {
		return this.operator;
	}

	/**
	 * Set the operands list.
	 * 
	 * @param operands
	 *            A vector that contains all operands (ZExp objects).
	 */
	public void setOperands(final Vector<ZExp> operands) {
		this.operands = operands;
	}

	/**
	 * Get this expression's operands.
	 * 
	 * @return the operands (as a Vector of ZExp objects).
	 */
	public Vector<ZExp> getOperands() {
		return this.operands;
	}

	/**
	 * Add an operand to the current expression.
	 * 
	 * @param operand
	 *            The operand to add.
	 */
	public void addOperand(final ZExp operand) {
		if (this.operands == null) {
			this.operands = new Vector<ZExp>();
		}
		this.operands.addElement(operand);
	}

	/**
	 * Get an operand according to its index (position).
	 * 
	 * @param pos
	 *            The operand index, starting at 0.
	 * @return The operand at the specified index, null if out of bounds.
	 */
	public ZExp getOperand(final int pos) {

		ZExp result;

		if (this.operands == null || pos >= this.operands.size()) {
			result = null;
		}

		result = this.operands.elementAt(pos);

		return result;
	}

	/**
	 * Get the number of operands.
	 * 
	 * @return The number of operands
	 */
	public int nbOperands() {
		int result;

		if (this.operands == null) {
			result = 0;
		}

		result = this.operands.size();
		return result;
	}

	/**
	 * String form of the current expression (reverse polish notation). Example: a > 1 AND b = 2 -> (AND (> a 1) (= b
	 * 2))
	 * 
	 * @return The current expression in reverse polish notation (a String)
	 */
	public String toReversePolish() {
		final StringBuffer buf = new StringBuffer("(");
		buf.append(this.operator);
		for (int i = 0; i < this.nbOperands(); i++) {
			final ZExp opr = this.getOperand(i);
			if (opr instanceof ZExpression) {
				// Warning recursive call
				buf.append(" " + ((ZExpression) opr).toReversePolish());
			} else
				if (opr instanceof ZQuery) {
					buf.append(" (" + opr.toString() + ")");
				} else {
					buf.append(" " + opr.toString());
				}
		}
		buf.append(")");
		return buf.toString();
	}

	@Override
	public String toString() {

		if (this.operator.equals("?")) {
			// For prepared columns ("?")
			return this.operator;
		}

		if (ZUtils.isCustomFunction(this.operator) >= 0) {
			return this.formatFunction();
		}

		final StringBuffer buf = new StringBuffer();
		if (needPar(this.operator)) {
			buf.append("(");
		}

		ZExp operand;
		switch (this.nbOperands()) {

			case 1:
				operand = this.getOperand(0);
				if (operand instanceof ZConstant) {
					// Operator may be an aggregate function (MAX, SUM...)
					if (ZUtils.isAggregate(this.operator)) {
						buf.append(this.operator + "(" + operand.toString() + ")");
					} else
						if (this.operator.equals("IS NULL") || this.operator.equals("IS NOT NULL")) {
							buf.append(operand.toString() + " " + this.operator);
						}
						// "," = list of values, here just one single value
						else
							if (this.operator.equals(",")) {
								buf.append(operand.toString());
							} else {
								buf.append(this.operator + " " + operand.toString());
							}
				} else
					if (operand instanceof ZQuery) {
						buf.append(this.operator + " (" + operand.toString() + ")");
					} else {
						if (this.operator.equals("IS NULL") || this.operator.equals("IS NOT NULL")) {
							buf.append(operand.toString() + " " + this.operator);
						}
						// "," = list of values, here just one single value
						else
							if (this.operator.equals(",")) {
								buf.append(operand.toString());
							} else {
								buf.append(this.operator + " " + operand.toString());
							}
					}
				break;

			case 3:
				if (this.operator.toUpperCase().endsWith("BETWEEN")) {
					buf.append(this.getOperand(0).toString() + " " + this.operator + " "
							+ this.getOperand(1).toString() + " AND " + this.getOperand(2).toString());
					break;
				}

			default:

				boolean inOperator = false;
				if (this.operator.equals("IN") || this.operator.equals("NOT IN")) {
					inOperator = true;
				}

				final int nb = this.nbOperands();
				for (int i = 0; i < nb; i++) {

					if (inOperator && i == 1) {
						buf.append(" " + this.operator + " (");
					}

					operand = this.getOperand(i);
					if (operand instanceof ZQuery && !inOperator) {
						buf.append("(" + operand.toString() + ")");
					} else {
						buf.append(operand.toString());
					}
					if (i < nb - 1) {
						if (this.operator.equals(",") || (inOperator && i > 0)) {
							buf.append(", ");
						} else
							if (!inOperator) {
								buf.append(" " + this.operator + " ");
							}
					}
				}
				if (inOperator) {
					buf.append(")");
				}
				break;
		}

		if (this.needPar(this.operator)) {
			buf.append(")");
		}

		return buf.toString();
	}

	/**
	 * Checks for special operators.
	 * 
	 * @param op
	 *            the operator.
	 * @return the result of the check.
	 */
	private boolean needPar(final String op) {
		final String tmp = op.toUpperCase();
		return !(tmp.equals("ANY") || tmp.equals("ALL") || tmp.equals("UNION") || ZUtils.isAggregate(tmp));
	}

	/**
	 * Formatting function.
	 * 
	 * @return the formatted string.
	 */
	private String formatFunction() {
		final StringBuffer b = new StringBuffer(this.operator + "(");
		final int nb = this.nbOperands();
		for (int i = 0; i < nb; i++) {
			b.append(this.getOperand(i).toString() + (i < nb - 1 ? "," : ""));
		}
		b.append(")");
		return b.toString();
	}
};
