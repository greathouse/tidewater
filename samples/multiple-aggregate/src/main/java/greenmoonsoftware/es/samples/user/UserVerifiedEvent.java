package greenmoonsoftware.es.samples.user;

import greenmoonsoftware.es.event.Event;

import java.time.Instant;
import java.util.UUID;

public class UserVerifiedEvent implements Event {
 private UUID id = UUID.randomUUID();
 private String aggregateId;
 private String type = "verify-user";
 private Instant eventDateTime = Instant.now();

 public UserVerifiedEvent(String aggregateId) {
  this.aggregateId = aggregateId;
 }

 public UUID getId() {
  return id;

 }

 public String getAggregateId() {
  return aggregateId;
 }

 public String getType() {
  return type;
 }

 public Instant getEventDateTime() {
  return eventDateTime;
 }
}
