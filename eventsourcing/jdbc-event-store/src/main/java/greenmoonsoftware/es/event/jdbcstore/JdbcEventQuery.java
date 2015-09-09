package greenmoonsoftware.es.event.jdbcstore;

import greenmoonsoftware.es.event.Event;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JdbcEventQuery {
    private final EventSerializer serializer;
    private final DataSource datasource;
    private final JdbcStoreConfiguration configuration;

    public JdbcEventQuery(
            JdbcStoreConfiguration config,
            DataSource ds,
            EventSerializer s
    ) {
        configuration = config;
        datasource = ds;
        serializer = s;
    }

    public JdbcEventQuery(
            JdbcStoreConfiguration config,
            DataSource ds
    ) {
        this(config, ds, new ObjectEventSerializer());
    }

    public List<Event> getEvents() {
        try (Connection con = datasource.getConnection();
             PreparedStatement ps = con.prepareStatement("select * from " + configuration.getTablename() + " order by eventDateTime asc");
             ResultSet rs = ps.executeQuery()
        ) {
            List<Event> events = new ArrayList<>();
            while(rs.next()) {
                events.add(serializer.deserialize(rs.getString("eventType"), rs.getBinaryStream("data")));
            }
            return Collections.unmodifiableList(events);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
