package greenmoonsoftware.es.event.jdbcstore;

import greenmoonsoftware.es.event.Event;
import greenmoonsoftware.es.event.EventSubscriber;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class JdbcStoreEventSubscriber implements EventSubscriber<Event> {
    private final EventSerializer<Event> serilalizer;
    private DataSource datasource;
    private JdbcStoreConfiguration configuration;

    public JdbcStoreEventSubscriber(
            JdbcStoreConfiguration config,
            DataSource ds,
            EventSerializer<Event> s) {
        configuration = config;
        datasource = ds;
        serilalizer = s;
    }

    public JdbcStoreEventSubscriber(
            JdbcStoreConfiguration config,
            DataSource ds) {
        this(config, ds, new ObjectEventSerializer());
    }

    @Override
    public void onEvent(Event event) {
        String sql = "insert into " + configuration.getTablename() + " (id, aggregateId, eventType, eventDateTime, data) " +
                        "values (?,?,?,?,?)";
        try (Connection con = datasource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)
        ) {
            prepareAndExecuteStatement(event, ps);
        }
        catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void prepareAndExecuteStatement(Event event, PreparedStatement ps) throws SQLException, IOException {
        ps.setString(1, event.getId().toString());
        ps.setString(2, event.getAggregateId());
        ps.setString(3, event.getType());
        ps.setTimestamp(4, new Timestamp(event.getEventDateTime().toEpochMilli()));
        ps.setBinaryStream(5, serilalizer.serialize(event));
        ps.execute();
    }
}
