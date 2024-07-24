package org.omnimc.lumina.writer;

import org.omnimc.lumina.paser.ParsingContainer;
import org.omnimc.lumina.paser.parsers.ProguardParser;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.AccessDeniedException;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class LuminaWriter extends Writer {
    private final ParsingContainer container = new ParsingContainer() {};

    public LuminaWriter(String minecraftURL) throws IOException, InterruptedException {
        URI uri = URI.create(minecraftURL);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<InputStream> inputStreamResponse = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        BufferedReader in = new BufferedReader(new InputStreamReader(inputStreamResponse.body()));
        ProguardParser proguardParser = new ProguardParser();

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            proguardParser.run(inputLine, container);
        }

        client.close();
    }

    public LuminaWriter(File mappingsFile) {

    }

    public void writeTo(String fileLocation) {
        /*  */
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        throw new AccessDeniedException("Cannot access this writer method!");
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() {

    }
}
