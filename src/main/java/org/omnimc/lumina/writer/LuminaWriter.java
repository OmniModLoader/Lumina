package org.omnimc.lumina.writer;

import org.jetbrains.annotations.NotNull;
import org.omnimc.lumina.paser.IParser;
import org.omnimc.lumina.paser.MappingContainer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * {@link LuminaWriter} is a concrete implementation of {@linkplain AbstractWriter} that handles writing Minecraft
 * mapping data to files. It reads data from URLs or files, parses it using the provided parser, and then writes the
 * parsed data into separate files for classes, methods, and fields.
 *
 * <p>
 * <b>Note:</b> The use of this class itself is not governed by the
 * <a href="https://account.mojang.com/documents/minecraft_eula">Minecraft EULA</a>, but any
 * interaction with Minecraft mappings or data is subject to the
 * <a href="https://account.mojang.com/documents/minecraft_eula">Minecraft EULA</a> and any other applicable
 * licenses.
 * </p>
 *
 * @author <b><a href="https://github.com/CadenCCC">Caden</a></b>
 * @see <a href="https://account.mojang.com/documents/minecraft_eula">Minecraft EULA</a>
 * @since 1.0.0
 */
public class LuminaWriter extends AbstractWriter {

    private FileWriter classWriter;
    private FileWriter fieldWriter;
    private FileWriter methodWriter;

    /**
     * <h6>Creates a {@code LuminaWriter} instance by reading and parsing data from a URL.
     *
     * @param minecraftURL the URL to read Minecraft mapping data from.
     * @param parser       the parser to use for processing the data.
     * @throws IOException          if there are issues with input/output operations.
     * @throws InterruptedException if the operation is interrupted.
     */
    public LuminaWriter(@NotNull String minecraftURL, @NotNull IParser parser) throws IOException, InterruptedException {
        super(minecraftURL, parser);
    }

    /**
     * <h6>Creates a {@code LuminaWriter} instance by reading and parsing data from a file.
     *
     * @param mappingsFile the file to read Minecraft mapping data from.
     * @param parser       the parser to use for processing the data.
     * @throws IOException              if there are issues with input/output operations.
     * @throws IllegalArgumentException if the provided file is not valid.
     */
    public LuminaWriter(@NotNull File mappingsFile, @NotNull IParser parser) throws IOException {
        super(mappingsFile, parser);
    }

    /**
     * <h6>Creates a {@code LuminaWriter} instance with a pre-populated {@linkplain MappingContainer}.
     *
     * @param container the container with pre-parsed data.
     */
    public LuminaWriter(@NotNull MappingContainer container) {
        super(container);
    }

    /**
     * <h6>Writes the parsed data to the specified location.
     *
     * <p>This method creates files for class, method, and field mappings in the specified directory.</p>
     *
     * @param fileLocation the directory where the files will be written.
     * @throws IOException if an error occurs while writing the files.
     */
    @Override
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
     * <h6>Creates a file with field mappings.
     *
     * @param location the directory where the file will be created.
     * @throws IOException if an error occurs while creating or writing to the file.
     */
    private void createFieldFile(File location) throws IOException {
        File fields = new File(location, "fields.mapping");

        fieldWriter = new FileWriter(fields);
        container.getFieldNames().forEach((parentName, hashMap) -> {
            try {
                fieldWriter.write(parentName + ":\n");

                hashMap.forEach((obfuscatedName, unObfuscatedName) -> {
                    try {
                        fieldWriter.write(obfuscatedName + " " + unObfuscatedName + "\n");
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
     * <h6>Creates a file with method mappings.
     *
     * @param location the directory where the file will be created.
     * @throws IOException if an error occurs while creating or writing to the file.
     */
    private void createMethodFile(File location) throws IOException {
        File methods = new File(location, "methods.mapping");

        methodWriter = new FileWriter(methods);
        container.getMethodNames().forEach((parentName, hashMap) -> {
            try {
                methodWriter.write(parentName + ":\n");
                hashMap.forEach((obfuscatedName, unObfuscatedName) -> {
                    try {
                        methodWriter.write(obfuscatedName + " " + unObfuscatedName + "\n");
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
     * <h6>Creates a file with class mappings.
     *
     * @param location the directory where the file will be created.
     * @throws IOException if an error occurs while creating or writing to the file.
     */
    private void createClassFile(File location) throws IOException {
        File classes = new File(location, "classes.mapping");

        classWriter = new FileWriter(classes);
        container.getClassNames().forEach((obfuscatedName, unObfuscatedName) -> {
            try {
                classWriter.write(obfuscatedName + ":" + unObfuscatedName + "\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * <h6>Flushes any buffered data to the files.
     *
     * @throws IOException if an error occurs during flushing.
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
     * <h6>Closes all open file writers and clears the container.
     *
     * @throws IOException if an error occurs while closing the writers.
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