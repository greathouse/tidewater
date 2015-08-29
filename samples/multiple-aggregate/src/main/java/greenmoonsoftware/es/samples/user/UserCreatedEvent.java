package greenmoonsoftware.es.samples.user;

import greenmoonsoftware.es.event.Event;

import java.time.Instant;
import java.util.UUID;

public class UserCreatedEvent implements Event {
 private UUID id = UUID.randomUUID();
 private String aggregateId = UUID.randomUUID().toString();
 private String type = "create-user";
 private Instant eventDateTime = Instant.now();
 private String fullname;

 public UserCreatedEvent(String aggregateId, String fullname) {
  this.aggregateId = aggregateId;
  this.fullname = fullname;
 }

 public UUID getId() {
  return id;
 }

 public void setId(UUID id) {
  this.id = id;
 }

 public String getAggregateId() {
  return aggregateId;
 }

 public void setAggregateId(String aggregateId) {
  this.aggregateId = aggregateId;
 }

 public String getType() {
  return type;
 }

 public void setType(String type) {
  this.type = type;
 }

 public Instant getEventDateTime() {
  return eventDateTime;
 }

 public void setEventDateTime(Instant eventDateTime) {
  this.eventDateTime = eventDateTime;
 }

 public String getFullname() {
  return fullname;
 }

 public void setFullname(String fullname) {
  this.fullname = fullname;
 }
}
