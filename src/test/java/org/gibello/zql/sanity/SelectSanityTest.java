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
import org.gibello.zql.alias.ZFromItem;
import org.gibello.zql.alias.ZSelectItem;
import org.gibello.zql.expression.ZConstant;
import org.gibello.zql.expression.ZExp;
import org.gibello.zql.expression.ZExpression;
import org.gibello.zql.query.ZQuery;
import org.gibello.zql.statement.ZStatement;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.gibello.zql.expression.ZConstant.*;
import static org.junit.Assert.*;

/**
 * @author Bogdan Mariesan, Romania, on 29-09-2015
 */
public class SelectSanityTest extends ZQLTestCase {

    @Test
    public void selectWithWildCardShouldWork() throws ParseException, IOException, SQLException {
        //given
        List<ZStatement> statements = parseSQL("select * from num;");
        ZQuery statement = (ZQuery) statements.get(0);
        //when
        String queryResult = queryDB(statement);
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
        List<ZStatement> statements = parseSQL("select a, b from num;");
        ZQuery statement = (ZQuery) statements.get(0);
        //when
        String queryResult = queryDB(statement);
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
        List<ZStatement> statements = parseSQL("select a+b, c from num;");
        ZQuery statement = (ZQuery) statements.get(0);
        //when
        String queryResult = queryDB(statement);
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
        List<ZStatement> statements = parseSQL("select * from num where a = 1 or e = 1;");
        ZQuery statement = (ZQuery) statements.get(0);
        //when
        String queryResult = queryDB(statement);
        //then
        assertNotNull(queryResult);
        assertTrue(queryResult.contains("[a = 1, b = 1, c = 1, d = 1, e = 1]"));
        assertTrue(queryResult.contains("[a = 1, b = 2, c = 3, d = 4, e = 5]"));
        assertTrue(queryResult.contains("[a = 5, b = 4, c = 3, d = 2, e = 1]"));
    }

    @Test
    public void selectWithWildcardWithComplexWhereClauseShouldWork() throws ParseException, IOException, SQLException {
        //given
        List<ZStatement> statements = parseSQL("select * from num where a = 1 and b = 1 or e = 1;");
        ZQuery statement = (ZQuery) statements.get(0);
        //when
        String queryResult = queryDB(statement);
        //then
        assertNotNull(queryResult);
        assertTrue(queryResult.contains("[a = 1, b = 1, c = 1, d = 1, e = 1]"));
        assertTrue(queryResult.contains("[a = 5, b = 4, c = 3, d = 2, e = 1]"));
    }

    @Test
    public void selectWithSpecificColumnNamesWithComplexWhereClauseShouldWork() throws ParseException, IOException, SQLException {
        //given
        List<ZStatement> statements = parseSQL("select d, e from num where a + b + c <= 3;");
        ZQuery statement = (ZQuery) statements.get(0);
        //when
        String queryResult = queryDB(statement);
        //then
        assertNotNull(queryResult);
        assertTrue(queryResult.contains("1.0, 1.0"));
    }

    @Test
    public void selectWithWildcardAndCompoundExpressionInWhereClauseShouldWork() throws ParseException, IOException, SQLException {
        //given
        List<ZStatement> statements = parseSQL("select * from num where 3 = a + b + c;");
        ZQuery statement = (ZQuery) statements.get(0);
        //when
        String queryResult = queryDB(statement);
        //then
        assertTrue(queryResult.contains("[a = 1, b = 1, c = 1, d = 1, e = 1]"));
    }

    @Test
    public void selectWithWildcardAndMultipleCompoundExpressionsInWhereClauseShouldWork() throws ParseException, IOException, SQLException {
        //given
        List<ZStatement> statements = parseSQL("select * from num where a = b or e = d - 1;");
        ZQuery statement = (ZQuery) statements.get(0);
        //when
        String queryResult = queryDB(statement);
        //then
        assertNotNull(queryResult);
        assertTrue(queryResult.contains("[a = 1, b = 1, c = 1, d = 1, e = 1]"));
        assertTrue(queryResult.contains("[a = 2, b = 2, c = 2, d = 2, e = 2]"));
        assertTrue(queryResult.contains("[a = 5, b = 4, c = 3, d = 2, e = 1]"));
    }

