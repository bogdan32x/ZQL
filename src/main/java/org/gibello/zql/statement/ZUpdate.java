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

package org.gibello.zql.statement;

import org.gibello.zql.expression.ZExp;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * ZUpdate: an SQL UPDATE statement.
 *
 * @author Pierre-Yves Gibello
 * @author Bogdan Mariesan, Romania
 */
public class ZUpdate implements ZStatement {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * The table name.
     */
    private String tableName;

    /**
     * The table alias.
     */
    private String tableAlias = null;

    /**
     * Set values.
     */
    private Hashtable<String, ZExp> setValues;

    /**
     * Where expression.
     */
    private ZExp whereClause = null;

    /**
     * Columns vector.
     */
    private Vector<String> tableColumns = null;

    /**
     * Create an UPDATE statement on a given table.
     *
     * @param tableName the table name.
     */
    public ZUpdate(final String tableName) {
        this.tableName = tableName;
    }

    /**
     * Gets the table name.
     *
     * @return the table name.
     */
    public String getTable() {
        return this.tableName;
    }

    /**
     * Gets the table alias.
     *
     * @return the table alias.
     */
    public String getAlias() {
        return this.tableAlias;
    }

    /**
     * Set table alias.
     *
     * @param tableAlias the table alias.
     */
    public void setAlias(final String tableAlias) {
        this.tableAlias = tableAlias;
    }

    /**
     * Insert a SET... clause in the UPDATE statement
     *
     * @param setValues A Hashtable, where keys are column names (the columns to update), and values are ZExp objects (the column values). For
     *                  example, the values may be ZConstant objects (like "Smith") or more complex SQL Expressions.
     */
    public void addSet(final Hashtable<String, ZExp> setValues) {
        this.setValues = setValues;
    }

    /**
     * Get the whole SET... clause
     *
     * @return A Hashtable, where keys are column names (the columns to update), and values are ZExp objects (Expressions that specify
     * column values: for example, ZConstant objects like "Smith").
     */
    public Hashtable<String, ZExp> getSet() {
        return this.setValues;
    }

    /**
     * Add one column=value pair to the SET... clause This method also keeps track of the column order
     *
     * @param col The column name
     * @param val The column value
     */
    public void addColumnUpdate(final String col, final ZExp val) {
        if (this.setValues == null) {
            this.setValues = new Hashtable<>();
        }

        this.setValues.put(col, val);

        if (this.tableColumns == null) {
            this.tableColumns = new Vector<>();
        }

        this.tableColumns.addElement(col);
    }

    /**
     * Get the SQL expression that specifies a given column's update value. (for example, a ZConstant object like "Smith").
     *
     * @param col The column name.
     * @return a ZExp, like a ZConstant representing a value, or a more complex SQL expression.
     */
    public ZExp getColumnUpdate(final String col) {
        return this.setValues.get(col);
    }

    /**
     * Get the SQL expression that specifies a given column's update value. (for example, a ZConstant object like "Smith").<br>
     * WARNING: This method will work only if column/value pairs have been inserted using addColumnUpdate() - otherwise it is not possible
     * to guess what the right order is, and null will be returned.
     *
     * @param index The column index (starting from 1).
     * @return a ZExp, like a ZConstant representing a value, or a more complex SQL expression.
     */
    public ZExp getColumnUpdate(final int index) {
        ZExp result;

        final String col = this.tableColumns.elementAt(index);
        result = this.setValues.get(col);

        return result;
    }

    /**
     * Get the column name that corresponds to a given index.<br>
     * WARNING: This method will work only if column/value pairs have been inserted using addColumnUpdate() - otherwise it is not possible
     * to guess what the right order is, and null will be returned.
     *
     * @param index The column index (starting from 1).
     * @return The corresponding column name.
     */
    public String getColumnUpdateName(final int index) {
        return this.tableColumns.elementAt(index);
    }

    /**
     * Returns the number of column/value pairs in the SET... clause.
     *
     * @return the number of pairs in the SET clause.
     */
    public int getColumnUpdateCount() {
        return this.setValues.size();
    }

    /**
     * Insert a WHERE... clause in the UPDATE statement
     *
     * @param whereExpr An SQL Expression compatible with a WHERE... clause.
     */
    public void addWhere(final ZExp whereExpr) {
        this.whereClause = whereExpr;
    }

    /**
     * Get the WHERE clause of this UPDATE statement.
     *
     * @return An SQL Expression compatible with a WHERE... clause.
     */
    public ZExp getWhere() {
        return this.whereClause;
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder("update " + this.tableName);
        if (this.tableAlias != null) {
            buf.append(" ").append(this.tableAlias);
        }

        buf.append(" set ");

        Enumeration<String> e;
        if (this.tableColumns != null) {
            e = this.tableColumns.elements();
        } else {
            e = this.setValues.keys();
        }

        boolean first = true;
        while (e.hasMoreElements()) {
            final String key = e.nextElement();

            if (!first) {
                buf.append(", ");
            }

            buf.append(key).append("=").append(this.setValues.get(key).toString());
            first = false;
        }

        if (this.whereClause != null) {
            buf.append(" where ").append(this.whereClause.toString());
        }

        return buf.toString();
    }
}
