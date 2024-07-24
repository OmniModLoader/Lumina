package org.omnimc.lumina.reader;

import org.omnimc.lumina.URLUtil;
import org.omnimc.lumina.paser.IParser;
import org.omnimc.lumina.paser.ParsingContainer;
import org.omnimc.lumina.paser.parsers.lumina.ClassParser;
import org.omnimc.lumina.paser.parsers.lumina.FieldParser;
import org.omnimc.lumina.paser.parsers.lumina.MethodParser;

import java.io.*;

/**
 * The {@code LuminaReader} class reads and populates a {@linkplain ParsingContainer} with data from Minecraft mapping
 * URLs or files on the local computer.
 *
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class LuminaReader {

    /**
     * Reads Minecraft mappings from a URL and returns a populated {@linkplain ParsingContainer}.
     *
     * @param url the URL to read Minecraft mappings from.
     * @return a populated {@linkplain ParsingContainer}.
     * @throws IOException          if an I/O error occurs.
     * @throws InterruptedException if the thread is interrupted.
     */
    public ParsingContainer readURL(String url) throws IOException, InterruptedException { // todo
        InputStream classes = URLUtil.getInputStreamFromURL(url + "/classes.mapping");
        InputStream methods = URLUtil.getInputStreamFromURL(url + "/methods.mapping");
        InputStream fields = URLUtil.getInputStreamFromURL(url + "/fields.mapping");
        return null;
    }

    /**
     * Reads Minecraft mappings from a file and returns a populated {@linkplain ParsingContainer}.
     *
     * @param path the file to read Minecraft mappings from.
     * @return a populated {@linkplain ParsingContainer}.
     * @throws IOException if an I/O error occurs.
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
     * Reads data from an input stream using a specified parser and populates the container.
     *
     * @param location         the location of the file it will read out of.
     * @param parser           the parser to parse the data.
     * @param parsingContainer the container to populate with parsed data.
     * @throws IOException if an I/O error occurs.
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