package alexander.ivanov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.resource.WebJarsResourceResolver;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebMvc
@EnableAsync
@ComponentScan(basePackages = {"alexander.ivanov"})
public class WebConfig implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    /*@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.debug("WebConfig.addResourceHandlers");
        registry.addResourceHandler("/WEB-INF/views/**").addResourceLocations("/");
        registry.addResourceHandler("/scripts/**").addResourceLocations("/WEB-INF/scripts/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/")
                .setCacheControl(CacheControl.maxAge(1L, TimeUnit.DAYS).cachePublic())
                .resourceChain(false)
                .addResolver(new WebJarsResourceResolver());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        logger.debug("WebConfig.addViewControllers");
        registry.addRedirectViewController("", "/home");
        logger.debug("registry = {}", registry);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }*/
}
