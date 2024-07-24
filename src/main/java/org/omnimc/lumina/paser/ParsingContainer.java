package org.omnimc.lumina.paser;

import java.io.Closeable;
import java.util.HashMap;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public abstract class ParsingContainer implements Closeable {
    protected final HashMap<String, String> classNames = new HashMap<>();
    protected final HashMap<String, HashMap<String, String>> methodNames = new HashMap<>();
    protected final HashMap<String, HashMap<String, String>> fieldNames = new HashMap<>();

    public void addClassName(String obfuscatedName, String unObfuscatedName) {
        if (classNames.containsKey(obfuscatedName)) {
            return;
        }

        classNames.put(obfuscatedName, unObfuscatedName);
    }

    public String getClassName(String obfuscatedName) {
        return classNames.getOrDefault(obfuscatedName, obfuscatedName);
    }

    public HashMap<String, String> getClassNames() {
        return classNames;
    }

    public void addMethodName(final String parentClass, final String obfuscatedName, final String unObfuscatedName) {
        HashMap<String, String> tempMap = methodNames.get(parentClass);
        if (tempMap == null) {
            tempMap = new HashMap<>();
        }

        tempMap.put(obfuscatedName, unObfuscatedName);

        methodNames.put(parentClass, tempMap);
    }

    public String getMethodName(final String parentClass, final String obfuscatedName, final String descriptor) {
        HashMap<String, String> tempMap = methodNames.get(parentClass);
        if (tempMap == null) {
            return obfuscatedName;
        }

        return tempMap.getOrDefault(obfuscatedName + descriptor, obfuscatedName);
    }

    public HashMap<String, HashMap<String, String>> getMethodNames() {
        return methodNames;
    }

    public void addFieldName(final String parentClass, final String obfuscatedName, final String unObfuscatedName) {
        HashMap<String, String> tempMap = fieldNames.get(parentClass);
        if (tempMap == null) {
            tempMap = new HashMap<>();
        }

        tempMap.put(obfuscatedName, unObfuscatedName);

        fieldNames.put(parentClass, tempMap);
    }

    public String getFieldName(final String parentClass, final String obfuscatedName) {
        HashMap<String, String> tempMap = fieldNames.get(parentClass);
        if (tempMap == null) {
            return obfuscatedName;
        }

        return tempMap.getOrDefault(obfuscatedName, obfuscatedName);
    }

    public HashMap<String, HashMap<String, String>> getFieldNames() {
        return fieldNames;
    }

    @Override
    public void close() {
        fieldNames.clear();
        methodNames.clear();
        classNames.clear();
    }
}
