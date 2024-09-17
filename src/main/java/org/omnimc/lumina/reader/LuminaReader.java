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
import org.omnimc.lumina.paser.ParsingContainer;
import org.omnimc.lumina.paser.parsers.lumina.ClassParser;
import org.omnimc.lumina.paser.parsers.lumina.FieldParser;
import org.omnimc.lumina.paser.parsers.lumina.MethodParser;

import java.io.*;

/**
 * {@code LuminaReader} is your go-to class for fetching Minecraft mapping data, whether you're pulling it from a URL or
 * digging it out from local files. It helps you load this data into a {@linkplain ParsingContainer}, so you can easily
 * work with Minecraft mappings.
 *
 * @author <b><a href="https://github.com/CadenCCC">Caden</a></b>
 * @since 1.0.0
 */
public class LuminaReader extends AbstractReader {

    /**
     * <h6>Fetches Minecraft mappings from the given URL and returns a fully populated {@linkplain ParsingContainer}.
     *
     * <p>This method expects URLs that point to mappings for classes, methods, and fields in the format:
     * <code>{url}/classes.mapping</code>, <code>{url}/methods.mapping</code>, and
     * <code>{url}/fields.mapping</code>.</p>
     *
     * @param url the base URL to fetch the Minecraft mappings from.
     * @return a {@linkplain ParsingContainer} filled with the mapping data.
     * @throws IOException          if something goes wrong with the input/output operations.
     * @throws InterruptedException if the operation is interrupted while fetching data.
     */
    public ParsingContainer readURL(String url) throws IOException, InterruptedException { // todo
        InputStream classes = URLUtil.getInputStreamFromURL(url + "/classes.mapping");
        InputStream methods = URLUtil.getInputStreamFromURL(url + "/methods.mapping");
        InputStream fields = URLUtil.getInputStreamFromURL(url + "/fields.mapping");
        return null;
    }

    /**
     * <h6>Reads Minecraft mappings from files located at the given path and returns a populated
     * {@linkplain ParsingContainer}.
     *
     * <p>This method looks for files named <code>classes.mapping</code>, <code>methods.mapping</code>, and
     * <code>fields.mapping</code> in the specified directory.</p>
     *
     * @param path the directory path where the Minecraft mapping files are located.
     * @return a {@linkplain ParsingContainer} filled with the mapping data.
     * @throws IOException if there’s an issue reading the files.
     */
    public ParsingContainer readPath(String path) throws IOException {
        File location = new File(path);
        if (location.isFile()) {
            location = new File(location.getParent());
        }

        ParsingContainer parsingContainer = new ParsingContainer() {
        };

        fileLookup(new File(location, "classes.mapping"), new ClassParser(), parsingContainer); // Class Parsing
        fileLookup(new File(location, "methods.mapping"), new MethodParser(), parsingContainer); // Method Parsing
        fileLookup(new File(location, "fields.mapping"), new FieldParser(), parsingContainer); // Field Parsing

        return parsingContainer;
    }

    /**
     * <h6>Reads from a file and uses the provided parser to populate the given {@linkplain ParsingContainer}.
     *
     * @param location         the file to read data from.
     * @param parser           the parser that will handle the data.
     * @param parsingContainer the container to fill with parsed data.
     * @throws IOException if an error occurs while reading the file.
     */
    private void fileLookup(File location, IParser parser, ParsingContainer parsingContainer) throws IOException {
        if (!location.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(location))) {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                parser.run(inputLine, parsingContainer);
            }
        }
    }
}