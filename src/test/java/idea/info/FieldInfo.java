package idea.info;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Deprecated
public class FieldInfo {
    private final String obfuscatedName;
    private String fieldName;
    private String descriptor;

    public FieldInfo(@NotNull String obfuscatedName, @NotNull String fieldName, @NotNull String descriptor) {
        this.obfuscatedName = obfuscatedName;
        this.fieldName = fieldName;
        this.descriptor = descriptor;
    }

    @NotNull
    public String getObfuscatedName() {
        return obfuscatedName;
    }

    @NotNull
    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(@NotNull String fieldName) {
        this.fieldName = fieldName;
    }

    @NotNull
    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(@NotNull String descriptor) {
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