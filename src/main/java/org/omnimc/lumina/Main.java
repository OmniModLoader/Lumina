package org.omnimc.lumina;

import org.omnimc.lumina.writer.LuminaWriter;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        LuminaWriter writer = new LuminaWriter("https://piston-data.mojang.com/v1/objects/0530a206839eb1e9b35ec86acbbe394b07a2d9fb/client.txt");
        writer.writeTo("file.location");
        writer.flush();
        writer.close();
    }
}