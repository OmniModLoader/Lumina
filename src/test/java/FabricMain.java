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

import hierarchy.HierarchyProvider;
import org.omnimc.lumina.paser.MappingContainer;
import org.omnimc.lumina.paser.parsers.fabric.FabricParser;
import org.omnimc.lumina.reader.LuminaReader;
import org.omnimc.lumina.writer.LuminaWriter;
import swap.SwapMapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class FabricMain {
    private static final String MINECRAFT_JAR = "C:\\Users\\CryroByte\\AppData\\Roaming\\.minecraft\\versions\\1.21\\1.21.jar";

    public static void main(String[] args) throws IOException, InterruptedException {
        final String dir = System.getProperty("user.dir") + "\\run";
        Path path;
        if (!Files.exists(path = Path.of(dir))) {
            Files.createDirectory(path);
        }
        System.out.println("output dir = " + dir);

        //createFabricMapping(path);

        createFabricMapping(path);

        LuminaReader reader = new LuminaReader();
        MappingContainer originalMappings = reader.readPath(dir + "\\hierarchy");
        MappingContainer mappingContainer = reader.readPath(path + "\\fabric\\hierarchy");

        SwapMapping mapping = new SwapMapping(originalMappings, mappingContainer);
        mapping.swap(path + "\\fabric\\swapped");
    }

    private static void createFabricMapping(Path path) throws IOException, InterruptedException {
        LuminaWriter writer = new LuminaWriter(new File("C:\\Users\\CryroByte\\Desktop\\Lumina-github\\1.21.tiny"), new FabricParser());
        writer.writeTo(path + "\\fabric");
        writer.flush();
        writer.close();

        HierarchyProvider hierarchyProvider = new HierarchyProvider(MINECRAFT_JAR, path + "\\fabric");
        hierarchyProvider.init();
        hierarchyProvider.write();
    }

}