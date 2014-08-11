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
 * ZQuery: an SQL SELECT statement.
 * 
 * @author Bogdan Mariesan, Romania
 */
public class ZQuery implements ZStatement, ZExp {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * Select query.
     */
    private Vector<?> select;

    /**
     * Distinct clause.
     */
    private boolean distinct = false;

    /**
     * From clause.
     */
    private Vector<?> from;

    /**
     * Where clause.
     */
    private ZExp where = null;

    /**
     * Group by clause.
     */
    private ZGroupBy groupby = null;

    /**
     * Set clause.
     */
    private ZExpression setclause = null;

    /**
     * Order by clause.
     */
    private Vector<?> orderby = null;

    /**
     * For update clause.
     */
    private boolean forupdate = false;

    /**
     * Create a new SELECT statement.
     */
    public ZQuery() {

    }

    /**
     * Insert the SELECT part of the statement.
     * 
     * @param select
     *            A vector of ZSelectItem objects
     */
    public void addSelect(final Vector<?> select) {
        this.select = select;
    }

    /**
     * Insert the FROM part of the statement.
     * 
     * @param from
     *            a Vector of ZFromItem objects
     */
    public void addFrom(final Vector<?> from) {
        this.from = from;
    }

    /**
     * Insert a WHERE clause.
     * 
     * @param where
     *            An SQL Expression
     */
    public void addWhere(final ZExp where) {
        this.where = where;
    }

    /**
     * Insert a GROUP BY...HAVING clause.
     * 
     * @param groupby
     *            A GROUP BY...HAVING clause
     */
    public void addGroupBy(final ZGroupBy groupby) {
        this.groupby = groupby;
    }

    /**
     * Insert a SET clause (generally UNION, INTERSECT or MINUS).
     * 
     * @param setclause
     *            An SQL Expression (generally UNION, INTERSECT or MINUS)
     */
    public void addSet(final ZExpression setclause) {
        this.setclause = setclause;
    }

    /**
     * Insert an ORDER BY clause.
     * 
     * @param orderby
     *            A vector of ZOrderBy objects
     */
    public void addOrderBy(final Vector<?> orderby) {
        this.orderby = orderby;
    }

    /**
     * Get the SELECT part of the statement.
     * 
     * @return A vector of ZSelectItem objects
     */
    public Vector<?> getSelect() {
        return this.select;
    }

    /**
     * Get the FROM part of the statement.
     * 
     * @return A vector of ZFromItem objects
     */
    public Vector<?> getFrom() {
        return this.from;
    }

    /**
     * Get the WHERE part of the statement.
     * 
     * @return An SQL Expression or sub-query (ZExpression or ZQuery object)
     */
    public ZExp getWhere() {
        return this.where;
    }

    /**
     * Get the GROUP BY...HAVING part of the statement.
     * 
     * @return A GROUP BY...HAVING clause
     */
    public ZGroupBy getGroupBy() {
        return this.groupby;
    }

    /**
     * Get the SET clause (generally UNION, INTERSECT or MINUS).
     * 
     * @return An SQL Expression (generally UNION, INTERSECT or MINUS)
     */
    public ZExpression getSet() {
        return this.setclause;
    }

    /**
     * Get the ORDER BY clause.
     * 
     * @return A vector of ZOrderBy objects
     */
    public Vector<?> getOrderBy() {
        return this.orderby;
    }

    /**
     * @return true if it is a SELECT DISTINCT query, false otherwise.
     */
    public boolean isDistinct() {
        return this.distinct;
    }

    /**
     * @return true if it is a FOR UPDATE query, false otherwise.
     */
    public boolean isForUpdate() {
        return this.forupdate;
    }

    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer("select ");
        if (this.distinct) {
            buf.append("distinct ");
        }

        // buf.append(select_.toString());
        int i;
        buf.append(this.select.elementAt(0).toString());
        for (i = 1; i < this.select.size(); i++) {
            buf.append(ZCommonConstants.COMMA + ZCommonConstants.EMPTY_STRING + this.select.elementAt(i).toString());
        }

        // buf.append(" from " + from_.toString());
        buf.append(" from ");
        buf.append(this.from.elementAt(0).toString());
        for (i = 1; i < this.from.size(); i++) {
            buf.append(ZCommonConstants.COMMA + ZCommonConstants.EMPTY_STRING + this.from.elementAt(i).toString());
        }

        if (this.where != null) {
            buf.append(" where " + this.where.toString());
        }
        if (this.groupby != null) {
            buf.append(ZCommonConstants.EMPTY_STRING + this.groupby.toString());
        }
        if (this.setclause != null) {
            buf.append(ZCommonConstants.EMPTY_STRING + this.setclause.toString());
        }
        if (this.orderby != null) {
            buf.append(" order by ");
            // buf.append(orderby_.toString());
            buf.append(this.orderby.elementAt(0).toString());
            for (i = 1; i < this.orderby.size(); i++) {
                buf.append(", " + this.orderby.elementAt(i).toString());
            }
        }
        if (this.forupdate) {
            buf.append(" for update");
        }

        return buf.toString();
    }

    /**
     * @return group by.
     */
    public ZGroupBy getGroupby() {
        return this.groupby;
    }

    /**
     * @param groupby
     *            the group by.
     */
    public void setGroupby(final ZGroupBy groupby) {
        this.groupby = groupby;
    }

    /**
     * @return the set clause.
     */
    public ZExpression getSetclause() {
        return this.setclause;
    }

    /**
     * @param setclause
     *            the set clause.
     */
    public void setSetclause(final ZExpression setclause) {
        this.setclause = setclause;
    }

    /**
     * @return get order by.
     */
    public Vector<?> getOrderby() {
        return this.orderby;
    }

    /**
     * @param orderby
     *            set order by.
     */
    public void setOrderby(final Vector<?> orderby) {
        this.orderby = orderby;
    }

    /**
     * @return is for update.
     */
    public boolean isForupdate() {
        return this.forupdate;
    }

    /**
     * @param forupdate
     *            set for update.
     */
    public void setForupdate(final boolean forupdate) {
        this.forupdate = forupdate;
    }

    /**
     * @param select
     *            set select.
     */
    public void setSelect(final Vector<?> select) {
        this.select = select;
    }

    /**
     * @param distinct
     *            set distinct.
     */
    public void setDistinct(final boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * @param from
     *            set from.
     */
    public void setFrom(final Vector<?> from) {
        this.from = from;
    }

    /**
     * @param where
     *            set where clause.
     */
    public void setWhere(final ZExp where) {
        this.where = where;
    }

};
