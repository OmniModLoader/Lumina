package org.omnimc.lumina.paser.parsers.lumina;

import org.omnimc.lumina.paser.IParser;
import org.omnimc.lumina.paser.MappingContainer;

/**
 * <h6>The {@code FieldParser} class implements the {@linkplain IParser} interface to provide functionality
 * for parsing field mapping data.
 * <p>
 * This parser processes lines of field mappings and updates a {@linkplain MappingContainer} with the parsed field
 * names.
 *
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class FieldParser implements IParser {

    private String parentClass;

    /**
     * Parses a single line of field mapping data and updates the provided {@linkplain MappingContainer}.
     *
     * @param line      the line of field mapping data to be parsed.
     * @param container the {@linkplain MappingContainer} to be updated with the parsed data.
     */
    @Override
    public void run(String line, MappingContainer container) {
        if (line.contains(":")) {
            parentClass = line.replace(":", "");
            return;
        }

        if (parentClass != null) {
            String[] split = line.split(" ");
            container.addFieldName(parentClass, split[0], split[1]);
        }
    }
}