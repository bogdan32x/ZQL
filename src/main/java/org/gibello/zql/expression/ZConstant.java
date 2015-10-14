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

package org.gibello.zql.expression;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * ZConstant: a representation of SQL constants.
 *
 * @author Pierre-Yves Gibello
 * @author Bogdan Mariesan, Romania
 */
public class ZConstant implements ZExp {

    /**
     * ZConstant types.
     */
    public static final int UNKNOWN = -1;
    /**
     * ZConstant types.
     */
    public static final int COLUMNNAME = 0;
    /**
     * ZConstant types.
     */
    public static final int NULL = 1;
    /**
     * ZConstant types.
     */
    public static final int NUMBER = 2;
    /**
     * ZConstant types.
     */
    public static final int STRING = 3;
    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;
    /**
     * ZConstant types.
     */
    private int zconstantType = ZConstant.UNKNOWN;

    /**
     * Values.
     */
    private String values = null;

    /**
     * Create a new constant, given its name and type.
     *
     * @param values         the values.
     * @param zconstantTypes the types.
     */
    public ZConstant(final String values, final int zconstantTypes) {
        this.values = values;
        this.zconstantType = zconstantTypes;
    }

    /**
     * @return the constant value
     */
    public String getValue() {
        return this.values;
    }

    /**
     * @return the constant type
     */
    public int getType() {
        return this.zconstantType;
    }

    @Override
    public String toString() {
        String toString;

        if (this.zconstantType == ZConstant.STRING) {
            toString = '\'' + this.values + '\'';
        } else {
            toString = this.values;
        }

        return toString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ZConstant zConstant = (ZConstant) o;

        return new EqualsBuilder()
                .append(zconstantType, zConstant.zconstantType)
                .append(values, zConstant.values)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(zconstantType)
                .append(values)
                .toHashCode();
    }
}
