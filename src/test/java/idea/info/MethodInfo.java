package idea.info;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class MethodInfo {
    private final String obfuscatedName;
    private String methodName;
    private String descriptor;

    public MethodInfo(String obfuscatedName, String methodName, String descriptor) {
        this.obfuscatedName = obfuscatedName;
        this.methodName = methodName;
        this.descriptor = descriptor;
    }

    public String getObfuscatedName() {
        return obfuscatedName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodInfo that = (MethodInfo) o;
        return Objects.equals(getObfuscatedName(), that.getObfuscatedName()) && Objects.equals(getMethodName(), that.getMethodName()) && Objects.equals(getDescriptor(), that.getDescriptor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getObfuscatedName(), getMethodName(), getDescriptor());
    }

    @Override
    public String toString() {
        return "MethodInfo{" +
                "obfuscatedName='" + obfuscatedName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", descriptor='" + descriptor + '\'' +
                '}';
    }
}