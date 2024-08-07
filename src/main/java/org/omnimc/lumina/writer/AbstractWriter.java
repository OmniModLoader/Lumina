/*
 * MIT License
 *
 * Copyright (c) 2024 OmniMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.omnimc.lumina.writer;

import org.jetbrains.annotations.NotNull;
import org.omnimc.lumina.URLUtil;
import org.omnimc.lumina.paser.IParser;
import org.omnimc.lumina.paser.ParsingContainer;

import java.io.*;

/**
 * {@code AbstractWriter} serves as a base class for writing Minecraft mapping data to files. It handles reading and
 * parsing data from a URL or file into a {@linkplain ParsingContainer}.
 *
 * <p>This class sets up the container with parsed data and provides abstract methods for
 * writing the data to files, flushing the content, and closing resources.</p>
 *
 * @author <b><a href="https://github.com/CadenCCC">Caden</a></b>
 * @since 1.2.5
 */
public abstract class AbstractWriter {
    protected final ParsingContainer container;

    /**
     * <h6>Constructs an {@code AbstractWriter} by fetching and parsing data from a URL.
     *
     * <p>This constructor reads data from the provided URL, parses it using the given parser,
     * and stores the results in a new {@linkplain ParsingContainer}.</p>
     *
     * @param minecraftURL the URL from which to fetch Minecraft mapping data.
     * @param parser       the parser used to process the data.
     * @throws IOException          if there are issues with input/output operations.
     * @throws InterruptedException if the operation is interrupted.
     */
    public AbstractWriter(String minecraftURL, IParser parser) throws IOException, InterruptedException {
        this(new ParsingContainer() {
        });

        try (BufferedReader in = new BufferedReader(new InputStreamReader(URLUtil.getInputStreamFromURL(minecraftURL)))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                parser.run(inputLine, container);
            }
        }
    }

    /**
     * <h6>Constructs an {@code AbstractWriter} by reading and parsing data from a file.
     *
     * <p>This constructor reads data from the specified file, parses it with the provided parser,
     * and stores the results in a new {@linkplain ParsingContainer}.</p>
     *
     * @param mappingsFile the file to read Minecraft mapping data from.
     * @param parser       the parser used to process the data.
     * @throws IOException              if there are issues with input/output operations.
     * @throws IllegalArgumentException if the provided file is not valid.
     */
    public AbstractWriter(@NotNull File mappingsFile, @NotNull IParser parser) throws IOException {
        this(new ParsingContainer() {
        });

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
     * <h6>Constructs an {@code AbstractWriter} with an existing {@linkplain ParsingContainer}.
     *
     * <p>This constructor allows you to initialize the writer with a container that's already populated
     * with parsed data.</p>
     *
     * @param container the container holding the parsed data.
     */
    public AbstractWriter(@NotNull ParsingContainer container) {
        this.container = container;
    }

    /**
     * <h6>Writes the parsed data to a specified location.
     *
     * @param fileLocation the directory where the files should be saved.
     * @throws Exception if any errors occur during the writing process.
     */
    public abstract void writeTo(String fileLocation) throws Exception;

    /**
     * <h6>Flushes any buffered data to the files.
     *
     * @throws Exception if there are issues with flushing the data.
     */
    public abstract void flush() throws Exception;

    /**
     * <h6>Closes all open resources and clears the container.
     *
     * @throws Exception if there are issues with closing resources.
     */
    public abstract void close() throws Exception;
}