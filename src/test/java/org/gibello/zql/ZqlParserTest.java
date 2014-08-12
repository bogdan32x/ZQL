package org.gibello.zql;

import static org.junit.Assert.*;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ZqlParserTest {

    private static final String SRC_TEST_RESOURCES_VALID_SELECT_WITH_WILDCARD_SQL = "src/test/resources/valid_select_with_wildcard.sql";
    @Mock
    private ZqlParser zqlParser;

    @Test
    public void test_simple_select_with_wildcard() throws IOException, ParseException {
        // given
        DataInputStream is = given_a_valid_select_with_a_wildcard_operator();
        String rawSqlContent = TestUtils.readInputStreamAsString(SRC_TEST_RESOURCES_VALID_SELECT_WITH_WILDCARD_SQL);
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

        for (ZStatement statement : parsedExpressions) {
            if (statement instanceof ZQuery) {
                ZQuery query = (ZQuery) statement;
                rawSqlContent = rawSqlContent.replace(query.toString() + ";", "");
            }
        }

        // if the parsed content and input match the result of the above function will render the raw string as an empty spaces string.
        assertTrue(rawSqlContent.replaceAll("\\s", "").isEmpty());
    }

    private ZqlParser when_the_parser_is_initialized(DataInputStream is) {
        ZqlParser parser = new ZqlParser(is);
        return parser;
    }

    private DataInputStream given_a_valid_select_with_a_wildcard_operator() throws FileNotFoundException {
        DataInputStream dis = new DataInputStream(new FileInputStream(new File(SRC_TEST_RESOURCES_VALID_SELECT_WITH_WILDCARD_SQL)));
        return dis;
    }

}
