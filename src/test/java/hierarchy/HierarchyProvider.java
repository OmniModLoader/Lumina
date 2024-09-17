/*
 * MIT License
 *
 * Copyright (c) 2024 OmniMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package hierarchy;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.omnimc.asm.changes.IClassChange;
import org.omnimc.asm.file.ClassFile;
import org.omnimc.asm.manager.thread.SafeClassManager;
import org.omnimc.lumina.paser.MappingContainer;
import org.omnimc.lumina.reader.LuminaReader;
import org.omnimc.lumina.writer.LuminaWriter;
import org.omnimc.trix.hierarchy.HierarchyManager;
import org.omnimc.trix.hierarchy.info.ClassInfo;
import org.omnimc.trix.hierarchy.info.FieldInfo;
import org.omnimc.trix.hierarchy.info.MethodInfo;
import org.omnimc.trix.visitors.hierarchy.HierarchyClassVisitor;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class HierarchyProvider {
    private final String minecraftJar;
    private final String mappingLocation;

    private HierarchyManager hierarchyManager;

    public HierarchyProvider(String minecraftJar, String mappingLocation) {
        this.minecraftJar = minecraftJar;
        this.mappingLocation = mappingLocation;
    }

    public void init() throws IOException {
        MappingContainer reader = getReader(mappingLocation);
        HierarchyManager hierarchyManager = new HierarchyManager();
        System.out.println(reader.getClassNames().size());

        SafeClassManager classManager = new SafeClassManager();
        classManager.readJarFile(new File(minecraftJar));
        classManager.applyChanges(((IClassChange) (name, classBytes) -> {
            ClassReader classReader = new ClassReader(classBytes);
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            //classReader.accept(new HierarchyClassVisitor(writer, hierarchyManager, reader), ClassReader.EXPAND_FRAMES);

            return new ClassFile(name, classBytes);
        }));
        classManager.close();
        hierarchyManager.populateClassFiles();
        this.hierarchyManager = hierarchyManager;
        System.out.println(hierarchyManager.getClassFiles().size());

    }

    public void write() throws IOException {
        MappingContainer container = new MappingContainer();

        populateClassNames(hierarchyManager, container);
        populateMethodNames(hierarchyManager, container);
        populateFieldNames(hierarchyManager, container);

        LuminaWriter writer = new LuminaWriter(container);
        writer.writeTo(mappingLocation + "\\hierarchy");
        writer.flush();
        writer.close();
    }

    private void populateMethodNames(HierarchyManager hierarchyManager, MappingContainer container) {
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

    private void populateFieldNames(HierarchyManager hierarchyManager, MappingContainer container) {
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

    private void populateClassNames(HierarchyManager hierarchyManager, MappingContainer container) {
        for (Map.Entry<String, ClassInfo> entry : hierarchyManager.getClassFiles().entrySet()) {
            String obfuscatedName = entry.getKey();
            ClassInfo classInfo = entry.getValue();

            container.addClassName(obfuscatedName, classInfo.getClassName());
        }
    }


    private MappingContainer getReader(String path) throws IOException {
        LuminaReader reader = new LuminaReader();
        return reader.readPath(path);
    }
}