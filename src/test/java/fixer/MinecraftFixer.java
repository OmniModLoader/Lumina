package fixer;

import org.jetbrains.annotations.NotNull;
import org.omnimc.lumina.paser.MappingContainer;
import org.omnimc.lumina.reader.LuminaReader;
import org.omnimc.lumina.writer.LuminaWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
        MappingContainer mappingContainer = luminaReader.readPath(dir);

        fixClasses(mappingContainer);
        fixMethods(mappingContainer);
        fixFields(mappingContainer);

        LuminaWriter writer = new LuminaWriter(mappingContainer);
        writer.writeTo(dir);
        writer.flush();
        writer.close();
    }

    private void fixClasses(MappingContainer mappingContainer) {
        HashMap<String, String> classTempMap = new HashMap<>(mappingContainer.getClassNames());

        classTempMap.forEach((obfuscatedName, unObfuscatedName) -> { // todo make it so it looks like this "net/minecraft/CLF_000001"
            if (unObfuscatedName.contains(MINECRAFT_MAIN)) {
                return;
            }

            int lastIndex = unObfuscatedName.lastIndexOf('/');
            if (lastIndex != -1) {
                String result = unObfuscatedName.substring(0, lastIndex);
                String fixedName = StringRandom.randomClassName(result);
                customClassMappings.put(unObfuscatedName, fixedName);
                mappingContainer.addClassName(obfuscatedName, fixedName);
            }
        });
    }

    private void fixMethods(MappingContainer mappingContainer) { // todo someone help me
        HashMap<String, HashMap<String, String>> methodTempMap = new HashMap<>(mappingContainer.getMethodNames());

        for (Map.Entry<String, HashMap<String, String>> methodTempMapEntry : methodTempMap.entrySet()) {
            String parentClass = methodTempMapEntry.getKey();

            for (Map.Entry<String, String> methodTempEntry : methodTempMapEntry.getValue().entrySet()) {
                String obfuscatedName = methodTempEntry.getKey();
                String unObfuscatedName = methodTempEntry.getValue();

                String[] split = obfuscatedName.split("\\(");
                String name = split[0];
                String descriptor = getDescriptorString(split);

                if (shouldSkip(name)) {
                    mappingContainer.addMethodName(parentClass, name + descriptor, unObfuscatedName);
                    //parsingContainer.removeMethodName(parentClass, name + split[0]);
                    continue;
                }

                String hierarchy = customMethodMappings.get(unObfuscatedName + descriptor);
                if (hierarchy != null) {
                    System.out.println("Hierarchy found! ");
                    mappingContainer.addMethodName(parentClass, name + descriptor, hierarchy);
                    //parsingContainer.removeMethodName(parentClass, name + split[0]);
                } else {
                    System.out.println("Making a mapping for: " + unObfuscatedName + descriptor);
                    String randomMethodName = StringRandom.randomMethodName();
                    customMethodMappings.put(unObfuscatedName + descriptor, randomMethodName);
                    mappingContainer.addMethodName(parentClass, name + descriptor, randomMethodName);
                    //parsingContainer.removeMethodName(parentClass, name + split[0]);
                }
            }
        }

/*        methodTempMap.forEach((parentClass, hashMap) -> {

            hashMap.forEach((obfuscatedName, unObfuscatedName) -> { // M_0000001

                customClassMappings.forEach((unObfuscatedName1, fixedName) -> { // Super brute force, this can be fixed;
                    if (descriptor[0].contains(unObfuscatedName1)) {
                        descriptor[0] = descriptor[0].replace(unObfuscatedName1, fixedName);
                    }
                });


            });
        });*/
    }

    private @NotNull String getDescriptorString(String[] split) {
        String descriptor = "(" + split[1];

        for (Map.Entry<String, String> customClassMappings : customClassMappings.entrySet()) { // This is Brute force on steroids
            String unObfClassName = customClassMappings.getKey();
            String fixedName = customClassMappings.getValue();

            if (descriptor.contains(unObfClassName)) {
                descriptor = descriptor.replace(unObfClassName, fixedName);
            }
        }
        return descriptor;
    }

    private void fixFields(MappingContainer mappingContainer) {
        HashMap<String, HashMap<String, String>> fieldTempMap = new HashMap<>(mappingContainer.getFieldNames());

        fieldTempMap.forEach((parentClass, hashMap) -> {
            hashMap.forEach((obfuscatedName, unObfuscatedName) -> { // F_0000001
                if (obfuscatedName.equals(unObfuscatedName)) {
                    mappingContainer.addFieldName(parentClass, obfuscatedName, unObfuscatedName);
                    return;
                }

                mappingContainer.addFieldName(parentClass, obfuscatedName, StringRandom.randomFieldName());
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