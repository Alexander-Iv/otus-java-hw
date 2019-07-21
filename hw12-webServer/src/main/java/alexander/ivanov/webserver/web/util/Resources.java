package alexander.ivanov.webserver.web.util;

import alexander.ivanov.webserver.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Resources {
    private static final Logger logger = LoggerFactory.getLogger(Resources.class);
    private static final Class applicationClass = Application.class;

    public static URL getResourcePathUrl(String name) throws ResourceNotFoundException {
        URL resourceUrl = applicationClass.getResource(name);
        logger.info("resourceUrl = {}", resourceUrl);
        if (resourceUrl == null) {
            throw new ResourceNotFoundException();
        }
        return resourceUrl;
    }

    public static String getResourcePath(String name) {
        URL resourceUrl = null;
        try {
            resourceUrl = getResourcePathUrl(name);
            logger.info("resourceUrl = {}", resourceUrl);
        } catch (ResourceNotFoundException e) {
            logger.warn(e.getMessage(), "\"" + name + "\"");
            return "";
        }

        return resourceUrl.toString();
    }

    public static InputStream getResourceAsStream(String name) {
        return applicationClass.getResourceAsStream(name);
    }

    public static Collection<File> getResourceFiles(String directory) {
        Collection<File> filesFromSubDir = new ArrayList<>();

        URL directoryPath = null;
        Collection<File> files = null;
        try {
            directoryPath = getResourcePathUrl(directory);
            URI dirUri = directoryPath.toURI();

            if (dirUri.getScheme().equals("jar")) {
                logger.info("dirUri.resolve(\"./\") = " + dirUri.resolve("./"));
                logger.info("dirUri.resolve(directory) = " + dirUri.resolve(directory));
                logger.info("dirUri.resolve(directory).normalize() = " + dirUri.resolve(directory).normalize());
                logger.info("dirUri.toString() = " + dirUri.toString());
                logger.info("dirUri.getScheme() = " + dirUri.getScheme());
                logger.info("dirUri.getPath() = " + dirUri.getPath());
                logger.info("dirUri.getAuthority() = " + dirUri.getAuthority());
                logger.info("dirUri.getFragment() = " + dirUri.getFragment());
                logger.info("dirUri.getHost() = " + dirUri.getHost());
                logger.info("dirUri.getQuery() = " + dirUri.getQuery());
                logger.info("dirUri.getRawAuthority() = " + dirUri.getRawAuthority());
                logger.info("dirUri.getRawFragment() = " + dirUri.getRawFragment());
                logger.info("dirUri.getRawPath() = " + dirUri.getRawPath());
                logger.info("dirUri.getRawQuery() = " + dirUri.getRawQuery());
                logger.info("dirUri.getRawSchemeSpecificPart() = " + dirUri.getRawSchemeSpecificPart());
                logger.info("dirUri.getRawUserInfo() = " + dirUri.getRawUserInfo());
                logger.info("dirUri.getSchemeSpecificPart() = " + dirUri.getSchemeSpecificPart());
                logger.info("dirUri.getUserInfo() = " + dirUri.getUserInfo());
                logger.info("dirUri.toASCIIString() = " + dirUri.toASCIIString());
                logger.info("dirUri.normalize() = " + dirUri.normalize());
                logger.info("dirUri.parseServerAuthority() = " + dirUri.parseServerAuthority());

                FileSystem fs = FileSystems.newFileSystem(dirUri, Collections.emptyMap());
                logger.info("fs = " + fs);
                fs.getFileStores().forEach(fileStore -> {
                    logger.info("fileStore = " + fileStore.name());
                });
                logger.info("fs.getPath(\"/\") = " + fs.getPath("/"));
                logger.info("fs.getPath(\"pages\") = " + fs.getPath("web/pages"));
                logger.info("fs.getPath(\"/pages\") = " + fs.getPath("/web/pages"));
                logger.info("getPackageNameAsUri() = " + getPackageNameAsUri());
                Stream<Path> fileStream =  Files.walk(fs.getPath(getPackageNameAsUri() + fs.getSeparator() + directory));
                logger.info("fileStream = " + fileStream);
                fileStream.forEach(path -> {
                    logger.info("path = " + path);
                    //logger.info("path.toFile() = " + path.toFile());
                    logger.info("path.toString() = " + path.toString());
                    logger.info("path.toUri() = " + path.toUri());
                    logger.info("path.toUri().toString().substring(0, path.toUri().toString().indexOf(\"!\")) = " + path.toUri().toString().substring(0, path.toUri().toString().indexOf("!")));
                    logger.info("path.toAbsolutePath() = " + path.toAbsolutePath());
                    if (!path.endsWith(directory)) {
                        File newFile = new File(/*fs.toString()*/path.toUri().toString().substring(0, path.toUri().toString().indexOf("!")+1), path.toString());
                        logger.info("newFile = " + newFile);
                        logger.info("newFile.isDirectory() = " + newFile.isDirectory());
                        if (!newFile.isDirectory()) {
                            filesFromSubDir.add(newFile);
                        }
                    }
                });

            } else {
                files = Arrays.asList(Optional.ofNullable(new File(directoryPath.toURI()).listFiles()).orElse(new File[]{}));

                files.forEach(file -> {
                    logger.info("file = {}", file);
                    if (file.isDirectory()) {
                        getResourceFilesUri(getFileRootDirPath(file));
                    } else {
                        filesFromSubDir.add(file);
                    }
                });
            }

        } catch (ResourceNotFoundException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        logger.info("directoryPath = {}", directoryPath);

        filesFromSubDir.forEach(file -> {
            logger.info("file = {}", file);
        });
        return filesFromSubDir;
    }

    public static Collection<URI> getResourceFilesUri(String directory) {
        return getResourceFiles(directory).stream()
                .map(file -> file.toURI())
                .peek(uri -> logger.info("uri = {}", uri))
                .collect(Collectors.toList());
    }

    private static final String getPackageNameAsUri() {
        return applicationClass.getPackageName().replace(".", "/");
    }

    public static final String getSubRootDirName(String uri) {
        return uri.substring(uri.indexOf(getPackageNameAsUri())).substring(getPackageNameAsUri().length()+1);
    }

    private static String getFileRootDirPath(File file) {
        String fileUri = file.toURI().toString();
        String rootFileDirPath = getSubRootDirName(fileUri);
        //logger.info("rootFileDirPath = {}", rootFileDirPath);
        return rootFileDirPath;
    }

    public static class ResourceNotFoundException extends Exception {
        public ResourceNotFoundException() {
            this("Resource {} not found!");
        }

        public ResourceNotFoundException(String message) {
            super(message);
        }
    }
}
