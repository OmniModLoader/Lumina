package org.omnimc.lumina.paser;

/**
 * <h6>The {@code IParser} interface defines a contract for classes that implement parsing functionality.
 * <p>
 * Implementing classes will provide their own implementation of the {@linkplain IParser#run(String, MappingContainer)}
 * method to process a given line and update the {@linkplain MappingContainer} accordingly.
 *
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public interface IParser {

    /**
     * Executes the parsing operation on the provided line and updates the provided {@linkplain MappingContainer}.
     *
     * @param line      the line to be parsed.
     * @param container the {@linkplain MappingContainer} to be updated based on the parsing results.
     */
    void run(String line, MappingContainer container);
}