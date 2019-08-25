package alexander.ivanov.fe;

import alexander.ivanov.messageSystem.MessageSystemContext;
import alexander.ivanov.messageSystem.services.DbService;
import alexander.ivanov.messageSystem.services.FeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.groovy.GroovyMarkupConfigurer;
import org.springframework.web.servlet.view.groovy.GroovyMarkupViewResolver;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"alexander.ivanov"})
public class WebConfig implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
    /*private MessageSystemContext context;

    @Autowired
    public void setContext(MessageSystemContext context) {
        this.context = context;
    }*/

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.debug("WebConfig.addResourceHandlers");
        //registry.addResourceHandler("/META-INF/views/").addResourceLocations("/");
        registry.addResourceHandler("/WEB-INF/views/").addResourceLocations("/");
        registry.addResourceHandler("/WEB-INF/scripts/").addResourceLocations("/scripts/");
        //registry.addResourceHandler("classpath:/webjars/**").addResourceLocations("/webjars/");
        registry.addResourceHandler("classpath:/webjars/**").addResourceLocations("/webjars/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        logger.debug("WebConfig.addViewControllers");
        registry.addRedirectViewController("", "/home");
        //registry.addViewController("").setViewName("home");
        logger.debug("registry = {}", registry);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver(ApplicationContext applicationContext){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(true);
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(SpringResourceTemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver(SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setOrder(1);
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }
}
