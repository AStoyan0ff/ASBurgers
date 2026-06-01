package START.Init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class BurgerSchemaMigration implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public BurgerSchemaMigration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {

        if (!tableExists("burgers")) {
            return;
        }

        boolean hasAvailable = columnExists("burgers", "available");
        boolean hasIsAvailable = columnExists("burgers", "is_available");

        if (hasAvailable && hasIsAvailable) {

            dropColumn("burgers", "is_available");
            hasIsAvailable = false;
        }

        if (!hasAvailable && hasIsAvailable) {

            jdbcTemplate.execute(
                    "ALTER TABLE burgers CHANGE COLUMN is_available available TINYINT(1) NOT NULL DEFAULT 1");
            hasAvailable = true;
        }

        if (hasAvailable) {

            jdbcTemplate.execute(
                    "ALTER TABLE burgers MODIFY COLUMN available TINYINT(1) NOT NULL DEFAULT 1");
        }
    }

    private boolean tableExists(String tableName) {

        Integer count = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM information_schema.TABLES
                WHERE TABLE_SCHEMA = DATABASE()
                  AND TABLE_NAME = ?
                """,
                Integer.class,
                tableName);
        return count != null && count > 0;
    }

    private boolean columnExists(String tableName, String columnName) {

        Integer count = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM information_schema.COLUMNS
                WHERE TABLE_SCHEMA = DATABASE()
                  AND TABLE_NAME = ?
                  AND COLUMN_NAME = ?
                """,
                Integer.class,
                tableName,
                columnName);
        return count != null && count > 0;
    }

    private void dropColumn(String tableName, String columnName) {
        jdbcTemplate.execute("ALTER TABLE " + tableName + " DROP COLUMN " + columnName);
    }
}
