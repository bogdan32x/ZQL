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

package org.gibello.zql.data;

import org.gibello.zql.utils.ZCommonConstants;

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Handles tuples.
 *
 * @author Pierre-Yves Gibello
 * @author Bogdan Mariesan, Romania
 */
public class ZTuple {

    /**
     * the names of the attributes.
     */
    private Vector<String> attributes;

    /**
     * the values of the attributes.
     */
    private Vector<Object> values;

    /**
     * hashtable to locate attribute names more easily.
     */
    private Hashtable<String, Integer> searchTable;

    /**
     * The default constructor.
     */
    public ZTuple() {
        this.attributes = new Vector<String>();
        this.values = new Vector<Object>();
        this.searchTable = new Hashtable<String, Integer>();
    }

    /**
     * Create a new tuple, given it's column names.
     *
     * @param colnames Column names separated by commas (,).
     */
    public ZTuple(final String colnames) {
        this();
        final StringTokenizer st = new StringTokenizer(colnames, ZCommonConstants.COMMA);
        while (st.hasMoreTokens()) {
            this.setAtt(st.nextToken().trim(), null);
        }
    }

    /**
     * Set the current tuple's column values.
     *
     * @param row Column values separated by commas (,).
     */
    public void setRow(final String row) {
        final StringTokenizer st = new StringTokenizer(row, ZCommonConstants.COMMA);
        for (int i = 0; st.hasMoreTokens(); i++) {
            final String val = st.nextToken().trim();
            // try {
            final Double d = new Double(val);
            this.setAtt(this.getAttName(i), d);
            // } catch (Exception e) {
            // TODO check why try/catch is needed.
            this.setAtt(this.getAttName(i), val);
            // }
        }
    }

    /**
     * Set the current tuple's column values.
     *
     * @param row A vector of column values.
     */
    public void setRow(final Vector<?> row) {
        for (int i = 0; i < row.size(); i++) {
            this.setAtt(this.getAttName(i), row.elementAt(i));
        }
    }

    /**
     * Set the value of the given attribute name.
     *
     * @param name  the string representing the attribute name
     * @param value the Object representing the attribute value
     */
    public void setAtt(final String name, final Object value) {
        if (name != null) {
            final boolean exist = this.searchTable.containsKey(name);

            if (exist) {
                final int i = this.searchTable.get(name);
                this.values.setElementAt(value, i);
            } else {
                final int i = this.attributes.size();
                this.attributes.addElement(name);
                this.values.addElement(value);
                this.searchTable.put(name, i);
            }
        }
    }

    /**
     * Return the name of the attribute corresponding to the index.
     *
     * @param index integer giving the index of the attribute
     * @return a String
     */
    public String getAttName(final int index) {
        String getAttributeName;

        try {
            getAttributeName = this.attributes.elementAt(index);
        } catch (final ArrayIndexOutOfBoundsException e) {
            getAttributeName = null;
        }

        return getAttributeName;
    }

    /**
     * Return the index of the attribute corresponding to the name.
     *
     * @param name the name of the requested attribute
     * @return the index as an int, -1 if name is not an attribute
     */
    public int getAttIndex(final String name) {

        int result = 0;

        if (name == null) {
            result = -1;
        }

        final Integer index = this.searchTable.get(name);

        if (index != null) {
            result = index;
        } else {
            result = -1;
        }

        return result;
    }

    /**
     * Return the value of the attribute corresponding to the index.
     *
     * @param index integer giving the index of the attribute
     * @return an Object (null if index is out of bound)
     */
    public Object getAttValue(final int index) {

        Object getAttributeValue;

        try {
            getAttributeValue = this.values.elementAt(index);
        } catch (final ArrayIndexOutOfBoundsException e) {
            getAttributeValue = null;
        }

        return getAttributeValue;
    }

    /**
     * Return the value of the attribute whith the given name.
     *
     * @param name the name of the attribute.
     * @return an Object (null if name is not an existing attribute)
     */
    public Object getAttValue(final String name) {
        boolean exist = false;

        if (name != null) {
            exist = this.searchTable.containsKey(name);
        }

        Object getAttributeValue;

        if (exist) {
            final int index = this.searchTable.get(name);
            getAttributeValue = this.values.elementAt(index);
        } else {
            getAttributeValue = null;
        }

        return getAttributeValue;
    }

    /**
     * To know if an attributes is already defined.
     *
     * @param attrName the name of the attribute
     * @return true if there, else false
     */
    public boolean isAttribute(final String attrName) {
        return attrName != null && this.searchTable.containsKey(attrName);
    }

    /**
     * Return the number of attributes in the tupple.
     *
     * @return int the number of attributes
     */
    public int getNumAtt() {
        return this.values.size();
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object
     */
    public String toString() {
        Object att;
        Object value;
        String attS;
        String valueS;

        final StringBuffer resp = new StringBuffer();
        resp.append("[");
        if (this.attributes.size() > 0) {
            att = this.attributes.elementAt(0);
            if (att == null) {
                attS = ZCommonConstants.NULLABLE;
            } else {
                attS = att.toString();
            }

            value = this.values.elementAt(0);
            if (value == null) {
                valueS = ZCommonConstants.NULLABLE;
            } else {
                valueS = value.toString();
            }
            resp.append(attS + ZCommonConstants.EQUALS_WITH_WHITESPACE_STRING + valueS);
        }

        for (int i = 1; i < this.attributes.size(); i++) {
            att = this.attributes.elementAt(i);
            if (att == null) {
                attS = ZCommonConstants.NULLABLE;
            } else {
                attS = att.toString();
            }

            value = this.values.elementAt(i);
            if (value == null) {
                valueS = ZCommonConstants.NULLABLE;
            } else {
                valueS = value.toString();
            }
            resp.append(", " + attS + ZCommonConstants.EQUALS_WITH_WHITESPACE_STRING + valueS);
        }
        resp.append("]");
        return resp.toString();
    }

}
