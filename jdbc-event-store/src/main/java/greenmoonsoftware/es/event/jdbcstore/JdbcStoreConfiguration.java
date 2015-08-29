package greenmoonsoftware.es.event.jdbcstore;

public final class JdbcStoreConfiguration {
    private String tablename;

    public JdbcStoreConfiguration(String tablename) {

        this.tablename = tablename;
    }

    public String getTablename() {
        return tablename;
    }
}
