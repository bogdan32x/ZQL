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

package org.gibello.zql.alias;

import org.gibello.zql.utils.ZCommonConstants;

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * A name/alias association. <br>
 * Names can have two forms:
 * <ul>
 * <li>FORM_TABLE for table names ([schema.]table)</li>
 * <li>FORM_COLUMN for column names ([[schema.]table.]column)</li>
 * </ul>
 *
 * @author Pierre-Yves Gibello
 * @author Bogdan Mariesan, Romania
 */
public class ZAliasedName implements Serializable {

	/**
	 * From table clause.
	 */
	public static final int FORM_TABLE = 1;
	/**
	 * From column clause.
	 */
	public static final int FORM_COLUMN = 2;
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 *
	 */
	private String strform = "";
	/**
	 * The schema.
	 */
	private String schema = null;
	/**
	 * The table name.
	 */
	private String table = null;
	/**
	 * The column name.
	 */
	private String column = null;
	/**
	 * The alias.
	 */
	private String alias = null;
	/**
	 * Form column.
	 */
	private int formColumn = ZAliasedName.FORM_COLUMN;

	/**
	 * Default constructor.
	 */
	public ZAliasedName() {

	}

	/**
	 * Create a new ZAliasedName given it's full name.
	 *
	 * @param fullname
	 * 		The full name: [[schema.]table.]column
	 * @param form
	 * 		The name form (FORM_TABLE or FORM_COLUMN)
	 */
	public ZAliasedName(final String fullname, final int form) {

		this.formColumn = form;
		this.strform = new String(fullname);

		final StringTokenizer st = new StringTokenizer(fullname, ".");
		switch (st.countTokens()) {
		case ZCommonConstants.MAGIC_NUMBER_1:
			if (form == ZAliasedName.FORM_TABLE) {
				this.table = new String(st.nextToken());
			} else {
				this.column = new String(st.nextToken());
			}
			break;
		case ZCommonConstants.MAGIC_NUMBER_2:
			if (form == ZAliasedName.FORM_TABLE) {
				this.schema = new String(st.nextToken());
				this.table = new String(st.nextToken());
			} else {
				this.table = new String(st.nextToken());
				this.column = new String(st.nextToken());
			}
			break;
		case ZCommonConstants.MAGIC_NUMBER_3:
		default:
			this.schema = new String(st.nextToken());
			this.table = new String(st.nextToken());
			this.column = new String(st.nextToken());
			break;
		}

		if (this.schema != null) {
			this.schema = this.postProcess(this.schema);
		}

		if (this.table != null) {
			this.table = this.postProcess(this.table);
		}

		if (this.column != null) {
			this.column = this.postProcess(this.column);
		}
	}

	/**
	 * Post process.
	 *
	 * @param val
	 * 		the value.
	 *
	 * @return the trimmed query
	 */
	private String postProcess(final String val) {
		String result = null;

		if (val == null) {
			result = null;
		}
		if (val.indexOf(ZCommonConstants.LEFT_BRACKET) >= 0) {
			result = val.substring(val.lastIndexOf(ZCommonConstants.LEFT_BRACKET) + 1);
		}
		if (val.indexOf(ZCommonConstants.RIGHT_BRACKET) >= 0) {
			result = val.substring(0, val.indexOf(ZCommonConstants.RIGHT_BRACKET));
		}

		if (result == null && val != null) {
			result = val;
		}
		return result.trim();
	}

	@Override public String toString() {
		if (this.alias == null) {
			return this.strform;
		} else {
			return this.strform + " " + this.alias;
		}
	}

	/**
	 * @return If the name is of the form schema.table.column, returns the schema part
	 */
	public String getSchema() {
		return this.schema;
	}

	/**
	 * @param schema
	 * 		the schema.
	 */
	public void setSchema(final String schema) {
		this.schema = schema;
	}

	/**
	 * @return If the name is of the form [schema.]table.column, returns the schema part
	 */
	public String getTable() {
		return this.table;
	}

	/**
	 * @param table
	 * 		the table name.
	 */
	public void setTable(final String table) {
		this.table = table;
	}

	/**
	 * @return The name is of the form [[schema.]table.]column: return the column part
	 */
	public String getColumn() {
		return this.column;
	}

	/**
	 * @param column
	 * 		the column.
	 */
	public void setColumn(final String column) {
		this.column = column;
	}

	/**
	 * @return true if column is "*", false otherwise. Example: *, table.* are wildcards.
	 */
	public boolean isWildcard() {
		boolean result = false;

		if (this.formColumn == ZAliasedName.FORM_TABLE) {
			result = this.table != null && this.table.equals("*");
		} else {
			result = this.column != null && this.column.indexOf('*') >= 0;
		}

		return result;
	}

	/**
	 * @return the alias associated to the current name.
	 */
	public String getAlias() {
		return this.alias;
	}

	/**
	 * Associate an alias with the current name.
	 *
	 * @param alias
	 * 		the alias associated to the current name.
	 */
	public void setAlias(final String alias) {
		this.alias = new String(alias);
	}

	/**
	 * @return the string form.
	 */
	public String getStrform() {
		return this.strform;
	}

	/**
	 * @param strform
	 * 		the string form.
	 */
	public void setStrform(final String strform) {
		this.strform = strform;
	}

	/**
	 * @return the form column.
	 */
	public int getFormColumn() {
		return this.formColumn;
	}

	/**
	 * @param formColumn
	 * 		the form column.
	 */
	public void setFormColumn(final int formColumn) {
		this.formColumn = formColumn;
	}
}
