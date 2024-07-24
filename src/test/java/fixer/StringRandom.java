package fixer;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since
 */
public final class StringRandom {
    private static int classCounter = 0;
    private static int methodCounter = 0;
    private static int fieldCounter = 0;

    public static String randomClassName(String unObfuscatedName) {
        return unObfuscatedName + "/" + generateClassId();
    }

    public static String randomMethodName() {
        return generateMethodId();
    }

    public static String randomFieldName() {
        return generateFieldId();
    }

    private static String generateClassId() {
        String id = String.format("CLS_%06d", classCounter);
        classCounter++;
        return id;
    }

    private static String generateMethodId() {
        String id = String.format("M_%07d", methodCounter);
        methodCounter++;
        return id;
    }

    private static String generateFieldId() {
        String id = String.format("F_%07d", fieldCounter);
        fieldCounter++;
        return id;
    }
}
