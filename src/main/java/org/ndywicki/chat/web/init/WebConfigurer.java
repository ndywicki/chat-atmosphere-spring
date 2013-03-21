package org.ndywicki.chat.web.init;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ndywicki.chat.config.ApplicationConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;


public class WebConfigurer implements ServletContextListener {

    private final Log log = LogFactory.getLog(WebConfigurer.class);

    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        log.info("Web application configuration");
        
        log.debug("Configuring Spring root application context");
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(ApplicationConfiguration.class);
        rootContext.setServletContext(servletContext);
        rootContext.refresh();

        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, rootContext);
        
        log.debug("Configuring Spring Web application context");
        AnnotationConfigWebApplicationContext dispatcherServletConfig = new AnnotationConfigWebApplicationContext();
        dispatcherServletConfig.setParent(rootContext);

        log.debug("Registering Meteor Servlet");
        ServletRegistration.Dynamic meteorServlet = servletContext.addServlet("MeteorServlet", org.atmosphere.cpr.MeteorServlet.class);
        meteorServlet.addMapping("/chat");
        meteorServlet.setLoadOnStartup(0);
        meteorServlet.setInitParameter("org.atmosphere.servlet", "org.ndywicki.chat.MeteorChatServlet");
        
        log.debug("Web application fully configured");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Destroying Web application");
        log.debug("Web application destroyed");
    }

}