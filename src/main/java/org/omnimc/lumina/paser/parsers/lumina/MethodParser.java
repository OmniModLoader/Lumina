package org.omnimc.lumina.paser.parsers.lumina;

import org.omnimc.lumina.paser.IParser;
import org.omnimc.lumina.paser.ParsingContainer;

/**
 * <h6>The {@code MethodParser} class implements the {@linkplain IParser} interface to provide functionality
 * for parsing method mapping data.
 * <p>
 * This parser processes lines of method mappings and updates a {@linkplain ParsingContainer} with the parsed method
 * names.
 *
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class MethodParser implements IParser {

    private String parentClass;

    /**
     * Parses a single line of method mapping data and updates the provided {@linkplain ParsingContainer}.
     *
     * @param line      the line of method mapping data to be parsed.
     * @param container the {@linkplain ParsingContainer} to be updated with the parsed data.
     */
    @Override
    public void run(String line, ParsingContainer container) {
        if (line.contains(":")) {
            parentClass = line.replace(":", "");
            return;
        }

        if (parentClass != null) {
            String[] split = line.split(" "); // 0 is the obfuscated name. 1 is the unObfuscatedName
            container.addMethodName(parentClass, split[0], split[1]);
        }
    }
}