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

import java.io.Serializable;

/**
 * An SQL query ORDER BY clause.
 *
 * @author Pierre-Yves Gibello
 * @author Bogdan Mariesan, Romania
 */
public class ZOrderBy implements Serializable {

    /**
     * The default serial UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The expression.
     */
    private ZExp expression;

    /**
     * Ascending.
     */
    private boolean ascending = true;

    /**
     * Constructor.
     * 
     * @param expression
     *            the expression.
     */
    public ZOrderBy(final ZExp expression) {
        this.expression = expression;
    }

    /**
     * Set the order to ascending or descending (default is ascending order).
     * 
     * @param ascending
     *            true for ascending order, false for descending order.
     */
    public void setAscOrder(final boolean ascending) {
        this.ascending = ascending;
    }

    /**
     * Get the order (ascending or descending).
     * 
     * @return true if ascending order, false if descending order.
     */
    public boolean getAscOrder() {
        return this.ascending;
    }

    /**
     * Get the ORDER BY expression.
     * 
     * @return An expression (generally, a ZConstant that represents a column name).
     */
    public ZExp getExpression() {
        return this.expression;
    }

    @Override
    public String toString() {
        String ascending;

        if (this.ascending) {
            ascending = "ASC";
        } else {
            ascending = "DESC";
        }
        return this.expression.toString() + " " + ascending;
    }
};
