package cc.concurrent.mango.exception;

import cc.concurrent.mango.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 测试{@link UncheckedSQLException}
 *
 * @author ash
 */
public class UncheckedSQLExceptionTest {

    private final static DataSource ds = Config.getDataSource();
    private final static Mango mango = new Mango(ds);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void before() throws Exception {
        Connection conn = ds.getConnection();
        Sqls.PERSON.run(conn);
        conn.close();
    }

    @Test
    public void test() {
        thrown.expect(UncheckedSQLException.class);
        PersonDao dao = mango.create(PersonDao.class);
        Person p = new Person(1, "ash");
        dao.add(p);
    }

    @DB
    static interface PersonDao {

        @SQL("insert intoo person(id, name) values(:1.id, :1.name)")
        public int add(Person p);
    }


    public static class Person {
        int id;
        String name;

        public Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

    }

}