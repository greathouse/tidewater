package greenmoonsoftware.es.event.jdbcstore;

import greenmoonsoftware.es.event.Aggregate;
import greenmoonsoftware.es.event.EventList;
import greenmoonsoftware.es.store.StoreRetrieval;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class JdbcStoreQuery<T extends Aggregate> implements StoreRetrieval<T> {
    private final EventSerializer serializer;
    private final DataSource datasource;
    private final JdbcStoreConfiguration configuration;

    public JdbcStoreQuery(
            JdbcStoreConfiguration config,
            DataSource ds,
            EventSerializer s
    ) {
        configuration = config;
        datasource = ds;
        serializer = s;
    }

    public JdbcStoreQuery(
            JdbcStoreConfiguration config,
            DataSource ds
    ) {
        this(config, ds, new ObjectEventSerializer());
    }

    protected abstract T create();

    public T retrieve(String aggregateId) {
        ensureNotNull(aggregateId);
        T aggregate = create();
        try (Connection con = datasource.getConnection();
             PreparedStatement ps = con.prepareStatement("select * from " + configuration.getTablename() + " where aggregateId = ? order by eventDateTime asc")
        ) {
            prepareExecuteAndApplyEvents(ps, aggregateId, aggregate);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        return aggregate;
    }

    private void ensureNotNull(String string) {
        if (string == null || "".equals(string.trim())) {
            throw new IllegalArgumentException("Argument cannot be null or empty");
        }
    }

    private void prepareExecuteAndApplyEvents(PreparedStatement ps, String aggregateId, T aggregate) throws SQLException, IOException, ClassNotFoundException {
        ps.setString(1, aggregateId);
        EventList events = new EventList();
        try (ResultSet rs = ps.executeQuery()) {
            while(rs.next()) {
                events.add(serializer.deserialize(rs.getString("eventType"), rs.getBinaryStream("data")));
            }
        }
        aggregate.apply(events);
    }
}
