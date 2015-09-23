package org.gibello.zql.expression;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gibello.zql.ParseException;
import org.gibello.zql.ZqlJJParser;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertNotNull;

/**
 * @author Bogdan Mariesan, Romania, on 3/9/2015
 */
public class ZExpressionTest {

    private static final Logger LOGGER = LogManager.getLogger(ZExpressionTest.class);

    /* Unit test written for fixing issue #8 */
    @Test
    public void zExpressionToStringNPEWhereClauseWithOrOperator() throws ParseException {
        //given
        final ZqlJJParser parser = givenAParserForTheSQLExpression("WHERE a = b OR c > 20");
        //when
        final ZExp whereClause = parser.WhereClause();
        //then
        assertExpressionToStringWorks(whereClause);
    }

    @Test
    public void zExpressionToStringNPEWhereClauseWithAndOperator() throws ParseException {
        //given
        final ZqlJJParser parser = givenAParserForTheSQLExpression("WHERE a = b AND c > 20");
        //when
        final ZExp whereClause = parser.WhereClause();
        //then
        assertExpressionToStringWorks(whereClause);
    }

    /* Invalid operators will be removed from the WHERE clause */
    @Test
    public void zExpressionToStringNPEWhereClauseWithInvalidOperator() throws ParseException {
        //given
        final ZqlJJParser parser = givenAParserForTheSQLExpression("WHERE a = b INVALID c > 20");
        //when
        final ZExp whereClause = parser.WhereClause();
        //then
        assertExpressionToStringWorks(whereClause);
    }

    private void assertExpressionToStringWorks(ZExp whereClause) {
        assertNotNull(whereClause);
        String toString = whereClause.toString();
        assertNotNull(toString);
        LOGGER.debug(toString);
    }

    private ZqlJJParser givenAParserForTheSQLExpression(String sqlExpression) {
        return new ZqlJJParser(new StringReader(sqlExpression));
    }
}