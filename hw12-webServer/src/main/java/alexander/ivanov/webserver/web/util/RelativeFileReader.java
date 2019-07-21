package alexander.ivanov.webserver.web.util;

import java.io.IOException;
import java.io.InputStream;

public class RelativeFileReader {
    public static StringBuffer getFileContent(String path) throws IOException {
        StringBuffer content;
        try (InputStream ins = Resources.getResourceAsStream(path)) {
            content = new StringBuffer();
            int buffer;
            while ((buffer = ins.read()) > 0) {
                content.append(Character.toString(buffer));
            }
        }
        return content;
    }
}
