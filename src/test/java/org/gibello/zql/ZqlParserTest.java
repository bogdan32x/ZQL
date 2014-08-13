package org.gibello.zql;

import static org.junit.Assert.*;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.gibello.zql.query.ZQuery;
import org.gibello.zql.statement.ZStatement;
import org.gibello.zql.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ZqlParserTest {

    @Mock
    private ZqlParser zqlParser;

    private static final String TEST_RESOURCE_PATH_VALID_SELECT_WITH_WILDCARD = "src/test/resources/valid_select_with_wildcard.sql";
    private static final String VALID_SELECT_WITH_WILDCARD_EXPECTED_RESULT = "select * from Stock s";

    @Test
    public void test_simple_select_with_wildcard() throws IOException, ParseException {
        // given
        DataInputStream is = given_a_valid_select_with_a_wildcard_operator();
        String rawSqlContent = TestUtils.readInputStreamAsString(TEST_RESOURCE_PATH_VALID_SELECT_WITH_WILDCARD);
        // when
        ZqlParser parser = when_the_parser_is_initialized(is);
        // then
        then_given_sql_should_be_the_same_as_the_parsed_sql(parser, rawSqlContent);
    }

    private void then_given_sql_should_be_the_same_as_the_parsed_sql(ZqlParser parser, String rawSqlContent) throws ParseException, IOException {
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
                assertEquals(VALID_SELECT_WITH_WILDCARD_EXPECTED_RESULT, query.toString());
            }
        }
    }

    private ZqlParser when_the_parser_is_initialized(DataInputStream is) {
        ZqlParser parser = new ZqlParser(is);
        return parser;
    }

    private DataInputStream given_a_valid_select_with_a_wildcard_operator() throws FileNotFoundException {
        DataInputStream dis = new DataInputStream(new FileInputStream(new File(TEST_RESOURCE_PATH_VALID_SELECT_WITH_WILDCARD)));
        return dis;
    }

}
