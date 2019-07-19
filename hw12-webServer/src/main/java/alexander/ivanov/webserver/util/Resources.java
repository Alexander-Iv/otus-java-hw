package alexander.ivanov.webserver.util;

import alexander.ivanov.webserver.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

        return resourceUrl.getPath();
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
                logger.info("directoryPath = {}", directoryPath);
                logger.info("dirUri = {}", dirUri);
                logger.info("dirUri.getPath() = {}", dirUri.getPath());
                logger.info("dirUri.toString() = {}", dirUri.toString());
                logger.info("dirUri.toString().substring(0, dirUri.toString().indexOf(\"!\")) = {}",
                        dirUri.toString().substring(0, dirUri.toString().indexOf("!")));

                JarFile jf = new JarFile(dirUri.toString().substring(0, dirUri.toString().indexOf("!")), false);
                logger.info("jf = {}", jf);
                jf.stream().forEach(jarEntry -> {
                    logger.info("jarEntry = {}", jarEntry);
                });
                Enumeration<JarEntry> entries = jf.entries();
                logger.info("entries = {}", entries);
                Iterator iter = entries.asIterator();
                while(iter.hasNext()) {
                    logger.info("iter.next() = {}", iter.next());
                }
                /*JarEntry entry = null;
                while((entry = entries.nextElement()) != null) {
                    logger.info("entry = {}", entry);
                }*/

            }
            logger.info("getResourceAsStream(directory) = {}", (getResourceAsStream(directory) == null));
            JarInputStream jis = new JarInputStream(getResourceAsStream(directory));
            logger.info("jis = {}", jis);
            ZipEntry entry;
            while ((entry = jis.getNextEntry()) != null) {
                logger.info("entry.getName() = {}", entry.getName());
                //logger.info("entry.getRealName() = {}", entry.getRealName());
                //logger.info("entry.getAttributes() = {}", entry.getAttributes());
            }

            Path filePath = null;
            if (dirUri.getScheme().equals("jar")) {
                logger.info("dirUri.getScheme() = {}", dirUri.getScheme());
                FileSystem fileSystem = FileSystems.newFileSystem(dirUri, Collections.emptyMap());
                logger.info("directory = {}", directory);
                filePath = fileSystem.getPath(directory);
            } else {
                filePath = Paths.get(dirUri);
            }
            logger.info("filePath = {}", filePath);

            Files.walk(filePath).forEach(path -> {
                File file = path.toFile();
                logger.info("path.endsWith(directory) = {}", path.endsWith(directory));
                if (!path.endsWith(directory)) {
                    if (file.isDirectory()) {
                        getResourceFilesUri(getFileRootDirPath(file));
                    } else {
                        filesFromSubDir.add(file);
                    }
                    logger.info("~~~ file = {}", file);
                }
            });

            /*Files.list(filePath).forEach(path -> {
                File file = path.toFile();
                logger.info("~~~ file = {}", file);
                if (file.isDirectory()) {
                    getResourceFilesUri(getFileRootDirPath(file));
                } else {
                    filesFromSubDir.add(file);
                }
            });*/

            //files = Arrays.asList(Optional.ofNullable(new File(directoryPath.toURI()).listFiles()).orElse(new File[]{}));
        } catch (ResourceNotFoundException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        logger.info("directoryPath = {}", directoryPath);

        /*files.forEach(file -> {
            logger.info("file = {}", file);
            if (file.isDirectory()) {
                getResourceFilesUri(getFileRootDirPath(file));
            } else {
                filesFromSubDir.add(file);
            }
        });*/

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
