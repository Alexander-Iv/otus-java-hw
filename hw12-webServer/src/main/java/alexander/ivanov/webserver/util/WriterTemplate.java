package alexander.ivanov.webserver.util;

import groovy.lang.Writable;
import groovy.text.Template;
import groovy.text.markup.MarkupTemplateEngine;
import groovy.text.markup.TemplateConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;

public class WriterTemplate {
    public static void writeTo(PrintWriter writer, String templateFilePath, String keyName, Collection<?> objects) {
        Template template = null;
        try {
            TemplateConfiguration config = new TemplateConfiguration();
            MarkupTemplateEngine engine = new MarkupTemplateEngine(config);
            File file = new File(templateFilePath);
            template = engine.createTemplate(file);
            Writable output = template.make(Collections.singletonMap(keyName, objects));
            output.writeTo(writer);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
