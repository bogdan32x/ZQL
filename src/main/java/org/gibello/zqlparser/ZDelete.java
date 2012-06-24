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

/**
 * ZDelete: an SQL DELETE statement.<br>
 * SQL Syntax: DELETE [from] table [where Expression];
 * 
 * @author Bogdan Mariesan, Romania
 */
public class ZDelete implements ZStatement {

	/**
	 * The serial version UID.
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * The table name.
	 */
	private String				table;

	/**
	 * The where clause.
	 */
	private ZExp				where				= null;

	/**
	 * Create a DELETE statement on a given table.
	 * 
	 * @param tab
	 *            the table name
	 */
	public ZDelete(final String tab) {
		this.table = new String(tab);
	}

	/**
	 * Add a WHERE clause to the DELETE statement.
	 * 
	 * @param where
	 *            An SQL expression compatible with a WHERE clause
	 */
	public void addWhere(final ZExp where) {
		this.where = where;
	}

	/**
	 * @return The table concerned by the DELETE statement.
	 */
	public String getTable() {
		return this.table;
	}

	/**
	 * @return The SQL Where clause of the DELETE statement (an SQL Expression or Subquery, compatible with an SQL WHERE
	 *         clause).
	 */
	public ZExp getWhere() {
		return this.where;
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer("delete ");
		if (this.where != null) {
			buf.append("from ");
		}
		buf.append(this.table);
		if (this.where != null) {
			buf.append(" where " + this.where.toString());
		}
		return buf.toString();
	}
};
