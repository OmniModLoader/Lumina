package org.omnimc.lumina.paser;

import java.io.Closeable;
import java.util.HashMap;

/**
 * The {@code ParsingContainer} class is an abstract class designed to hold and manage mappings of obfuscated
 * and unobfuscated names for classes, methods, parameters, and fields.
 * <p>
 * This class provides methods to add, retrieve, and clear these mappings.
 * </p>
 *
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public abstract class ParsingContainer implements Closeable {
    /**
     * A {@code HashMap} to store class names with obfuscated names as keys and unobfuscated names as values.
     */
    protected final HashMap<String, String> classNames = new HashMap<>();
    /**
     * A {@code HashMap} to store method names. The outer map uses parent class names as keys,
     * and the inner map uses obfuscated method names as keys and unobfuscated method names as values.
     */
    protected final HashMap<String, HashMap<String, String>> methodNames = new HashMap<>();
    /**
     * A {@code HashMap} to store parameter names. The outer map uses parent class names as keys,
     * and the inner map uses obfuscated parameter names as keys and arrays of unobfuscated parameter names as values.
     */
    protected final HashMap<String, HashMap<String, String[]>> parameterNames = new HashMap<>(); // todo
    /**
     * A {@code HashMap} to store field names. The outer map uses parent class names as keys,
     * and the inner map uses obfuscated field names as keys and unobfuscated field names as values.
     */
    protected final HashMap<String, HashMap<String, String>> fieldNames = new HashMap<>();

    /**
     * Adds a class name mapping to the container.
     *
     * @param obfuscatedName   the obfuscated name of the class.
     * @param unObfuscatedName the unobfuscated name of the class.
     */
    public void addClassName(String obfuscatedName, String unObfuscatedName) {
        if (classNames.containsKey(obfuscatedName)) {
            return;
        }

        classNames.put(obfuscatedName, unObfuscatedName);
    }

    /**
     * Retrieves the unobfuscated name of a class.
     *
     * @param obfuscatedName the obfuscated name of the class.
     * @return the unobfuscated name of the class, or the obfuscated name if no mapping is found.
     */
    public String getClassName(String obfuscatedName) {
        return classNames.getOrDefault(obfuscatedName, obfuscatedName);
    }

    /**
     * Retrieves all class name mappings.
     *
     * @return a {@code HashMap} containing all class name mappings.
     */
    public HashMap<String, String> getClassNames() {
        return classNames;
    }

    /**
     * Adds a method name mapping to the container.
     *
     * @param parentClass      the name of the parent class.
     * @param obfuscatedName   the obfuscated name of the method.
     * @param unObfuscatedName the unobfuscated name of the method.
     */
    public void addMethodName(final String parentClass, final String obfuscatedName, final String unObfuscatedName) {
        HashMap<String, String> tempMap = methodNames.get(parentClass);
        if (tempMap == null) {
            tempMap = new HashMap<>();
        }

        tempMap.put(obfuscatedName, unObfuscatedName);

        methodNames.put(parentClass, tempMap);
    }

    /**
     * Retrieves the unobfuscated name of a method.
     *
     * @param parentClass    the name of the parent class.
     * @param obfuscatedName the obfuscated name of the method.
     * @param descriptor     the descriptor of the method.
     * @return the unobfuscated name of the method, or the obfuscated name if no mapping is found.
     */
    public String getMethodName(final String parentClass, final String obfuscatedName, final String descriptor) {
        HashMap<String, String> tempMap = methodNames.get(parentClass);
        if (tempMap == null) {
            return obfuscatedName;
        }

        return tempMap.getOrDefault(obfuscatedName + descriptor, obfuscatedName);
    }

    /**
     * Retrieves all method name mappings.
     *
     * @return a {@code HashMap} containing all method name mappings.
     */
    public HashMap<String, HashMap<String, String>> getMethodNames() {
        return methodNames;
    }

    /**
     * Adds a field name mapping to the container.
     *
     * @param parentClass      the name of the parent class.
     * @param obfuscatedName   the obfuscated name of the field.
     * @param unObfuscatedName the unobfuscated name of the field.
     */
    public void addFieldName(final String parentClass, final String obfuscatedName, final String unObfuscatedName) {
        HashMap<String, String> tempMap = fieldNames.get(parentClass);
        if (tempMap == null) {
            tempMap = new HashMap<>();
        }

        tempMap.put(obfuscatedName, unObfuscatedName);

        fieldNames.put(parentClass, tempMap);
    }

    /**
     * Retrieves the unobfuscated name of a field.
     *
     * @param parentClass    the name of the parent class.
     * @param obfuscatedName the obfuscated name of the field.
     * @return the unobfuscated name of the field, or the obfuscated name if no mapping is found.
     */
    public String getFieldName(final String parentClass, final String obfuscatedName) {
        HashMap<String, String> tempMap = fieldNames.get(parentClass);
        if (tempMap == null) {
            return obfuscatedName;
        }

        return tempMap.getOrDefault(obfuscatedName, obfuscatedName);
    }

    /**
     * Retrieves all field name mappings.
     *
     * @return a {@code HashMap} containing all field name mappings.
     */
    public HashMap<String, HashMap<String, String>> getFieldNames() {
        return fieldNames;
    }

    /**
     * Closes the container by clearing all stored mappings.
     */
    @Override
    public void close() {
        fieldNames.clear();
        methodNames.clear();
        classNames.clear();
    }
}
