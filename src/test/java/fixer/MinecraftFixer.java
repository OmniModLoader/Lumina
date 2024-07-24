package fixer;

import org.omnimc.lumina.paser.ParsingContainer;
import org.omnimc.lumina.reader.LuminaReader;
import org.omnimc.lumina.writer.LuminaWriter;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class MinecraftFixer {
    private final LuminaReader luminaReader;

    public MinecraftFixer() {
        this.luminaReader = new LuminaReader();
    }

    public void fixFiles(String dir) throws IOException {
        ParsingContainer parsingContainer = luminaReader.readPath(dir);

        fixClasses(parsingContainer);
        fixMethods(parsingContainer);
        fixFields(parsingContainer);

        LuminaWriter writer = new LuminaWriter(parsingContainer);
        writer.writeTo(dir);
        writer.flush();
        writer.close();
    }

    private void fixClasses(ParsingContainer parsingContainer) {
        HashMap<String, String> classTempMap = new HashMap<>(parsingContainer.getClassNames());

        classTempMap.forEach((obfuscatedName, unObfuscatedName) -> {
            int lastIndex = unObfuscatedName.lastIndexOf('/');
            if (lastIndex != -1) {
                String result = unObfuscatedName.substring(0, lastIndex);
                parsingContainer.addClassName(obfuscatedName, StringRandom.randomClassName(result));
            }
        });
    }

    private void fixMethods(ParsingContainer parsingContainer) { // todo
        HashMap<String, HashMap<String, String>> methodTempMap = new HashMap<>(parsingContainer.getMethodNames());

        methodTempMap.forEach((parentClass, hashMap) -> {

            hashMap.forEach((obfuscatedName, unObfuscatedName) -> { // M_0000001

            });
        });
    }

    private void fixFields(ParsingContainer parsingContainer) { // todo
        HashMap<String, HashMap<String, String>> fieldTempMap = new HashMap<>(parsingContainer.getFieldNames());

        fieldTempMap.forEach((parentClass, hashMap) -> {

            hashMap.forEach((obfuscatedName, unObfuscatedName) -> { // F_0000001

            });
        });
    }
}