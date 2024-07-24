package org.omnimc.lumina.writer;

import org.omnimc.lumina.URLUtil;
import org.omnimc.lumina.paser.IParser;
import org.omnimc.lumina.paser.ParsingContainer;

import java.io.*;
import java.rmi.AccessException;

/**
 * {@linkplain LuminaWriter} class reads Minecraft mapping URLs or files on the local computer,
 * parses them using the {@linkplain IParser}, and writes the parsed data into separate files
 * for {@linkplain LuminaWriter#createClassFile(File)}, {@linkplain LuminaWriter#createFieldFile(File)}, and {@linkplain LuminaWriter#createMethodFile(File)}.
 *
 * <p>
 * <b>Note:</b> Usage and modification of this class itself are not governed by the Minecraft End User License Agreement (<a href="https://account.mojang.com/documents/minecraft_eula">EULA</a>).
 * However, any interaction with Minecraft mappings or data is subject to the <a href="https://account.mojang.com/documents/minecraft_eula">Minecraft EULA</a> and any other applicable licenses.
 * </p>
 *
 * @see <a href="https://account.mojang.com/documents/minecraft_eula">Minecraft EULA</a>
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class LuminaWriter extends Writer {
    /**
     * FileWriter for the class File
     * <p>
     * Usage can be found here {@linkplain LuminaWriter#createClassFile(File)}
     */
    private FileWriter classWriter;
    /**
     * FileWriter for the field File
     * <p>
     * Usage can be found here {@linkplain LuminaWriter#createFieldFile(File)}
     */
    private FileWriter fieldWriter;
    /**
     * FileWriter for the method File
     * <p>
     * Usage can be found here {@linkplain LuminaWriter#createMethodFile(File)}
     */
    private FileWriter methodWriter;

    //@formatter:off
    private final ParsingContainer container = new ParsingContainer() {};
    //@formatter:on

    /**
     * Constructs a new {@code LuminaWriter} instance by reading Minecraft mappings from a URL.
     *
     * @param minecraftURL the URL to read Minecraft mappings from.
     * @param parser       the parser to parse the mappings.
     * @throws IOException          if an I/O error occurs.
     * @throws InterruptedException if the thread is interrupted.
     */
    public LuminaWriter(String minecraftURL, IParser parser) throws IOException, InterruptedException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(URLUtil.getInputStreamFromURL(minecraftURL)))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                parser.run(inputLine, container);
            }
        }
    }

    /**
     * Constructs a new {@code LuminaWriter} instance by reading Minecraft mappings from a file.
     *
     * @param mappingsFile the file to read Minecraft mappings from.
     * @param parser       the parser to parse the mappings.
     * @throws IOException if an I/O error occurs.
     * @throws IllegalArgumentException if the provided {@code mappingsFile} is not a valid file.
     */
    public LuminaWriter(File mappingsFile, IParser parser) throws IOException {
        if (!mappingsFile.isFile()) {
            throw new IllegalArgumentException("`mappingsFile` has to be an actual file not a directory!");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(mappingsFile))) {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                parser.run(inputLine, container);
            }
        }
    }

    /**
     * Writes the parsed data to the specified location.
     *
     * @param fileLocation the location to write the files to.
     * @throws IOException if an I/O error occurs.
     */
    public void writeTo(String fileLocation) throws IOException {
        File location = new File(fileLocation);
        if (location.isFile()) {
            location = new File(location.getParent());
        }

        createClassFile(location);
        createMethodFile(location);
        createFieldFile(location);
    }

    /**
     * Creates a file containing field mappings.
     *
     * @param location the location to create the file.
     * @throws IOException if an I/O error occurs.
     */
    private void createFieldFile(File location) throws IOException {
        File fields = new File(location, "fields.mapping");

        fieldWriter = new FileWriter(fields);
        container.getFieldNames().forEach((parentName, hashMap) -> {
            try {
                fieldWriter.write(parentName + ":\r\n");

                hashMap.forEach((obfuscatedName, unObfuscatedName) -> {
                    try {
                        fieldWriter.write(obfuscatedName + " " + unObfuscatedName + "\r\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Creates a file containing method mappings.
     *
     * @param location the location to create the file.
     * @throws IOException if an I/O error occurs.
     */
    private void createMethodFile(File location) throws IOException {
        File methods = new File(location, "methods.mapping");

        methodWriter = new FileWriter(methods);
        container.getMethodNames().forEach((parentName, hashMap) -> {
            try {
                methodWriter.write(parentName + ":\r\n");
                hashMap.forEach((obfuscatedName, unObfuscatedName) -> {
                    try {
                        methodWriter.write(obfuscatedName + " " + unObfuscatedName + "\r\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Creates a file containing class mappings.
     *
     * @param location the location to create the file.
     * @throws IOException if an I/O error occurs.
     */
    private void createClassFile(File location) throws IOException {
        File classes = new File(location, "classes.mapping");

        classWriter = new FileWriter(classes);
        container.getClassNames().forEach((obfuscatedName, unObfuscatedName) -> {
            try {
                classWriter.write(obfuscatedName + ":" + unObfuscatedName + "\r\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * This method is not accessible in this class.
     *
     * @param cbuf the buffer to write.
     * @param off  the offset within the buffer.
     * @param len  the length of data to write.
     * @throws IOException always thrown to indicate this method is not accessible.
     */
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        throw new AccessException("You cannot access this method.");
    }

    /**
     * Flushes the writers for class, field, and method files.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void flush() throws IOException {
        if (classWriter != null) {
            classWriter.flush();
        }

        if (fieldWriter != null) {
            fieldWriter.flush();
        }

        if (methodWriter != null) {
            methodWriter.flush();
        }
    }

    /**
     * Closes the writers for class, field, and method files, and clears the container.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        if (classWriter != null) {
            classWriter.close();
        }

        if (fieldWriter != null) {
            fieldWriter.close();
        }

        if (methodWriter != null) {
            methodWriter.close();
        }

        container.close();
    }
}
