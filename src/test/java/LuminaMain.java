import hierarchy.HierarchyProvider;
import org.omnimc.lumina.paser.parsers.ProguardParser;
import org.omnimc.lumina.writer.LuminaWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SuppressWarnings("NonFinalUtilityClass")
public class LuminaMain {
    private static final String MINECRAFT_JAR = "C:\\Users\\CryroByte\\AppData\\Roaming\\.minecraft\\versions\\1.21\\1.21.jar";

    public static void main(String[] args) throws IOException {
        final String dir = System.getProperty("user.dir") + "\\run";
        Path path;
        if (!Files.exists(path = Path.of(dir))) {
            Files.createDirectory(path);
        }
        System.out.println("output dir = " + dir);

        //createMappings(dir);


        HierarchyProvider provider = new HierarchyProvider(MINECRAFT_JAR, dir);
        provider.init();
        provider.write();
        //createHierarchyMapping(dir);
    }


    private static void createMappings(String path) throws IOException, InterruptedException {
        LuminaWriter writer = new LuminaWriter("https://piston-data.mojang.com/v1/objects/0530a206839eb1e9b35ec86acbbe394b07a2d9fb/client.txt", new ProguardParser());
        writer.writeTo(path);
        writer.flush();
        writer.close();
    }
}