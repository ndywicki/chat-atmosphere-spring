package org.ndywicki.chat;

import static org.atmosphere.cpr.AtmosphereResource.TRANSPORT.LONG_POLLING;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.atmosphere.config.service.MeteorService;
import org.atmosphere.cpr.AtmosphereResourceEventListenerAdapter;
import org.atmosphere.cpr.Meteor;
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor;
import org.ndywicki.chat.json.Message;
import org.ndywicki.chat.json.User;


@SuppressWarnings("serial")
@MeteorService(path = "/chat", interceptors = {AtmosphereResourceLifecycleInterceptor.class})
public class MeteorChatServlet extends HttpServlet {

	private Map<String, User> users = Collections.synchronizedMap(new HashMap<String, User>());
	private List<Message> messages = Collections.synchronizedList(new ArrayList<Message>());

	@Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);        
    }
    /**
     * Create a {@link Meteor} and use it to suspend the response.
     *
     * @param req An {@link HttpServletRequest}
     * @param res An {@link HttpServletResponse}
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // Set the logger level to TRACE to see what's happening.
        Meteor m = Meteor.build(req).addListener(new AtmosphereResourceEventListenerAdapter());
        
        m.addListener(new MeteorChatEventListener(users, messages));
        m.resumeOnBroadcast(m.transport() == LONG_POLLING ? true : false).suspend(-1);
    }
    
    /**
    * Re-use the {@link Meteor} created on the first GET for broadcasting message.
    *
    * @param req An {@link HttpServletRequest}
    * @param res An {@link HttpServletResponse}
    */
   @Override
   public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    	
   }
}
