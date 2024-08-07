import idea.HierarchyManager;
import idea.hierarchy.HierarchyVisitor;
import idea.info.ClassInfo;
import idea.info.FieldInfo;
import idea.info.MethodInfo;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.omnimc.asm.changes.IClassChange;
import org.omnimc.asm.file.ClassFile;
import org.omnimc.asm.manager.thread.SafeClassManager;
import org.omnimc.lumina.paser.ParsingContainer;
import org.omnimc.lumina.paser.parsers.ProguardParser;
import org.omnimc.lumina.reader.LuminaReader;
import org.omnimc.lumina.writer.LuminaWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@SuppressWarnings("NonFinalUtilityClass")
public class TestMain {
    private static final String MINECRAFT_JAR = "C:\\Users\\CryroByte\\AppData\\Roaming\\.minecraft\\versions\\1.21\\1.21.jar";

    public static void main(String[] args) throws IOException, InterruptedException {
        final String dir = System.getProperty("user.dir") + "\\run";
        Path path;
        if (!Files.exists(path = Path.of(dir))) {
            Files.createDirectory(path);
        }
        System.out.println("output dir = " + dir);

        //createMappings(dir);

        //createHierarchyMapping(dir);

        ParsingContainer reader = getReader(dir + "\\hierarchy");

    }

    private static void createHierarchyMapping(String dir) throws IOException {
        ParsingContainer reader = getReader(dir);
        HierarchyManager hierarchyManager = new HierarchyManager();
        System.out.println(reader.getClassNames().size());

        SafeClassManager classManager = new SafeClassManager();
        classManager.readJarFile(new File(MINECRAFT_JAR));
        classManager.applyChanges(((IClassChange) (name, classBytes) -> {
            ClassReader classReader = new ClassReader(classBytes);
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classReader.accept(new HierarchyVisitor(writer, hierarchyManager, reader), ClassReader.EXPAND_FRAMES);

            return new ClassFile(name, classBytes);
        }));
        classManager.close();
        hierarchyManager.populateClassFiles();
        System.out.println(hierarchyManager.getClassFiles().size());

        ParsingContainer container = new ParsingContainer() {};

        populateClassNames(hierarchyManager, container);
        populateMethodNames(hierarchyManager, container);
        populateFieldNames(hierarchyManager, container);

        LuminaWriter writer = new LuminaWriter(container);
        writer.writeTo(dir + "\\hierarchy");
        writer.flush();
        writer.close();
    }

    private static void populateMethodNames(HierarchyManager hierarchyManager, ParsingContainer container) {
        for (Map.Entry<String, ClassInfo> entry : hierarchyManager.getClassFiles().entrySet()) {
            String obfuscatedClassName = entry.getKey();
            ClassInfo classInfo = entry.getValue();

            for (Map.Entry<String, MethodInfo> methodEntry : classInfo.getMethods().entrySet()) {
                MethodInfo methodInfo = methodEntry.getValue();

                container.addMethodName(obfuscatedClassName, methodInfo.getObfuscatedName() + methodInfo.getDescriptor(), methodInfo.getMethodName());
            }

            for (Map.Entry<String, MethodInfo> privateMethodEntry : classInfo.getPrivateMethods().entrySet()) {
                MethodInfo methodInfo = privateMethodEntry.getValue();

                container.addMethodName(obfuscatedClassName, methodInfo.getObfuscatedName() + methodInfo.getDescriptor(), methodInfo.getMethodName());
            }
        }
    }

    private static void populateFieldNames(HierarchyManager hierarchyManager, ParsingContainer container) {
        for (Map.Entry<String, ClassInfo> entry : hierarchyManager.getClassFiles().entrySet()) {
            String obfuscatedClassName = entry.getKey();
            ClassInfo classInfo = entry.getValue();

            for (Map.Entry<String, FieldInfo> fieldEntry : classInfo.getFields().entrySet()) {
                FieldInfo fieldInfo = fieldEntry.getValue();

                container.addFieldName(obfuscatedClassName, fieldInfo.getObfuscatedName(), fieldInfo.getFieldName());
            }

            for (Map.Entry<String, FieldInfo> privateFieldEntry : classInfo.getPrivateFields().entrySet()) {
                FieldInfo fieldInfo = privateFieldEntry.getValue();

                container.addFieldName(obfuscatedClassName, fieldInfo.getObfuscatedName(), fieldInfo.getFieldName());
            }
        }
    }

    private static void populateClassNames(HierarchyManager hierarchyManager, ParsingContainer container) {
        for (Map.Entry<String, ClassInfo> entry : hierarchyManager.getClassFiles().entrySet()) {
            String obfuscatedName = entry.getKey();
            ClassInfo classInfo = entry.getValue();

            container.addClassName(obfuscatedName, classInfo.getClassName());
        }
    }

    private static ParsingContainer getReader(String path) throws IOException {
        LuminaReader reader = new LuminaReader();
        return reader.readPath(path);
    }


    private static void createMappings(String path) throws IOException, InterruptedException {
        LuminaWriter writer = new LuminaWriter("https://piston-data.mojang.com/v1/objects/0530a206839eb1e9b35ec86acbbe394b07a2d9fb/client.txt", new ProguardParser());
        writer.writeTo(path);
        writer.flush();
        writer.close();
    }
}