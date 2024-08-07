package idea.info;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Deprecated
public class ClassInfo {
    private final String className;

    private final ArrayList<String> dependentClasses = new ArrayList<>();

    private final HashMap<String, FieldInfo> fields = new HashMap<>();
    private final HashMap<String, FieldInfo> privateFields = new HashMap<>();
    private final HashMap<String, MethodInfo> methods = new HashMap<>();
    private final HashMap<String, MethodInfo> privateMethods = new HashMap<>();

    public ClassInfo(@NotNull String className) {
        this.className = className;
    }

    @NotNull
    public String getClassName() {
        return className;
    }

    @NotNull
    public ArrayList<String> getDependentClasses() {
        return dependentClasses;
    }

    public void addDependentClass(@NotNull String className) {
        if (dependentClasses.contains(className)) {
            return;
        }

        dependentClasses.add(className);
    }

    @NotNull
    public HashMap<String, MethodInfo> getMethods() {
        return methods;
    }

    public void addMethod(@NotNull String obfuscatedName, @NotNull String unObfuscatedName, @NotNull String descriptor) {
        if (methods.containsKey(obfuscatedName)) {
            return;
        }

        methods.put(obfuscatedName + descriptor, new MethodInfo(obfuscatedName, unObfuscatedName, descriptor));
    }

    @NotNull
    public HashMap<String, FieldInfo> getFields() {
        return fields;
    }

    public void addField(@NotNull String obfuscatedName, @NotNull String unObfuscatedName, @NotNull String descriptor) {
        if (fields.containsKey(obfuscatedName)) {
            return;
        }

        fields.put(obfuscatedName + descriptor, new FieldInfo(obfuscatedName, unObfuscatedName, descriptor));
    }

    @NotNull
    public HashMap<String, FieldInfo> getPrivateFields() {
        return privateFields;
    }

    public void addPrivateField(@NotNull String obfuscatedName, @NotNull String unObfuscatedName, @NotNull String descriptor) {
        if (privateFields.containsKey(obfuscatedName)) {
            return;
        }

        privateFields.put(obfuscatedName + descriptor, new FieldInfo(obfuscatedName, unObfuscatedName, descriptor));
    }

    @NotNull
    public HashMap<String, MethodInfo> getPrivateMethods() {
        return privateMethods;
    }

    public void addPrivateMethod(@NotNull String obfuscatedName, @NotNull String unObfuscatedName, @NotNull String descriptor) {
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