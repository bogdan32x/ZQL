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

import java.util.Vector;

import org.gibello.zql.utils.ZCommonConstants;

/**
 * ZInsert: an SQL INSERT statement.
 * 
 * @author Pierre-Yves Gibello
 * @author Bogdan Mariesan, Romania
 */
public class ZInsert implements ZStatement {

    /**
     * The default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The table name.
     */
    private String tableName;

    /**
     * The table columns.
     */
    private Vector<ZExp> tableColumns = null;

    /**
     * The specified values.
     */
    private ZExp specifiedValues = null;

    /**
     * Create an INSERT statement on a given table.
     * 
     * @param tableName
     *            the table name.
     */
    public ZInsert(final String tableName) {
        this.tableName = new String(tableName);
    }

    /**
     * Get the name of the table involved in the INSERT statement.
     * 
     * @return A String equal to the table name
     */
    public String getTable() {
        return this.tableName;
    }

    /**
     * Get the columns involved in the INSERT statement.
     * 
     * @return A Vector of Strings equal to the column names
     */
    public Vector<ZExp> getColumns() {
        return this.tableColumns;
    }

    /**
     * Specify which columns to insert.
     * 
     * @param tableColumns
     *            A vector of column names (Strings)
     */
    public void addColumns(final Vector<ZExp> tableColumns) {
        this.tableColumns = tableColumns;
    }

    /**
     * Specify the VALUES part or SQL sub-query of the INSERT statement.
     * 
     * @param specifiedValues
     *            An SQL expression or a SELECT statement. If it is a list of SQL expressions, e should be represented by ONE SQL expression
     *            with operator = "," and operands = the expressions in the list. If it is a SELECT statement, e should be a ZQuery object.
     */
    public void addValueSpec(final ZExp specifiedValues) {
        this.specifiedValues = specifiedValues;
    }

    /**
     * Get the VALUES part of the INSERT statement.
     * 
     * @return A vector of SQL Expressions (ZExp objects); If there's no VALUES but a subquery, returns null (use getQuery() method).
     */
    public Vector<ZExp> getValues() {

        Vector<ZExp> result;

        if (!(this.specifiedValues instanceof ZExpression)) {
            result = null;
        }
        result = ((ZExpression) this.specifiedValues).getOperands();

        return result;
    }

    /**
     * Get the sub-query (ex. in INSERT INTO table1 SELECT * FROM table2;, the sub-query is SELECT * FROM table2;)
     * 
     * @return A ZQuery object (A SELECT statement), or null if there's no sub-query (in that case, use the getValues() method to get the
     *         VALUES part).
     */
    public ZQuery getQuery() {
        ZQuery result;

        if (!(this.specifiedValues instanceof ZQuery)) {
            result = null;
        }
        result = (ZQuery) this.specifiedValues;

        return result;
    }

    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer("insert into " + this.tableName);
        if (this.tableColumns != null && this.tableColumns.size() > 0) {
            // buf.append(" " + columns_.toString());
            buf.append(ZCommonConstants.LEFT_BRACKET + this.tableColumns.elementAt(0));
            for (int i = 1; i < this.tableColumns.size(); i++) {
                buf.append("," + this.tableColumns.elementAt(i));
            }
            buf.append(ZCommonConstants.RIGHT_BRACKET);
        }

        final String vlist = this.specifiedValues.toString();
        buf.append(ZCommonConstants.EMPTY_STRING);
        if (this.getValues() != null) {
            buf.append("values ");
        }
        if (vlist.startsWith(ZCommonConstants.LEFT_BRACKET)) {
            buf.append(vlist);
        } else {
            buf.append(ZCommonConstants.EMPTY_STRING + ZCommonConstants.LEFT_BRACKET + vlist + ZCommonConstants.RIGHT_BRACKET);
        }

        return buf.toString();
    }
};
