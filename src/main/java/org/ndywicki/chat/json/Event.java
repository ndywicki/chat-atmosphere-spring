package org.ndywicki.chat.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use=Id.NAME, include=As.PROPERTY, property="type")
@JsonSubTypes({
    @JsonSubTypes.Type(value=User.class, name="user"),
    @JsonSubTypes.Type(value=Message.class, name="message")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
	
	protected String event;

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
}
