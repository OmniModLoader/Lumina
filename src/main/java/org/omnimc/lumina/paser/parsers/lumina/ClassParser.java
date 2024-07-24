package org.omnimc.lumina.paser.parsers.lumina;

import org.omnimc.lumina.paser.IParser;
import org.omnimc.lumina.paser.ParsingContainer;

/**
 * <h6>The {@code ClassParser} class implements the {@linkplain IParser} interface to provide functionality
 * for parsing class mapping data.
 * <p>
 * This parser processes lines of class mappings and updates a
 * {@linkplain ParsingContainer} with the parsed class names.
 *
 *
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class ClassParser implements IParser {

    /**
     * Parses a single line of class mapping data and updates the provided {@linkplain ParsingContainer}.
     *
     * @param line      the line of class mapping data to be parsed.
     * @param container the {@linkplain ParsingContainer} to be updated with the parsed data.
     */
    @Override
    public void run(String line, ParsingContainer container) {
        String[] split = line.split(":");

        container.addClassName(split[0], split[1]);
    }
}
