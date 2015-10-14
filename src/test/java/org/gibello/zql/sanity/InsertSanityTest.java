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

package org.gibello.zql.sanity;

import org.gibello.zql.ParseException;
import org.gibello.zql.expression.ZConstant;
import org.gibello.zql.statement.ZInsert;
import org.gibello.zql.statement.ZStatement;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.gibello.zql.expression.ZConstant.NUMBER;
import static org.gibello.zql.expression.ZConstant.STRING;
import static org.junit.Assert.*;

/**
 * @author Bogdan Mariesan, Romania, on 12-10-2015
 */
public class InsertSanityTest extends ZQLTestCase {

    @Test
    public void singleLinePartialInsertsShouldWork() throws ParseException {
        final List<ZStatement> statements = parseSQL("INSERT INTO num (a,b) VALUES (1,22);");

        assertNotNull(statements);
        assertFalse(statements.isEmpty());
        ZStatement insertStatement = statements.get(0);
        assertNotNull(insertStatement);
        assertTrue(insertStatement instanceof ZInsert);
        ZInsert insert = (ZInsert) insertStatement;
        assertEquals("num", insert.getTable());
        assertEquals(Arrays.asList("a", "b"), insert.getColumns());
        assertNull(insert.getQuery());
        assertEquals(Arrays.asList(new ZConstant("1", NUMBER), new ZConstant("22", NUMBER)), insert.getValues());
    }

    @Test
    public void singleLineInsertAllColumnsInsertShouldWork() throws ParseException {
        final List<ZStatement> statements = parseSQL("INSERT INTO ANTIQUES VALUES (21, 01, 'Ottoman', 200.00);");

        assertNotNull(statements);
        assertFalse(statements.isEmpty());
        ZStatement insertStatement = statements.get(0);
        assertNotNull(insertStatement);
        assertTrue(insertStatement instanceof ZInsert);
        ZInsert insert = (ZInsert) insertStatement;
        assertEquals("ANTIQUES", insert.getTable());
        assertNull(insert.getColumns());
        assertNull(insert.getQuery());
        assertEquals(Arrays.asList(new ZConstant("21", NUMBER), new ZConstant("01", NUMBER), new ZConstant("Ottoman", STRING), new ZConstant("200.00", NUMBER)), insert.getValues());
    }

    /**
     * Implemented as part of issue https://github.com/bogdan32x/ZQL/issues/10
     */
    @Ignore
    @Test
    public void multiLinePartialInsertShouldWork() throws ParseException {
        final List<ZStatement> statements = parseSQL("INSERT INTO table1 (First,Last) VALUES ('Fred','Smith'), ('John','Smith'), ('Michael','Smith'),('Robert','Smith');");

        assertNotNull(statements);
        assertFalse(statements.isEmpty());
        ZStatement insertStatement = statements.get(0);
        assertNotNull(insertStatement);
        assertTrue(insertStatement instanceof ZInsert);
        ZInsert insert = (ZInsert) insertStatement;
        assertEquals("table1", insert.getTable());
        assertEquals(Arrays.asList("First", "Last"), insert.getColumns());
        assertNull(insert.getQuery());
    }

    @Test
    public void insertSanityCheck1() throws ParseException {
        final List<ZStatement> statements = parseSQL("INSERT INTO ANTIQUES VALUES (21, 01, 'Ottoman', 200.00);");
        assertEquals("insert into ANTIQUES values (21, 01, 'Ottoman', 200.00)", statements.get(0).toString());
    }

    @Test
    public void insertSanityCheck2() throws ParseException {
        final List<ZStatement> statements = parseSQL("INSERT INTO ANTIQUES (BUYERID, SELLERID, ITEM) VALUES (01, 21, 'Ottoman');");
        assertEquals("insert into ANTIQUES(BUYERID,SELLERID,ITEM) values (01, 21, 'Ottoman')", statements.get(0).toString());
    }

}
