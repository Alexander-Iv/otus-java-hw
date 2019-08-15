package alexander.ivanov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.groovy.GroovyMarkupConfigurer;
import org.springframework.web.servlet.view.groovy.GroovyMarkupViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan//(basePackages = {"alexander.ivanov"})
public class WebConfig implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.debug("WebConfig.addResourceHandlers");
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        logger.debug("WebConfig.addViewControllers");
        registry.addViewController("").setViewName("home");
        logger.debug("registry = {}", registry);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public GroovyMarkupViewResolver groovyMarkupViewResolver() {
        logger.debug("WebConfig.viewResolver");
        GroovyMarkupViewResolver resolver = new GroovyMarkupViewResolver();
        resolver.setSuffix(".tpl");
        resolver.setRequestContextAttribute("requestContext");
        return resolver;
    }

    @Bean
    public GroovyMarkupConfigurer groovyMarkupConfigurer() {
        logger.debug("WebConfig.markupConfig");
        GroovyMarkupConfigurer groovyMarkupConfigurer = new GroovyMarkupConfigurer();
        groovyMarkupConfigurer.setResourceLoaderPath("/WEB-INF/views/");
        groovyMarkupConfigurer.setAutoNewLine(true);
        logger.debug("groovyMarkupConfigurer = {}", groovyMarkupConfigurer);

        //Paths.get("/WEB-INF/views/**").toFile()
        /*MarkupTemplateEngine templateEngine = new MarkupTemplateEngine(groovyMarkupConfigurer);
        groovyMarkupConfigurer.setTemplateEngine(templateEngine);
        logger.debug("templateEngine = {}", templateEngine);*/

        return groovyMarkupConfigurer;
    }
}
