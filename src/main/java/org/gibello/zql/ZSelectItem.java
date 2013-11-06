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

package org.gibello.zql;

/**
 * ZSelectItem: an item in the SELECT part of an SQL query. (The SELECT part of a query is a Vector of ZSelectItem).
 * 
 * @author Bogdan Mariesan, Romania
 */
public class ZSelectItem extends ZAliasedName {

	/**
	 * Serial version UID.
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * The expression.
	 */
	private ZExp				expression			= null;

	/**
	 * The aggregate.
	 */
	private String				aggregate			= null;

	/**
	 * Create a new SELECT item.
	 */
	public ZSelectItem() {
		super();
	}

	/**
	 * Create a new SELECT item, given its name (for column names and wildcards).
	 * 
	 * @param fullname
	 *            A string that represents a column name or wildcard (example: a.*).
	 */
	public ZSelectItem(final String fullname) {
		super(fullname, ZAliasedName.FORM_COLUMN);
		// PY.Gibello 21 Apr 2001
		this.setAggregate(ZUtils.getAggregateCall(fullname));
	}

	/**
	 * @return An SQL Expression if this SELECT item is an expression, a ZConstant if it is a column name, null if it is
	 *         a wildcard
	 */
	public final ZExp getExpression() {
		ZExp result;

		if (isExpression()) {
			result = this.expression;
		} else
			if (this.isWildcard()) {
				result = null;
			} else {
				result = new ZConstant(getColumn(), ZConstant.COLUMNNAME);
			}

		return result;
	}

	/**
	 * Initialize this SELECT item as an SQL expression (not a column name nor wildcard) Example: SELECT a+b FROM
	 * table1; (a+b is an expression).
	 * 
	 * @param expression
	 *            the expression.
	 */
	public void setExpression(final ZExp expression) {
		this.expression = expression;
		this.setStrform(this.expression.toString());
	}

	/**
	 * @return true if this item is an SQL expression, false if not. (Example: SELECT a+b, c FROM num; -> a+b is an
	 *         expression, not c)
	 */
	public boolean isExpression() {
		boolean result = false;

		if (this.expression != null && this.expression instanceof ZExpression) {
			result = true;
		}
		return result;
	}

	/**
	 * Initialize an aggregate function on this item (generally SUM, AVG, MAX, MIN) Example: SELECT AVG(age) FROM
	 * people; -> The aggregate function is AVG.
	 * 
	 * @param aggregate
	 *            The name of the aggregate function (a String, like SUM, AVG, MAX, MIN)
	 */
	public void setAggregate(final String aggregate) {
		this.aggregate = aggregate;
	}

	/**
	 * If this item is an aggregate function, return the function name.
	 * 
	 * @return The name of an aggregate function (generally SUM, AVG, MAX, MIN), or null if there's no aggregate.
	 *         Example: SELECT name, AVG(age) FROM people; -> null for the "name" item, and "AVG" for the "AVG(age)"
	 *         item.
	 */
	public String getAggregate() {
		return this.aggregate;
	}

	/**
	 * TBD public String toString() { String agg = getAggregate(); if(agg == null) agg = ""; return agg +
	 * super.toString(); }
	 **/
};
