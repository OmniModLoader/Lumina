package org.omnimc.lumina;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public final class URLUtil {

    public static InputStream getInputStreamFromURL(String url) throws IOException, InterruptedException {
        URI uri = URI.create(url);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<InputStream> inputStreamResponse = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        return inputStreamResponse.body();

    }

}
