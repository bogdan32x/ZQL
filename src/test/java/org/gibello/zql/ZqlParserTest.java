package org.gibello.zql;

import org.gibello.zql.query.ZQuery;
import org.gibello.zql.statement.ZStatement;
import org.gibello.zql.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.*;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ZqlParserTest {

    private static final String VALID_SELECT_WITH_WILDCARD_EXPECTED_RESULT = "select * from Stock s";
    private static final String VALID_SELECT_FROM_TWO_TABLES_WITH_WILDCARD_EXPECTED_RESULT = "select * from Stock s, Market m";
    private static final String VALID_SELECT_FROM_MULTIPLE_TABLES_WITH_WILDCARD_EXPECTED_RESULT = "select * from Stock s, Market m, Shares s, Logs l, Mortgage mm";
    @Mock
    private ZqlParser zqlParser;

    @Test
    public void test_simple_select_with_wildcard() throws IOException, ParseException {
        // given
        DataInputStream is = given_a_valid_select_with_a_wildcard_operator();
        String rawSqlContent = TestUtils.readInputStreamAsString("src/test/resources/valid_select_with_wildcard.sql");
        // when
        ZqlParser parser = when_the_parser_is_initialized(is);
        // then
        then_given_sql_should_be_the_same_as_the_parsed_sql(parser, rawSqlContent, VALID_SELECT_WITH_WILDCARD_EXPECTED_RESULT);
    }

    @Test
    public void test_select_from_two_tables_with_wildcard() throws IOException, ParseException {
        // given
        DataInputStream is = given_a_valid_select_from_two_tables_with_a_wildcard_operator();
        String rawSqlContent = TestUtils.readInputStreamAsString("src/test/resources/valid_select_from_two_tables_with_wildcard.sql");
        // when
        ZqlParser parser = when_the_parser_is_initialized(is);
        // then
        then_given_sql_should_be_the_same_as_the_parsed_sql(parser, rawSqlContent, VALID_SELECT_FROM_TWO_TABLES_WITH_WILDCARD_EXPECTED_RESULT);
    }

    @Test
    public void test_select_from_multiple_tables_with_wildcard() throws IOException, ParseException {
        // given
        DataInputStream is = given_a_valid_select_from_multiple_tables_with_a_wildcard_operator();
        String rawSqlContent = TestUtils.readInputStreamAsString("src/test/resources/valid_select_from_multiple_tables_with_wildcard.sql");
        // when
        ZqlParser parser = when_the_parser_is_initialized(is);
        // then
        then_given_sql_should_be_the_same_as_the_parsed_sql(parser, rawSqlContent, VALID_SELECT_FROM_MULTIPLE_TABLES_WITH_WILDCARD_EXPECTED_RESULT);
    }

    private DataInputStream given_a_valid_select_from_multiple_tables_with_a_wildcard_operator() throws FileNotFoundException {
        DataInputStream dis = new DataInputStream(new FileInputStream(new File("src/test/resources/valid_select_from_multiple_tables_with_wildcard.sql")));
        return dis;
    }

    private DataInputStream given_a_valid_select_from_two_tables_with_a_wildcard_operator() throws FileNotFoundException {
        DataInputStream dis = new DataInputStream(new FileInputStream(new File("src/test/resources/valid_select_from_two_tables_with_wildcard.sql")));
        return dis;
    }

    private ZqlParser when_the_parser_is_initialized(DataInputStream is) {
        ZqlParser parser = new ZqlParser(is);
        return parser;
    }

    private DataInputStream given_a_valid_select_with_a_wildcard_operator() throws FileNotFoundException {
        DataInputStream dis = new DataInputStream(new FileInputStream(new File("src/test/resources/valid_select_with_wildcard.sql")));
        return dis;
    }

    private void then_given_sql_should_be_the_same_as_the_parsed_sql(ZqlParser parser, String rawSqlContent, String expectedResult)
            throws ParseException, IOException {
        assertNotNull(parser);
        assertNotNull(rawSqlContent);

        List<ZStatement> parsedExpressions = parser.readStatements();

        assertNotNull(parsedExpressions);
        assertTrue(parsedExpressions.size() > 0);

        for (ZStatement statement : parsedExpressions) {
            assertNotNull(statement);
        }

        assertNotNull(rawSqlContent);
        assertEquals(1, parsedExpressions.size());
        for (ZStatement statement : parsedExpressions) {
            if (statement instanceof ZQuery) {
                ZQuery query = (ZQuery) statement;
                assertEquals(expectedResult, query.toString());
            }
        }
    }
}
