package idea.info;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class FieldInfo {
    private final String obfuscatedName;
    private String fieldName;
    private String descriptor;

    public FieldInfo(String obfuscatedName, String fieldName, String descriptor) {
        this.obfuscatedName = obfuscatedName;
        this.fieldName = fieldName;
        this.descriptor = descriptor;
    }

    public String getObfuscatedName() {
        return obfuscatedName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
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
        FieldInfo fieldInfo = (FieldInfo) o;
        return Objects.equals(getObfuscatedName(), fieldInfo.getObfuscatedName()) && Objects.equals(getFieldName(), fieldInfo.getFieldName()) && Objects.equals(getDescriptor(), fieldInfo.getDescriptor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getObfuscatedName(), getFieldName(), getDescriptor());
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "obfuscatedName='" + obfuscatedName + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", descriptor='" + descriptor + '\'' +
                '}';
    }
}