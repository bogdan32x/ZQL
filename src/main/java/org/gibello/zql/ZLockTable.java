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

import java.util.List;

/**
 * ZLockTable: an SQL LOCK TABLE statement.
 * 
 * @author Pierre-Yves Gibello
 * @author Bogdan Mariesan, Romania
 */
public class ZLockTable implements ZStatement {

    /**
     * The default serial UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Is no wait.
     */
    private boolean noWait = false;

    /**
     * The lock mode.
     */
    private String lockMode = null;

    /**
     * The tables.
     */
    private List<?> tables = null;

    /**
     * Default constructor.
     */
    public ZLockTable() {

    }

    /**
     * @param tables
     *            the tables.
     */
    public void addTables(final List<?> tables) {
        this.tables = tables;
    }

    /**
     * @return the tables.
     */
    public List<?> getTables() {
        return this.tables;
    }

    /**
     * @param lc
     *            sets the lock mode.
     */
    public void setLockMode(final String lc) {
        this.lockMode = new String(lc);
    }

    /**
     * @return the lock mode.
     */
    public String getLockMode() {
        return this.lockMode;
    }

    /**
     * @return is no wait.
     */
    public boolean isNowait() {
        return this.noWait;
    }

    /**
     * @param noWait
     *            is no wait.
     */
    public void setNowait(final boolean noWait) {
        this.noWait = noWait;
    }
};
