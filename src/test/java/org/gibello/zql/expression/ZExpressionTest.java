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

    @Test
    public void zExpressionToStringNPE() throws ParseException {
        String sqlString = "WHERE a = b OR c > 20";
        ZqlJJParser parser = new ZqlJJParser(new StringReader(sqlString));
        ZExp whereClause = parser.WhereClause();

        assertNotNull(whereClause);
        String toString = whereClause.toString();
        LOGGER.debug(toString);
        assertNotNull(toString);
    }
}