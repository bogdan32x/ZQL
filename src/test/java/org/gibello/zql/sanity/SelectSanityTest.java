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
import org.gibello.zql.query.ZQuery;
import org.gibello.zql.statement.ZStatement;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Bogdan Mariesan, Romania, on 29-09-2015
 */
public class SelectSanityTest {

    ZQLTestUtils testUtils = new ZQLTestUtils();

    @Test
    public void selectWithWildCardShouldWork() throws ParseException, IOException, SQLException {
        //given
        List<ZStatement> statements = testUtils.parseSql("select * from num;");
        ZQuery statement = (ZQuery) statements.get(0);
        //when
        String queryResult = testUtils.queryDB(statement);
        //then
        assertNotNull(queryResult);
        assertTrue(queryResult.contains("[a = 1, b = 1, c = 1, d = 1, e = 1]"));
        assertTrue(queryResult.contains("[a = 2, b = 2, c = 2, d = 2, e = 2]"));
        assertTrue(queryResult.contains("[a = 1, b = 2, c = 3, d = 4, e = 5]"));
        assertTrue(queryResult.contains("[a = 5, b = 4, c = 3, d = 2, e = 1]"));
    }

    @Test
    public void selectWithSpecificColumnNamesShouldWork() throws ParseException, IOException, SQLException {
        //given
        List<ZStatement> statements = testUtils.parseSql("select a, b from num;");
        ZQuery statement = (ZQuery) statements.get(0);
        //when
        String queryResult = testUtils.queryDB(statement);
        //then
        assertNotNull(queryResult);
        assertTrue(queryResult.contains("1.0, 1.0"));
        assertTrue(queryResult.contains("2.0, 2.0"));
        assertTrue(queryResult.contains("1.0, 2.0"));
        assertTrue(queryResult.contains("5.0, 4.0"));
    }

    @Test
    public void selectWithSpecificColumnNamesAndConcatenatedColumnsShouldWork() throws ParseException, IOException, SQLException {
        //given
        List<ZStatement> statements = testUtils.parseSql("select a+b, c from num;");
        ZQuery statement = (ZQuery) statements.get(0);
        //when
        String queryResult = testUtils.queryDB(statement);
        //then
        assertNotNull(queryResult);
        assertTrue(queryResult.contains("2.0, 1.0"));
        assertTrue(queryResult.contains("4.0, 2.0"));
        assertTrue(queryResult.contains("3.0, 3.0"));
        assertTrue(queryResult.contains("9.0, 3.0"));
    }

    @Test
    public void selectWithWildcardAndOrInWhereClauseShouldWork() throws ParseException, IOException, SQLException {
        //given
        List<ZStatement> statements = testUtils.parseSql("select * from num where a = 1 or e = 1;");
        ZQuery statement = (ZQuery) statements.get(0);
        //when
        String queryResult = testUtils.queryDB(statement);
        //then
        assertNotNull(queryResult);
        assertTrue(queryResult.contains("[a = 1, b = 1, c = 1, d = 1, e = 1]"));
        assertTrue(queryResult.contains("[a = 1, b = 2, c = 3, d = 4, e = 5]"));
        assertTrue(queryResult.contains("[a = 5, b = 4, c = 3, d = 2, e = 1]"));
    }

    @Test
    public void selectWithWildcardWithComplexWhereClauseShouldWork() throws ParseException, IOException, SQLException {
        //given
        List<ZStatement> statements = testUtils.parseSql("select * from num where a = 1 and b = 1 or e = 1;");
        ZQuery statement = (ZQuery) statements.get(0);
        //when
        String queryResult = testUtils.queryDB(statement);
        //then
        assertNotNull(queryResult);
        assertTrue(queryResult.contains("[a = 1, b = 1, c = 1, d = 1, e = 1]"));
        assertTrue(queryResult.contains("[a = 5, b = 4, c = 3, d = 2, e = 1]"));
    }

    @Test
    public void selectWithSpecificColumnNamesWithComplexWhereClauseShouldWork() throws ParseException, IOException, SQLException {
        //given
        List<ZStatement> statements = testUtils.parseSql("select d, e from num where a + b + c <= 3;");
        ZQuery statement = (ZQuery) statements.get(0);
        //when
        String queryResult = testUtils.queryDB(statement);
        //then
        assertNotNull(queryResult);
        assertTrue(queryResult.contains("1.0, 1.0"));
        assertTrue(queryResult.contains("2.0, 2.0"));
        assertTrue(queryResult.contains("2.0, 1.0"));
        assertTrue(queryResult.contains("4.0, 5.0"));
    }

    @Test
    public void selectWithWildcardAndCompoundExpressionInWhereClauseShouldWork() throws ParseException, IOException, SQLException {
        //given
        List<ZStatement> statements = testUtils.parseSql("select * from num where 3 = a + b + c;");
        ZQuery statement = (ZQuery) statements.get(0);
        //when
        String queryResult = testUtils.queryDB(statement);
        //then
        assertTrue(queryResult.contains("[a = 1, b = 1, c = 1, d = 1, e = 1]"));
    }

    @Test
    public void selectWithWildcardAndMultipleCompoundExpressionsInWhereClauseShouldWork() throws ParseException, IOException, SQLException {
        //given
        List<ZStatement> statements = testUtils.parseSql("select * from num where a = b or e = d - 1;");
        ZQuery statement = (ZQuery) statements.get(0);
        //when
        String queryResult = testUtils.queryDB(statement);
        //then
        assertNotNull(queryResult);
        assertTrue(queryResult.contains("[a = 1, b = 1, c = 1, d = 1, e = 1]"));
        assertTrue(queryResult.contains("[a = 2, b = 2, c = 2, d = 2, e = 2]"));
        assertTrue(queryResult.contains("[a = 5, b = 4, c = 3, d = 2, e = 1]"));
    }

    @Test
    public void selectWithWildcardAndMultipleCompoundExpressionsAndSpecialOperatorInWhereClauseShouldWork() throws ParseException, IOException, SQLException {
        //given
        List<ZStatement> statements = testUtils.parseSql("select * from num where b ** a <= 2;");
        ZQuery statement = (ZQuery) statements.get(0);
        //when
        String queryResult = testUtils.queryDB(statement);
        //then
        assertNotNull(queryResult);
        assertTrue(queryResult.contains("[a = 2, b = 2, c = 2, d = 2, e = 2]"));
        assertTrue(queryResult.contains("[a = 1, b = 2, c = 3, d = 4, e = 5]"));
        assertTrue(queryResult.contains("[a = 5, b = 4, c = 3, d = 2, e = 1]"));
    }
}

