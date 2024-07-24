package org.omnimc.lumina.paser.parsers;

import org.omnimc.lumina.paser.IParser;
import org.omnimc.lumina.paser.ParsingContainer;

/**
 * <h6>Thank you Proguard for this code :)</h6>
 *
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class ProguardParser implements IParser {
    private String parentClass;

    @Override
    public void run(String line, ParsingContainer container) {
        if (line.isEmpty()) {
            return;
        }

        String trimmedEntry = line.trim();

        if (trimmedEntry.contains("#")) {
            trimmedEntry = trimmedEntry.substring(0, trimmedEntry.indexOf("#"));
            if (trimmedEntry.isEmpty()) {
                return;
            }
        }

        /* Checking if it's a parentClass */
        if (trimmedEntry.charAt(trimmedEntry.length() - 1) == ':') {
            String currentClassName1 = processClassMapping(trimmedEntry, container);
            if (currentClassName1 == null) {
                return;
            }

            parentClass = currentClassName1;
            return;
        }

        if (parentClass != null) {
            processClassMemberMapping(parentClass, trimmedEntry, container);
        }
    }

    private void processClassMemberMapping(String parentClass, String line, ParsingContainer container) {
        int colonIndex1 = line.indexOf(':');
        int colonIndex2 = colonIndex1 < 0 ? -1 : line.indexOf(':', colonIndex1 + 1);
        int spaceIndex = line.indexOf(' ', colonIndex2 + 2);
        int argumentIndex1 = line.indexOf('(', spaceIndex + 1);
        int argumentIndex2 = argumentIndex1 < 0 ? -1 : line.indexOf(')', argumentIndex1 + 1);
        int colonIndex3 = argumentIndex2 < 0 ? -1 : line.indexOf(':', argumentIndex2 + 1);
        int colonIndex4 = colonIndex3 < 0 ? -1 : line.indexOf(':', colonIndex3 + 1);
        int arrowIndex = line.indexOf("->", (colonIndex4 >= 0 ? colonIndex4 : colonIndex3 >= 0 ? colonIndex3 : argumentIndex2 >= 0 ? argumentIndex2 : spaceIndex) + 1);

        if (spaceIndex < 0 || arrowIndex < 0) {
            return;
        }

        // Extract the elements.
        String type = line.substring(colonIndex2 + 1, spaceIndex).trim().replace(".", "/");
        String name = line.substring(spaceIndex + 1, argumentIndex1 >= 0 ? argumentIndex1 : arrowIndex).trim();
        String newName = line.substring(arrowIndex + 2).trim();

        // Does the method name contain an explicit original class name?
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex >= 0) {
            parentClass = name.substring(0, dotIndex);

            name = name.substring(dotIndex + 1);
        }

        // Process this class member mapping.
        if (!type.isEmpty() && !name.isEmpty() && !newName.isEmpty()) {
            // Is it a field or a method?
            if (argumentIndex2 < 0) {
                // Field found

                container.addFieldName(parentClass, newName, name);
            } else {
                String replace = line.substring(argumentIndex1 + 1, argumentIndex2).trim().replace(".", "/");
                String arguments = this.methodFormat(replace);
                String formatedType = this.primitiveTypes(type);

                container.addMethodName(parentClass, newName + arguments + formatedType, name);
            }
        }
    }

    private String processClassMapping(String line, ParsingContainer container) {
        int arrowIndex = line.indexOf("->");
        if (arrowIndex < 0) {
            return null;
        }

        int colonIndex = line.indexOf(':', arrowIndex + 2);
        if (colonIndex < 0) {
            return null;
        }

        // Extract the elements.
        String className = line.substring(0, arrowIndex).trim().replace(".", "/");
        String newClassName = line.substring(arrowIndex + 2, colonIndex).trim().replace(".", "/");

        container.addClassName(newClassName, className);

        return newClassName;
    }

    private String methodFormat(String arguments) {
        return "(" + method(arguments) + ")";
    }

    private String method(String arguments) {
        if (arguments.isEmpty()) {
            return arguments;
        }

        String[] split = arguments.split(",");

        StringBuilder returnBuilder = new StringBuilder();
        for (String s : split) {
            returnBuilder.append(primitiveTypes(s));
        }

        return returnBuilder.toString();
    }

    private String primitiveTypes(String input) {
        StringBuilder brackets = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (c == '[') {
                brackets.append("[");
                continue;
            }

            if (c == ']') {
                input = input.replaceFirst("\\[]", "");
            }
        }

        switch (input) { // I am so sorry JitPack wouldnt let me do it the clean way for some reason...
            case "boolean" -> {
                return brackets + "Z";
            }
            case "byte" -> {
                return brackets + "B";
            }
            case "char" -> {
                return brackets + "C";
            }
            case "double" -> {
                return brackets + "D";
            }
            case "float" -> {
                return brackets + "F";
            }
            case "int" -> {
                return brackets + "I";
            }
            case "long" -> {
                return brackets + "J";
            }
            case "void" -> {
                return "V";
            }
            default -> {
                return brackets + "L" + input + ";";
            }
        }
    }
}
