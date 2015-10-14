package org.gibello.zql.sanity;

import org.gibello.zql.ParseException;
import org.gibello.zql.statement.ZStatement;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Bogdan Mariesan, Romania, on 14-10-2015
 */
public class UpdateSanityTest extends ZQLTestCase {

    @Test
    public void updateSanityCheck1() throws ParseException {
        final List<ZStatement> statements = parseSQL("UPDATE ANTIQUES SET PRICE = 500.00 WHERE ITEM = 'Chair';");
        assertEquals("update ANTIQUES set PRICE=500.00 where (ITEM = 'Chair')", statements.get(0).toString());
    }

    @Test
    public void updateSanityCheck2() throws ParseException {
        final List<ZStatement> statements = parseSQL("UPDATE ANTIQUEOWNERS SET OWNERFIRSTNAME = 'John'\n" +
                "WHERE OWNERID = (SELECT BUYERID FROM ANTIQUES WHERE ITEM = 'Bookcase');");
        assertEquals("update ANTIQUEOWNERS set OWNERFIRSTNAME='John' where (OWNERID = (select BUYERID from ANTIQUES where (ITEM = 'Bookcase')))", statements.get(0).toString());
    }
}