    @Test
    public void selectWithWildcardAndMultipleCompoundExpressionsAndSpecialOperatorInWhereClauseShouldWork() throws ParseException, IOException, SQLException {
        //given
        List<ZStatement> statements = parseSQL("select * from num where b ** a <= 2;");
        ZQuery statement = (ZQuery) statements.get(0);
        //when
        String queryResult = queryDB(statement);
        //then
        assertNotNull(queryResult);
        assertTrue(queryResult.contains("[a = 1, b = 1, c = 1, d = 1, e = 1]"));
        assertTrue(queryResult.contains("[a = 1, b = 2, c = 3, d = 4, e = 5]"));
    }

    /**
     * Test implementation in progress...
     */
    @Test
    public void selectWithWildcardAndOrderByShouldWork() throws ParseException, IOException, SQLException {
        //given
        List<ZStatement> statements = parseSQL("select * from num order by a desc;");
        ZQuery statement = (ZQuery) statements.get(0);
        assertNotNull(statement);
        assertEquals("select * from num order by a DESC", statement.toString());
    }

    /**
     * The tests below this point might need some proper naming but for now we need them for coverage...
     */
    @Test
    public void selectSanityCheck1() throws ParseException {
        //given
        final List<?> fromItems = Collections.singletonList(new ZFromItem("TableName"));
        final List<?> selectItems = Collections.singletonList(new ZSelectItem("*"));
        //when
        final List<ZStatement> statements = parseSQL("SELECT * FROM TableName;");
        //then
        final ZStatement statement = statements.get(0);
        assertValidSelect(statement, fromItems, selectItems, null);
    }

    @Test
    public void selectSanityCheck2() throws ParseException {
        //given
        final List<?> fromItems = Collections.singletonList(new ZFromItem("EmployeeAddressTable"));
        final List<?> selectItems = Arrays.asList(new ZSelectItem("FirstName"), new ZSelectItem("LastName"), new ZSelectItem("Address"), new ZSelectItem("City"),
                new ZSelectItem("State"));
        //when
        final List<ZStatement> statements = parseSQL("SELECT FirstName, LastName, Address, City, State FROM EmployeeAddressTable;");
        //then
        final ZStatement statement = statements.get(0);
        assertValidSelect(statement, fromItems, selectItems, null);
    }

    @Test
    public void selectSanityCheck3() throws ParseException {
        //given
        final List<?> fromItems = Collections.singletonList(new ZFromItem("EMPLOYEESTATISTICSTABLE"));
        final List<ZSelectItem> selectItems = new ArrayList<>();
        selectItems.add(new ZSelectItem("EMPLOYEEIDNO"));
        final ZExp whereClause = new ZExpression(">=", new ZConstant("SALARY", COLUMNNAME), new ZConstant("50000", NUMBER));
        //when
        final List<ZStatement> statements = parseSQL("SELECT EMPLOYEEIDNO FROM EMPLOYEESTATISTICSTABLE WHERE SALARY >= 50000;");
        //then
        final ZStatement statement = statements.get(0);
        assertValidSelect(statement, fromItems, selectItems, whereClause);
    }

    @Test
    public void selectSanityCheck4() throws ParseException {
        //given
        final List<?> fromItems = Collections.singletonList(new ZFromItem("EMPLOYEESTATISTICSTABLE"));
        final List<ZSelectItem> selectItems = new ArrayList<>();
        selectItems.add(new ZSelectItem("EMPLOYEEIDNO"));
        final ZExp whereClause = new ZExpression("=", new ZConstant("POSITION", COLUMNNAME), new ZConstant("Manager", STRING));
        //when
        final List<ZStatement> statements = parseSQL("SELECT EMPLOYEEIDNO FROM EMPLOYEESTATISTICSTABLE WHERE POSITION = 'Manager';");
        //then
        final ZStatement statement = statements.get(0);
        assertValidSelect(statement, fromItems, selectItems, whereClause);
    }

