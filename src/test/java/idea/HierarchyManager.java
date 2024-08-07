package idea;

import idea.info.ClassInfo;
import idea.info.FieldInfo;
import idea.info.MethodInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class HierarchyManager {
    private final HashMap<String, ClassInfo> classFiles = new HashMap<>();

    @NotNull
    public HashMap<String, ClassInfo> getClassFiles() {
        return classFiles;
    }

    public void addClassFile(@NotNull String name, @NotNull ClassInfo file) {
        if (classFiles.containsKey(name)) {
            return;
        }

        classFiles.put(name, file);
    }

    @Nullable
    public MethodInfo getMethod(@NotNull String owner, @NotNull String obfuscatedName, @NotNull String descriptor) {
        final ClassInfo classInfo = classFiles.get(owner);
        if (classInfo == null) {
            return null;
        }

        return classInfo.getMethods().getOrDefault(obfuscatedName + descriptor, null);
    }

    @NotNull
    public String getMethodName(@NotNull String owner, @NotNull String obfuscatedName, @NotNull String descriptor) {
        final MethodInfo method = this.getMethod(owner, obfuscatedName, descriptor);
        if (method == null) {
            return obfuscatedName;
        }

        return method.getMethodName();
    }

    @Nullable
    public FieldInfo getField(@NotNull String owner, @NotNull String obfuscatedName, @NotNull String descriptor) {
        final ClassInfo classInfo = classFiles.get(owner);
        if (classInfo == null) {
            return null;
        }

        return classInfo.getFields().getOrDefault(obfuscatedName + descriptor, null);
    }

    @NotNull
    public String getFieldName(@NotNull String owner, @NotNull String obfuscatedName, @NotNull String descriptor) {
        final FieldInfo field = this.getField(owner, obfuscatedName, descriptor);
        if (field == null) {
            return obfuscatedName;
        }

        return field.getFieldName();
    }

    public void populateClassFiles() {
        final HashMap<String, ClassInfo> classFileHashMap = new HashMap<>();

        for (Map.Entry<String, ClassInfo> entry : classFiles.entrySet()) {
            String className = entry.getKey();
            ClassInfo originalClassFile = entry.getValue();

            ArrayList<String> dependencies = new ArrayList<>(originalClassFile.getDependentClasses());
            while (!dependencies.isEmpty()) {
                ArrayList<String> nextDependencies = new ArrayList<>();
                for (String dependency : dependencies) {
                    ClassInfo file = classFiles.get(dependency);
                    if (file != null) {
                        originalClassFile.getFields().putAll(file.getFields());
                        originalClassFile.getMethods().putAll(file.getMethods());
                        nextDependencies.addAll(file.getDependentClasses());
                    }
                }
                dependencies = nextDependencies;
            }


            classFileHashMap.put(className, originalClassFile);
        }

        classFiles.clear();
        classFiles.putAll(classFileHashMap);
    }

    @Deprecated
    private ClassInfo lookup(ClassInfo originalClassFile, ArrayList<String> dependentClasses) {
        ClassInfo tempClassFile = originalClassFile;
        ArrayList<String> listOfPossibleLookups = new ArrayList<>();

        for (String dependentClass : dependentClasses) {
            ClassInfo file = classFiles.get(dependentClass);
            if (file == null) {
                continue;
            }

            listOfPossibleLookups.addAll(file.getDependentClasses());

            tempClassFile.getFields().putAll(file.getFields());
            tempClassFile.getMethods().putAll(file.getMethods());
        }

        if (!listOfPossibleLookups.isEmpty()) {
            tempClassFile = lookup(tempClassFile, listOfPossibleLookups);
        }

        return tempClassFile;
    }
}