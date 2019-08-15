package alexander.ivanov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/*@Configuration
@ComponentScan
@EnableWebMvc*/
public class Application /*implements WebApplicationInitializer*/ {
    /*private static final Logger logger = LoggerFactory.getLogger(Application.class);
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        logger.info("servletContext = {}", servletContext);

        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
        //ac.register(AppConfig.class);
        ac.refresh();

        DispatcherServlet servlet = new DispatcherServlet(ac);
        ServletRegistration.Dynamic registration = servletContext.addServlet("di", servlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/di/*");

        *//*
        context.addServlet(new ServletHolder("Auth", new AuthServlet(userDao)), "/auth/*");
        context.addServlet(new ServletHolder("Home", new HomeServlet(userDao)), "/home/*");
        context.addServlet(new ServletHolder("Logout", new LogoutServlet()), "/logout/*");
        context.addServlet(new ServletHolder("Register", new RegisterServlet(userDao)), "/register/*");
        *//*
    }*/
}
