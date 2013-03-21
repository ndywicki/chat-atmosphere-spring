package org.ndywicki.chat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.cpr.DefaultBroadcaster;
import org.atmosphere.websocket.WebSocketEventListener;
import org.ndywicki.chat.json.Event;
import org.ndywicki.chat.json.Message;
import org.ndywicki.chat.json.User;
import org.ndywicki.chat.util.JsonUtil;
import org.ndywicki.chat.util.MD5Util;


@SuppressWarnings("rawtypes")
public class MeteorChatEventListener implements WebSocketEventListener {

//    private static final Logger logger = LoggerFactory.getLogger(MeteorChatEventListener.class);

    private Map<String, User> users = null;
    private List<Message> messages;
    private int HISTORY_MAX = 10;
    private User me = null;
    
    public MeteorChatEventListener(Map<String, User> users, List<Message> messages) {
    	this.users = users;
    	this.messages = messages;
    }

    @Override
    public void onConnect(WebSocketEvent event) {
        AtmosphereResource r = event.webSocket().resource();
        User user;
        for (Map.Entry<String, User> entry : users.entrySet()) {
        	user = entry.getValue();
        	user.setEvent("newusr");
        	this.lookupBroadcaster().broadcast(JsonUtil.ObjectToJson(user), r);
        }
        for (Message message : messages) {
        	this.lookupBroadcaster().broadcast(JsonUtil.ObjectToJson(message), r);
        }
    }

    @Override
	public void onDisconnect(WebSocketEvent event) {        	
    	AtmosphereResource r = event.webSocket().resource();
        this.disconnect(r);
    }   

    @Override
    public void onSuspend(final AtmosphereResourceEvent event) {
    }

    @Override
    public void onResume(AtmosphereResourceEvent event) {
    }

    @Override
    public void onDisconnect(AtmosphereResourceEvent event) {    	
    	AtmosphereResource r = event.getResource();
    	this.disconnect(r);
    }

    @Override
    public void onBroadcast(AtmosphereResourceEvent event) {
    }

    @Override
    public void onThrowable(AtmosphereResourceEvent event) {
    }

    @Override
    public void onHandshake(WebSocketEvent event) {
    }

    @Override
    public void onMessage(WebSocketEvent event) {
    	String json = event.message().toString();    	
    	AtmosphereResource r = event.webSocket().resource();
    	Event data = JsonUtil.JsonStrToClass(json, Event.class);

    	switch(data.getEvent()) {
	    	case "login" :	    		
	    		User user = (User) data;
	    		me = user;
	    		me.setId(r.uuid());
	    		me.setAvatar("https://gravatar.com/avatar/" + MD5Util.md5Hex(me.getMail()) + "?s=50");
	    		users.put(me.getId(), me);
	    		
	    		//emit 'logged' event for me
	    		Event emit = new Event();
	    		emit.setEvent("logged");
	    		this.lookupBroadcaster().broadcast(JsonUtil.ObjectToJson(emit), r);
	    		
	    		//emit me with 'newusr' event for all
	    		me.setEvent("newusr");	    		
	    		this.lookupBroadcaster().broadcast(JsonUtil.ObjectToJson(me));
	    		break;    	

	    	case "newmsg" :	    		
	    		Message message = (Message) data;
	    		
	    		message.setUser(me);	    		
	    		Calendar calendar = Calendar.getInstance();
	    		calendar.setTime(new Date());
	    		message.setH(calendar.get(Calendar.HOUR_OF_DAY));
	    		message.setM(calendar.get(Calendar.MINUTE));	    		
	    		message.setEvent("newmsg");
	    		messages.add(message);
	    		if (messages.size() > HISTORY_MAX) {
	    			messages.remove(0);
	    		}
	    		this.lookupBroadcaster().broadcast(JsonUtil.ObjectToJson(message));
	    		break;
    	}
    	
    }

    @Override
    public void onClose(WebSocketEvent event) {
    }

    @Override
    public void onControl(WebSocketEvent event) {
    }

	@Override
	public void onPreSuspend(AtmosphereResourceEvent event) {
	}
	
	/**
	 * Disconnect user
	 * @param r
	 */
	private void disconnect(AtmosphereResource r) {		
		if(!r.uuid().equals(me.getId())) {
			return;
		}
        users.remove(me.getId());
		me.setEvent("disusr");
		this.lookupBroadcaster().broadcast(JsonUtil.ObjectToJson(me));
	}
	
	/**
	 * Lookup Broadcaster
	 * @return
	 */
	private Broadcaster lookupBroadcaster() {        
        Broadcaster b = BroadcasterFactory.getDefault().lookup(DefaultBroadcaster.class, "/*");
        return b;
    }
}