    @Test
    public void selectSanityCheck5() throws ParseException {
        //given
        final List<?> fromItems = Collections.singletonList(new ZFromItem("EMPLOYEESTATISTICSTABLE"));
        final List<ZSelectItem> selectItems = new ArrayList<>();
        selectItems.add(new ZSelectItem("EMPLOYEEIDNO"));
        final ZExpression whereClause = new ZExpression("AND");
        whereClause.addOperand(new ZExpression(">", new ZConstant("SALARY", COLUMNNAME), new ZConstant("40000", NUMBER)));
        whereClause.addOperand(new ZExpression("=", new ZConstant("POSITION", COLUMNNAME), new ZConstant("Staff", STRING)));
        //when
        final List<ZStatement> statements = parseSQL("SELECT EMPLOYEEIDNO FROM EMPLOYEESTATISTICSTABLE\n" +
                "WHERE SALARY > 40000 AND POSITION = 'Staff';");
        //then
        final ZStatement statement = statements.get(0);
        assertValidSelect(statement, fromItems, selectItems, whereClause);
    }

    @Test
    public void selectSanityCheck6() throws ParseException {
        //given
        final List<?> fromItems = Collections.singletonList(new ZFromItem("EMPLOYEESTATISTICSTABLE"));
        final List<ZSelectItem> selectItems = new ArrayList<>();
        selectItems.add(new ZSelectItem("EMPLOYEEIDNO"));
        final ZExpression whereClause = new ZExpression("OR");
        whereClause.addOperand(new ZExpression("<", new ZConstant("SALARY", COLUMNNAME), new ZConstant("40000", NUMBER)));
        whereClause.addOperand(new ZExpression("<", new ZConstant("BENEFITS", COLUMNNAME), new ZConstant("10000", NUMBER)));
        //when
        final List<ZStatement> statements = parseSQL("SELECT EMPLOYEEIDNO FROM EMPLOYEESTATISTICSTABLE\n" +
                "WHERE SALARY < 40000 OR BENEFITS < 10000;");
        //then
        final ZStatement statement = statements.get(0);
        assertValidSelect(statement, fromItems, selectItems, whereClause);
    }

    @Test
    public void selectSanityCheck7() throws ParseException {
        //given
        final List<?> fromItems = Collections.singletonList(new ZFromItem("EMPLOYEESTATISTICSTABLE"));
        final List<ZSelectItem> selectItems = new ArrayList<>();
        selectItems.add(new ZSelectItem("EMPLOYEEIDNO"));
        final ZExpression whereClause = new ZExpression("OR");
        ZExpression andClause = new ZExpression("AND");
        andClause.addOperand(new ZExpression("=", new ZConstant("POSITION", COLUMNNAME), new ZConstant("Manager", STRING)));
        andClause.addOperand(new ZExpression(">", new ZConstant("SALARY", COLUMNNAME), new ZConstant("60000", NUMBER)));
        whereClause.addOperand(andClause);
        whereClause.addOperand(new ZExpression(">", new ZConstant("BENEFITS", COLUMNNAME), new ZConstant("12000", NUMBER)));
        //when
        final List<ZStatement> statements = parseSQL("SELECT EMPLOYEEIDNO FROM EMPLOYEESTATISTICSTABLE\n" +
                "WHERE POSITION = 'Manager' AND SALARY > 60000 OR BENEFITS > 12000;");
        //then
        final ZStatement statement = statements.get(0);
        assertValidSelect(statement, fromItems, selectItems, whereClause);
    }

