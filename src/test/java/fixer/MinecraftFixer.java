package fixer;

import org.omnimc.lumina.paser.ParsingContainer;
import org.omnimc.lumina.reader.LuminaReader;
import org.omnimc.lumina.writer.LuminaWriter;

import java.io.IOException;
import java.util.HashMap;

/**
 * This is basically making the mapping files into our mapping style, this makes it so we dont get in trouble for using minecraft names
 *
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class MinecraftFixer {
    private final LuminaReader luminaReader;

    private static final String MINECRAFT_MAIN = "net/minecraft/client/main/";
    private static final String SERVER_MAIN = "net/minecraft/...";

    private final HashMap<String, String> customClassMappings = new HashMap<>();

    /**
     * This takes the unobfuscated method name and its fixed parameter, and then the value is the randomMethodName, this is to make sure we have "correct" hierarchy
     * <p>Assuming that each method with the name "toValue(Ljava/lang/Object;)Ljava/lang/Object;" is a inharented method</p>
     */
    private final HashMap<String, String> customMethodMappings = new HashMap<>();

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

        classTempMap.forEach((obfuscatedName, unObfuscatedName) -> { // todo make it so it looks like this "net/minecraft/CLF_000001"
            if (unObfuscatedName.contains(MINECRAFT_MAIN)) {
                return;
            }

            int lastIndex = unObfuscatedName.lastIndexOf('/');
            if (lastIndex != -1) {
                String result = unObfuscatedName.substring(0, lastIndex);
                String fixedName = StringRandom.randomClassName(result);
                customClassMappings.put(unObfuscatedName, fixedName);
                parsingContainer.addClassName(obfuscatedName, fixedName);
            }
        });
    }

    private void fixMethods(ParsingContainer parsingContainer) { // todo someone help me
        HashMap<String, HashMap<String, String>> methodTempMap = new HashMap<>(parsingContainer.getMethodNames());

        methodTempMap.forEach((parentClass, hashMap) -> {

            hashMap.forEach((obfuscatedName, unObfuscatedName) -> { // M_0000001
                String[] split = obfuscatedName.split("\\(");
                String name = split[0];
                String descriptor = split[1].replace(")", " ");


            });
        });
    }

    private void fixFields(ParsingContainer parsingContainer) {
        HashMap<String, HashMap<String, String>> fieldTempMap = new HashMap<>(parsingContainer.getFieldNames());

        fieldTempMap.forEach((parentClass, hashMap) -> {
            hashMap.forEach((obfuscatedName, unObfuscatedName) -> { // F_0000001
                if (obfuscatedName.equals(unObfuscatedName)) {
                    parsingContainer.addFieldName(parentClass, obfuscatedName, unObfuscatedName);
                    return;
                }

                parsingContainer.addFieldName(parentClass, obfuscatedName, StringRandom.randomFieldName());
            });
        });
    }

    private boolean shouldSkip(String input) {
        switch (input) {
            case "toString", "clone", "equals", "hashCode", "<init>", "<clinit>" -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}