import org.omnimc.lumina.paser.ParsingContainer;
import org.omnimc.lumina.paser.parsers.ProguardParser;
import org.omnimc.lumina.reader.LuminaReader;
import org.omnimc.lumina.writer.LuminaWriter;

import java.io.IOException;

@SuppressWarnings("NonFinalUtilityClass")
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        final String dir = System.getProperty("user.dir");
        System.out.println("current dir = " + dir);

        createMappings(dir);

        //getReader(dir);
    }

    private static ParsingContainer getReader(String path) throws IOException {
        LuminaReader reader = new LuminaReader();
        return reader.readPath(path);
    }

    private static void createMappings(String path) throws IOException, InterruptedException {
        LuminaWriter writer = new LuminaWriter("https://piston-data.mojang.com/v1/objects/0530a206839eb1e9b35ec86acbbe394b07a2d9fb/client.txt", new ProguardParser());
        writer.writeTo(path);
        writer.flush();
        writer.close();
    }
}