    @Test
    public void selectSanityCheck8() throws ParseException {
        //given
        final List<?> fromItems = Collections.singletonList(new ZFromItem("EMPLOYEESTATISTICSTABLE"));
        final List<ZSelectItem> selectItems = new ArrayList<>();
        selectItems.add(new ZSelectItem("EMPLOYEEIDNO"));
        final ZExpression whereClause = new ZExpression("AND");
        ZExpression orOperand = new ZExpression("OR");
        orOperand.addOperand(new ZExpression(">", new ZConstant("SALARY", COLUMNNAME), new ZConstant("50000", NUMBER)));
        orOperand.addOperand(new ZExpression(">", new ZConstant("BENEFIT", COLUMNNAME), new ZConstant("10000", NUMBER)));
        whereClause.addOperand(new ZExpression("=", new ZConstant("POSITION", COLUMNNAME), new ZConstant("Manager", STRING)));
        whereClause.addOperand(orOperand);
        //when
        final List<ZStatement> statements = parseSQL("SELECT EMPLOYEEIDNO FROM EMPLOYEESTATISTICSTABLE\n" +
                "WHERE POSITION = 'Manager' AND (SALARY > 50000 OR BENEFIT > 10000);");
        //then
        final ZStatement statement = statements.get(0);
        assertValidSelect(statement, fromItems, selectItems, whereClause);
    }

