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

package org.omnimc.lumina.reader;

import org.omnimc.lumina.URLUtil;
import org.omnimc.lumina.paser.IParser;
import org.omnimc.lumina.paser.MappingContainer;
import org.omnimc.lumina.paser.parsers.lumina.ClassParser;
import org.omnimc.lumina.paser.parsers.lumina.FieldParser;
import org.omnimc.lumina.paser.parsers.lumina.MethodParser;

import java.io.*;
import java.net.URI;
import java.util.HashMap;

/**
 * {@code LuminaReader} is your go-to class for fetching Minecraft mapping data, whether you're pulling it from a URL or
 * digging it out from local files. It helps you load this data into a {@linkplain MappingContainer}, so you can easily
 * work with Minecraft mappings.
 *
 * @author <b><a href="https://github.com/CadenCCC">Caden</a></b>
 * @since 1.0.0
 */
public class LuminaReader extends AbstractReader {

    /**
     * <h6>Fetches Minecraft mappings from the given URL and returns a fully populated {@linkplain MappingContainer}.
     *
     * <p>This method expects URLs that point to mappings for classes, methods, and fields in the format:
     * <code>{url}/classes.mapping</code>, <code>{url}/methods.mapping</code>, and
     * <code>{url}/fields.mapping</code>.</p>
     *
     * @param url the base URL to fetch the Minecraft mappings from.
     * @return a {@linkplain MappingContainer} filled with the mapping data.
     * @throws IOException          if something goes wrong with the input/output operations.
     * @throws InterruptedException if the operation is interrupted while fetching data.
     */
    public MappingContainer readURL(String url) throws IOException, InterruptedException { // todo
        InputStream classes = URLUtil.getInputStreamFromURL(url + "/classes.mapping");
        InputStream methods = URLUtil.getInputStreamFromURL(url + "/methods.mapping");
        InputStream fields = URLUtil.getInputStreamFromURL(url + "/fields.mapping");
        return null;
    }

    /**
     * <h6>Reads Minecraft mappings from files located at the given path and returns a populated
     * {@linkplain MappingContainer}.
     *
     * <p>This method looks for files named <code>classes.mapping</code>, <code>methods.mapping</code>, and
     * <code>fields.mapping</code> in the specified directory.</p>
     *
     * @param path the directory path where the Minecraft mapping files are located.
     * @return a {@linkplain MappingContainer} filled with the mapping data.
     * @throws IOException if there’s an issue reading the files.
     */
    public MappingContainer readPath(String path) throws IOException {
        File location = new File(path);
        if (location.isFile()) {
            location = new File(location.getParent());
        }

        MappingContainer mappingContainer = new MappingContainer();

        fileLookup(new File(location, "classes.mapping"), new ClassParser(), mappingContainer); // Class Parsing
        fileLookup(new File(location, "methods.mapping"), new MethodParser(), mappingContainer); // Method Parsing
        fileLookup(new File(location, "fields.mapping"), new FieldParser(), mappingContainer); // Field Parsing

        return mappingContainer;
    }

    public static MappingContainer read(String path) throws IOException {
        return new LuminaReader().readPath(path);
    }

    public static MappingContainer read(URI uri) throws IOException, InterruptedException {
        return new LuminaReader().readURL(uri.toURL().toString());
    }

    /**
     * <h6>Reads from a file and uses the provided parser to populate the given {@linkplain MappingContainer}.
     *
     * @param location         the file to read data from.
     * @param parser           the parser that will handle the data.
     * @param mappingContainer the container to fill with parsed data.
     * @throws IOException if an error occurs while reading the file.
     */
    private void fileLookup(File location, IParser parser, MappingContainer mappingContainer) throws IOException {
        if (!location.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(location))) {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                parser.run(inputLine, mappingContainer);
            }
        }
    }
}