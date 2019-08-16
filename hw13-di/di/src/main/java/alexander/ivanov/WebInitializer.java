package alexander.ivanov;

import alexander.ivanov.filters.SessionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    private static final Logger logger = LoggerFactory.getLogger(WebInitializer.class);

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {
                WebConfig.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {
                "/"
        };
    }

    @Override
    protected Filter[] getServletFilters() {
        logger.debug("WebInitializer.getServletFilters");

        String charsEncoding = StandardCharsets.UTF_8.name();
        logger.debug("charsEncoding = {}", charsEncoding);

        CharacterEncodingFilter cef = new CharacterEncodingFilter(charsEncoding, true);
        return new Filter[] {
                new SessionFilter(), new HiddenHttpMethodFilter(), cef
        };
    }
}
