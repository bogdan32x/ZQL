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

package org.gibello.zqlparser;

/*
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
 * along with Zql.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Vector;

import org.gibello.zql.data.ZEval;
import org.gibello.zql.data.ZTuple;

/**
 * <pre>
 * ZDemo is able to send SQL queries to simple CSV (comma-separated values)
 * files; the CSV syntax used here is very simple:
 *  The 1st line contains the column names
 *  Other lines contain column values (tuples)
 *  Values are separated by commas, so they can't contain commas (it's just
 *  for demo purposes).
 * Example:
 * Create a num.db text file that contains the following:
 *  a,b,c,d,e
 *  1,1,1,1,1
 *  2,2,2,2,2
 *  1,2,3,4,5
 *  5,4,3,2,1
 * You can then run ZDemo, and query it; some legal queries follow:
 *  select * from num;
 *  select a, b from num;
 *  select a+b, c from num;
 *  select * from num where a = 1 or e = 1;
 *  select * from num where a = 1 and b = 1 or e = 1;
 *  select d, e from num where a + b + c <= 3;
 *  select * from num where 3 = a + b + c;
 *  select * from num where a = b or e = d - 1;
 *  select * from num where b ** a <= 2;
 * </pre>
 * 
 * @author Bogdan Mariesan, Romania
 */
public final class ZDemo {

	/**
	 * Default constructor.
	 */
	private ZDemo() {

	}

	/**
	 * Non commented main method.
	 * 
	 * @param args
	 *            args.
	 */
	public static void main(final String[] args) {
		try {

			ZqlParser p = null;

			if (args.length < 1) {
				System.out.println("Reading SQL from stdin (quit; or exit; to quit)");
				p = new ZqlParser(System.in);
			} else {
				p = new ZqlParser(new DataInputStream(new FileInputStream(args[0])));
			}

			// Read all SQL statements from input
			ZStatement st;
			while ((st = p.readStatement()) != null) {

				// Display the statement
				System.out.println(st.toString());

				// An SQL query: query the DB
				if (st instanceof ZQuery) {
					queryDB((ZQuery) st);
				} else
					// An SQL insert
					if (st instanceof ZInsert) {
						insertDB((ZInsert) st);
					}
			}

		} catch (final SQLException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Query the database.
	 * 
	 * @param q
	 *            the query.
	 * @throws SQLException
	 *             the exception.
	 * @throws IOException
	 *             the exception.
	 */
	static void queryDB(final ZQuery q) throws IOException, SQLException {

		// SELECT part of the query
		final Vector<?> sel = q.getSelect();
		// FROM part of the query
		final Vector<?> from = q.getFrom();
		// WHERE part of the
		final ZExpression where = (ZExpression) q.getWhere();
		// query

		if (from.size() > 1) {
			throw new SQLException("Joins are not supported");
		}

		// Retrieve the table name in the FROM clause
		final ZFromItem table = (ZFromItem) from.elementAt(0);

		// We suppose the data is in a text file called <tableName>.db
		// <tableName> is the table name in the FROM clause
		// BufferedReader db1 = new BufferedReader(new
		// FileReader(table.getTable() + ".db"));
		final BufferedReader db = new BufferedReader(new InputStreamReader(ZDemo.class.getResourceAsStream(table
				.getTable() + ".db")));

		// Read the column names (the 1st line of the .db file)
		final ZTuple tuple = new ZTuple(db.readLine());

		final ZEval evaluator = new ZEval();

		// Now, each line in the .db file is a tuple
		String tpl;
		while ((tpl = db.readLine()) != null) {

			tuple.setRow(tpl);

			// Evaluate the WHERE expression for the current tuple
			// Display the tuple if the condition evaluates to true

			if (where == null || evaluator.eval(tuple, where)) {
				displayTuple(tuple, sel);
			}

		}

		db.close();
	}

	/**
	 * Display a tuple, according to a SELECT map.
	 * 
	 * @param tuple
	 *            the tuple.
	 * @param map
	 *            the element map.
	 * @throws SQLException
	 *             the exception.
	 */
	static void displayTuple(final ZTuple tuple, final Vector<?> map) throws SQLException {

		// If it is a "select *", display the whole tuple
		if (((ZSelectItem) map.elementAt(0)).isWildcard()) {
			System.out.println(tuple.toString());
			return;
		}

		final ZEval evaluator = new ZEval();

		// Evaluate the value of each select item
		for (int i = 0; i < map.size(); i++) {

			final ZSelectItem item = (ZSelectItem) map.elementAt(i);
			System.out.print(evaluator.evalExpValue(tuple, item.getExpression()).toString());

			if (i == map.size() - 1) {
				System.out.println("");
			} else {
				System.out.print(", ");
			}
		}
	}

	/**
	 * @param ins
	 *            insert query.
	 * @throws IOException
	 *             the exception.
	 */
	static void insertDB(final ZInsert ins) throws IOException {
		System.out.println("Should implement INSERT here");
		System.out.println(ins.toString());
	}

};
