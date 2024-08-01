package idea.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ClassInfo {
    private final String className;

    private final ArrayList<String> dependentClasses = new ArrayList<>();

    private final HashMap<String, FieldInfo> fields = new HashMap<>();
    private final HashMap<String, FieldInfo> privateFields = new HashMap<>();
    private final HashMap<String, MethodInfo> methods = new HashMap<>();
    private final HashMap<String, MethodInfo> privateMethods = new HashMap<>();

    public ClassInfo(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public ArrayList<String> getDependentClasses() {
        return dependentClasses;
    }

    public void addDependentClass(String className) {
        if (dependentClasses.contains(className)) {
            return;
        }

        dependentClasses.add(className);
    }

    public HashMap<String, MethodInfo> getMethods() {
        return methods;
    }

    public void addMethod(String obfuscatedName, String unObfuscatedName, String descriptor) {
        if (methods.containsKey(obfuscatedName)) {
            return;
        }

        methods.put(obfuscatedName + descriptor, new MethodInfo(obfuscatedName, unObfuscatedName, descriptor));
    }

    public HashMap<String, FieldInfo> getFields() {
        return fields;
    }

    public void addField(String obfuscatedName, String unObfuscatedName, String descriptor) {
        if (fields.containsKey(obfuscatedName)) {
            return;
        }

        fields.put(obfuscatedName + descriptor, new FieldInfo(obfuscatedName, unObfuscatedName, descriptor));
    }

    public HashMap<String, FieldInfo> getPrivateFields() {
        return privateFields;
    }

    public void addPrivateField(String obfuscatedName, String unObfuscatedName, String descriptor) {
        if (privateFields.containsKey(obfuscatedName)) {
            return;
        }

        privateFields.put(obfuscatedName + descriptor, new FieldInfo(obfuscatedName, unObfuscatedName, descriptor));
    }

    public HashMap<String, MethodInfo> getPrivateMethods() {
        return privateMethods;
    }

    public void addPrivateMethod(String obfuscatedName, String unObfuscatedName, String descriptor) {
        if (privateMethods.containsKey(obfuscatedName)) {
            return;
        }

        privateMethods.put(obfuscatedName + descriptor, new MethodInfo(obfuscatedName, unObfuscatedName, descriptor));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassInfo classInfo = (ClassInfo) o;
        return Objects.equals(getClassName(), classInfo.getClassName()) && Objects.equals(getDependentClasses(), classInfo.getDependentClasses()) && Objects.equals(getFields(), classInfo.getFields()) && Objects.equals(getMethods(), classInfo.getMethods());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClassName(), getDependentClasses(), getFields(), getMethods());
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
                "className='" + className + '\'' +
                ", dependentClasses=" + dependentClasses +
                ", fields=" + fields +
                ", methods=" + methods +
                '}';
    }
}