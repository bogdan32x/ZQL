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

import org.gibello.zql.expression.ZExp;

import java.io.Serializable;
import java.util.List;

/**
 * ZGroupBy: an SQL GROUP BY...HAVING clause.
 *
 * @author Pierre-Yves Gibello
 * @author Bogdan Mariesan, Romania
 */
public class ZGroupBy implements Serializable {

	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Group by clause.
	 */
	private List<?> groupBy;

	/**
	 * Having clause.
	 */
	private ZExp having = null;

	/**
	 * Create a GROUP BY given a set of Expressions.
	 *
	 * @param groupBy
	 * 		A vector of SQL Expressions (ZExp objects).
	 */
	public ZGroupBy(final List<?> groupBy) {
		this.groupBy = groupBy;
	}

	/**
	 * Get the GROUP BY expressions.
	 *
	 * @return A vector of SQL Expressions (ZExp objects)
	 */
	public List<?> getGroupBy() {
		return this.groupBy;
	}

	/**
	 * Get the HAVING clause.
	 *
	 * @return An SQL expression
	 */
	public ZExp getHaving() {
		return this.having;
	}

	/**
	 * Initiallize the HAVING part of the GROUP BY.
	 *
	 * @param having
	 * 		An SQL Expression (the HAVING clause)
	 */
	public void setHaving(final ZExp having) {
		this.having = having;
	}

	@Override public String toString() {
		final StringBuffer buf = new StringBuffer("group by ");

		// buf.append(groupby_.toString());
		buf.append(this.groupBy.get(0).toString());
		for (int i = 1; i < this.groupBy.size(); i++) {
			buf.append(", " + this.groupBy.get(i).toString());
		}
		if (this.having != null) {
			buf.append(" having " + this.having.toString());
		}
		return buf.toString();
	}
};
