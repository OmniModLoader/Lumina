package idea.hierarchy;

import idea.HierarchyManager;
import idea.info.ClassInfo;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Remapper;
import org.omnimc.lumina.paser.ParsingContainer;

@Deprecated
public class HierarchyVisitor extends ClassVisitor {
    private String originalClassName;
    private ClassInfo classInfo;
    private final ParsingContainer container;
    private final HierarchyManager hierarchyManager;

    private final Remapper remapper;

    public HierarchyVisitor(ClassVisitor classVisitor, HierarchyManager hierarchyManager, ParsingContainer container) {
        super(Opcodes.ASM9, classVisitor);
        this.container = container;
        this.hierarchyManager = hierarchyManager;
        this.remapper = new CustomRemapper(container);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.originalClassName = name;
        classInfo = new ClassInfo(remapper.mapType(name));

        classInfo.addDependentClass(superName);

        if (interfaces != null) {
            for (String anInterface : interfaces) {
                classInfo.addDependentClass(anInterface);
            }
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        if ((access & Opcodes.ACC_PRIVATE) != 0) {
            classInfo.addPrivateField(name, container.getFieldName(originalClassName, name), remapper.mapDesc(descriptor));
            return super.visitField(access, name, descriptor, signature, value);
        }

        classInfo.addField(name, container.getFieldName(originalClassName, name), remapper.mapDesc(descriptor));
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if ((access & Opcodes.ACC_PRIVATE) != 0) {
            classInfo.addPrivateMethod(name, container.getMethodName(originalClassName, name, remapper.mapMethodDesc(descriptor)), remapper.mapMethodDesc(descriptor));
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }

        classInfo.addMethod(name, container.getMethodName(originalClassName, name, remapper.mapMethodDesc(descriptor)), remapper.mapMethodDesc(descriptor));
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        hierarchyManager.addClassFile(originalClassName, classInfo);
        super.visitEnd();
    }

    static class CustomRemapper extends Remapper {
        private final ParsingContainer container;

        public CustomRemapper(ParsingContainer container) {
            this.container = container;
        }

        @Override
        public String map(String internalName) {
            return mapType(internalName);
        }

        @Override
        public String mapType(String internalName) {
            return container.getClassName(internalName);
        }

        @Override
        public String mapMethodName(String owner, String name, String descriptor) {
            return container.getMethodName(owner, name, descriptor);
        }

        @Override
        public String mapFieldName(String owner, String name, String descriptor) {
            return container.getFieldName(owner, name);
        }
    }
}