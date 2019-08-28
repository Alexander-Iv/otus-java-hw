package alexander.ivanov.ms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
@EnableAsync
@ComponentScan(basePackages = {"alexander.ivanov.ms"})
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
    }*/

    /*@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        logger.debug("WebConfig.addViewControllers");
        registry.addRedirectViewController("/ms", "/fe");
        logger.debug("registry = {}", registry);
    }*/

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable("ms-dispatcher");
    }
}
