package alexander.ivanov.webserver.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RelativeFilesPath {
    private static final Logger logger = LoggerFactory.getLogger(RelativeFilesPath.class);
    private static String rootDir;
    private static String rootPath;
    private static List<String> names = new ArrayList<>();

    public static List<String> get(String path) {
        rootDir = path.substring(0, path.indexOf("/"));
        //logger.info("rootDir = " + rootDir);
        rootPath = path;
        //logger.info("rootPath = " + rootPath);

        getFileList(path).forEach(file -> {
            if (file.isDirectory()) {
                //logger.info("path = {}", rootPath + file.getName() + File.separator);
                get(rootPath + file.getName() + File.separator);
            } else {
                //logger.info("file = {}", file.getPath());
                //logger.info("file.getPath().indexOf(rootDir) = {}", file.getPath().indexOf(rootDir)+1);
                names.add(file.getPath().substring(file.getPath().indexOf(rootDir)));
            }
        });

        return names;
    }

    public static List<String> getNames() {
        return names;
    }

    private static List<File> getFileList(String path) {
        return Arrays.asList(Optional.ofNullable(new File(RelativeDirectoryPath.get(path)).listFiles()).orElse(new File[]{}));
    }
}
