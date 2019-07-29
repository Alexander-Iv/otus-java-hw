package alexander.ivanov.webserver.web.util;

import groovy.lang.Writable;
import groovy.text.Template;
import groovy.text.markup.MarkupTemplateEngine;
import groovy.text.markup.TemplateConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

public class WriterTemplate {
    private static final Logger logger = LoggerFactory.getLogger(WriterTemplate.class);

    public static void writeTo(PrintWriter writer, String templateFilePath, String templateDir, String keyName, Collection<?> objects) {
        Template template = null;
        try {
            TemplateConfiguration config = new TemplateConfiguration();
            config.setAutoNewLine(true);
            MarkupTemplateEngine engine = null;
            URL tplFileUrl = null;
            File file = null;
            try {
                tplFileUrl = Resources.getResourcePathUrl(templateFilePath);
                //logger.info("tplFileUrl = " + tplFileUrl);
                file = new File(tplFileUrl.toURI());

                URL tplDirUrl = Resources.getResourcePathUrl(templateDir);
                File templateDirFile = new File(tplDirUrl.toURI());

                engine = new MarkupTemplateEngine(Resources.getAppClassLoader(), templateDirFile, config);
            } catch (Resources.ResourceNotFoundException | URISyntaxException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
            template = engine.createTemplate(new FileReader(file), file.getName());
            //logger.info("template = " + template);
            Writable output = null;
            output = template.make(Collections.singletonMap(keyName, objects));
            //logger.info("output = " + output);
            output.writeTo(writer);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