    @Test
    public void selectSanityCheck9() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT EMPLOYEEIDNO FROM EMPLOYEESTATISTICSTABLE\n" +
                "WHERE POSITION IN ('Manager', 'Staff');");
        final ZStatement statement = statements.get(0);
        assertEquals("select employeeidno from employeestatisticstable where (position in ('manager', 'staff'))".toLowerCase(), statement.toString().toLowerCase());
    }

    @Test
    public void selectSanityCheck10() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT EMPLOYEEIDNO FROM EMPLOYEESTATISTICSTABLE\n" +
                "WHERE SALARY BETWEEN 30000 AND 50000;");
        final ZStatement statement = statements.get(0);
        assertEquals("select EMPLOYEEIDNO from EMPLOYEESTATISTICSTABLE where (SALARY BETWEEN 30000 AND 50000)", statement.toString());
    }

    @Test
    public void selectSanityCheck11() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT EMPLOYEEIDNO FROM EMPLOYEESTATISTICSTABLE\n" +
                "WHERE SALARY NOT BETWEEN 30000 AND 50000;");
        final ZStatement statement = statements.get(0);
        assertEquals("select EMPLOYEEIDNO from EMPLOYEESTATISTICSTABLE where (SALARY NOT BETWEEN 30000 AND 50000)", statement.toString());
    }

    @Test
    public void selectSanityCheck12() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT EMPLOYEEIDNO FROM EMPLOYEEADDRESSTABLE WHERE LASTNAME LIKE 'L%';");
        final ZStatement statement = statements.get(0);
        assertEquals("select EMPLOYEEIDNO from EMPLOYEEADDRESSTABLE where (LASTNAME LIKE 'L%')", statement.toString());
    }

    @Test
    public void selectSanityCheck13() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT OWNERLASTNAME, OWNERFIRSTNAME FROM ANTIQUEOWNERS, ANTIQUES\n" +
                "WHERE BUYERID = OWNERID AND ITEM = 'Chair';");
        final ZStatement statement = statements.get(0);
        assertEquals("select OWNERLASTNAME, OWNERFIRSTNAME from ANTIQUEOWNERS, ANTIQUES where ((BUYERID = OWNERID) AND (ITEM = 'Chair'))", statement.toString());
    }

    @Test
    public void selectSanityCheck14() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT ANTIQUEOWNERS.OWNERLASTNAME, ANTIQUEOWNERS.OWNERFIRSTNAME\n" +
                "FROM ANTIQUEOWNERS, ANTIQUES\n" +
                "WHERE ANTIQUES.BUYERID = ANTIQUEOWNERS.OWNERID AND ANTIQUES.ITEM = 'Chair';");
        final ZStatement statement = statements.get(0);
        assertEquals("select ANTIQUEOWNERS.OWNERLASTNAME, ANTIQUEOWNERS.OWNERFIRSTNAME from ANTIQUEOWNERS, ANTIQUES where ((ANTIQUES.BUYERID = ANTIQUEOWNERS.OWNERID) AND (ANTIQUES.ITEM = 'Chair'))", statement.toString());
    }

    @Test
    public void selectSanityCheck15() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT DISTINCT SELLERID, OWNERLASTNAME, OWNERFIRSTNAME\n" +
                "FROM ANTIQUES, ANTIQUEOWNERS\n" +
                "WHERE SELLERID = OWNERID\n" +
                "ORDER BY OWNERLASTNAME, OWNERFIRSTNAME, OWNERID;");
        final ZStatement statement = statements.get(0);
        assertEquals("select distinct SELLERID, OWNERLASTNAME, OWNERFIRSTNAME from ANTIQUES, ANTIQUEOWNERS where (SELLERID = OWNERID) order by OWNERLASTNAME ASC, OWNERFIRSTNAME ASC, OWNERID ASC", statement.toString());
    }

    @Test
    public void selectSanityCheck16() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT OWN.OWNERLASTNAME Last Name, ORD.ITEMDESIRED Item Ordered\n" +
                "FROM ORDERS ORD, ANTIQUEOWNERS OWN\n" +
                "WHERE ORD.OWNERID = OWN.OWNERID\n" +
                "AND ORD.ITEMDESIRED IN (SELECT ITEM FROM ANTIQUES);");
        final ZStatement statement = statements.get(0);
        assertEquals("select OWN.OWNERLASTNAME Last Name, ORD.ITEMDESIRED Item Ordered from ORDERS ORD, ANTIQUEOWNERS OWN where ((ORD.OWNERID = OWN.OWNERID) AND (ORD.ITEMDESIRED IN (select ITEM from ANTIQUES)))", statement.toString());
    }

    @Test
    public void selectSanityCheck17() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT SUM(SALARY), AVG(SALARY) FROM EMPLOYEESTATISTICSTABLE;");
        final ZStatement statement = statements.get(0);
        assertEquals("select SUM(SALARY), AVG(SALARY) from EMPLOYEESTATISTICSTABLE", statement.toString());
    }

    @Test
    public void selectSanityCheck18() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT MIN(BENEFITS) FROM EMPLOYEESTATISTICSTABLE WHERE POSITION = 'Manager';");
        final ZStatement statement = statements.get(0);
        assertEquals("select MIN(BENEFITS) from EMPLOYEESTATISTICSTABLE where (POSITION = 'Manager')", statement.toString());
    }

    @Test
    public void selectSanityCheck19() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT COUNT(*) FROM EMPLOYEESTATISTICSTABLE WHERE POSITION = 'Staff';");
        final ZStatement statement = statements.get(0);
        assertEquals("select COUNT(*) from EMPLOYEESTATISTICSTABLE where (POSITION = 'Staff')", statement.toString());
    }

    @Test
    public void selectSanityCheck20() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT SELLERID FROM ANTIQUES, ANTVIEW WHERE ITEMDESIRED = ITEM;");
        final ZStatement statement = statements.get(0);
        assertEquals("select SELLERID from ANTIQUES, ANTVIEW where (ITEMDESIRED = ITEM)", statement.toString());
    }

    @Test
    public void selectSanityCheck21() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT BUYERID, MAX(PRICE) FROM ANTIQUES GROUP BY BUYERID;");
        final ZStatement statement = statements.get(0);
        assertEquals("select BUYERID, MAX(PRICE) from ANTIQUES group by BUYERID", statement.toString());
    }

    @Test
    public void selectSanityCheck22() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT BUYERID, MAX(PRICE) FROM ANTIQUES GROUP BY BUYERID HAVING PRICE > 1000;");
        final ZStatement statement = statements.get(0);
        assertEquals("select BUYERID, MAX(PRICE) from ANTIQUES group by BUYERID having (PRICE > 1000)", statement.toString());
    }

    @Test
    public void selectSanityCheck23() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT OWNERID FROM ANTIQUES\n" +
                "WHERE PRICE > (SELECT AVG(PRICE) + 100 FROM ANTIQUES);");
        final ZStatement statement = statements.get(0);
        assertEquals("select OWNERID from ANTIQUES where (PRICE > (select (AVG(PRICE) + 100) from ANTIQUES))", statement.toString());
    }

    @Test
    public void selectSanityCheck24() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT OWNERLASTNAME FROM ANTIQUEOWNERS\n" +
                "WHERE OWNERID = (SELECT DISTINCT BUYERID FROM ANTIQUES);");
        final ZStatement statement = statements.get(0);
        assertEquals("select OWNERLASTNAME from ANTIQUEOWNERS where (OWNERID = (select distinct BUYERID from ANTIQUES))", statement.toString());
    }

    @Test
    public void selectSanityCheck25() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT OWNERFIRSTNAME, OWNERLASTNAME FROM ANTIQUEOWNERS\n" +
                "WHERE EXISTS (SELECT * FROM ANTIQUES WHERE ITEM = 'Chair');");
        final ZStatement statement = statements.get(0);
        assertEquals("select OWNERFIRSTNAME, OWNERLASTNAME from ANTIQUEOWNERS where (EXISTS (select * from ANTIQUES where (ITEM = 'Chair')))", statement.toString());
    }

    @Test
    public void selectSanityCheck26() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT BUYERID, ITEM FROM ANTIQUES\n" +
                "WHERE PRICE >= ALL (SELECT PRICE FROM ANTIQUES);");
        final ZStatement statement = statements.get(0);
        assertEquals("select BUYERID, ITEM from ANTIQUES where (PRICE >= ALL (select PRICE from ANTIQUES))", statement.toString());
    }

    @Test
    public void selectSanityCheck27() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT BUYERID FROM ANTIQUEOWNERS UNION SELECT OWNERID FROM ORDERS;");
        final ZStatement statement = statements.get(0);
        assertEquals("select BUYERID from ANTIQUEOWNERS UNION (select OWNERID from ORDERS)", statement.toString());
    }

    @Test
    public void selectSanityCheck28() throws ParseException {
        final List<ZStatement> statements = parseSQL("SELECT OWNERID, 'is in both Orders & Antiques' FROM ORDERS, ANTIQUES\n" +
                "WHERE OWNERID = BUYERID\n" +
                "UNION\n" +
                "SELECT BUYERID, 'is in Antiques only' FROM ANTIQUES\n" +
                "WHERE BUYERID NOT IN (SELECT OWNERID FROM ORDERS);");
        final ZStatement statement = statements.get(0);
        assertEquals("select OWNERID, 'is in both Orders & Antiques' from ORDERS, ANTIQUES where (OWNERID = BUYERID) UNION (select BUYERID, 'is in Antiques only' from ANTIQUES where (BUYERID NOT IN (select OWNERID from ORDERS)))", statement.toString());
    }


    private void assertValidSelect(ZStatement statement, List<?> fromItems, List<?> selectItems, ZExp whereClause) {
        assertNotNull(statement);
        assertTrue(statement instanceof ZQuery);
        ZQuery query = (ZQuery) statement;
        assertEquals(fromItems, query.getFrom());
        assertEquals(null, query.getGroupBy());
        assertEquals(null, query.getOrderBy());
        assertEquals(selectItems, query.getSelect());
        assertEquals(whereClause, query.getWhere());
    }
}

