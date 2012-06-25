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

package org.gibello.zqlparser;

/**
 * ZTransactStmt: an SQL statement that concerns database transactions (example: COMMIT, ROLLBACK, SET TRANSACTION).
 * 
 * @author Bogdan Mariesan, Romania
 */
public class ZTransactStmt implements ZStatement {

	/**
	 * Serial version UID.
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * The statement string.
	 */
	private String				statement;

	/**
	 * The comment.
	 */
	private String				comment				= null;

	/**
	 * Read only param.
	 */
	private boolean				readOnly			= false;

	/**
	 * Transaction statement constructor.
	 * 
	 * @param statement
	 *            the statement string.
	 */
	public ZTransactStmt(final String statement) {
		this.setStatement(new String(statement));
	}

	/**
	 * Sets the comment.
	 * 
	 * @param comment
	 *            the comment.
	 */
	public void setComment(final String comment) {
		this.comment = new String(comment);
	}

	/**
	 * Gets the comment.
	 * 
	 * @return the comment.
	 */
	public String getComment() {
		return this.comment;
	}

	/**
	 * Check if is read only.
	 * 
	 * @return check if is read only.
	 */
	public boolean isReadOnly() {
		return this.readOnly;
	}

	/**
	 * @return the statement
	 */
	public String getStatement() {
		return this.statement;
	}

	/**
	 * @param statement
	 *            the statement to set
	 */
	public void setStatement(final String statement) {
		this.statement = statement;
	}

	/**
	 * @param readOnly
	 *            sets read only.
	 */
	public void setReadOnly(final boolean readOnly) {
		this.readOnly = readOnly;
	}
};
