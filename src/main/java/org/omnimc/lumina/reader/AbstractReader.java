/*
 * MIT License
 *
 * Copyright (c) 2024 OmniMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.omnimc.lumina.reader;

import org.omnimc.lumina.paser.MappingContainer;

/**
 * <h6>{@code AbstractReader} lays the groundwork for classes that read Minecraft mappings from different sources.
 *
 * <p>It defines the essential methods for fetching mapping data from URLs or file paths, and expects subclasses
 * to provide the actual implementation details.</p>
 *
 * @author <b><a href="https://github.com/CadenCCC">Caden</a></b>
 * @since 1.2.5
 */
public abstract class AbstractReader {

    /**
     * <h6>Fetches Minecraft mappings from a URL and returns a populated {@linkplain MappingContainer}.
     *
     * @param url the URL to fetch mappings from.
     * @return a {@linkplain MappingContainer} filled with the mapping data.
     * @throws Exception if there’s an issue fetching or processing the data.
     */
    public abstract MappingContainer readURL(String url) throws Exception;

    /**
     * <h6>Reads Minecraft mappings from a local directory and returns a populated {@linkplain MappingContainer}.
     *
     * @param path the path to the directory containing the mapping files.
     * @return a {@linkplain MappingContainer} filled with the mapping data.
     * @throws Exception if there’s an issue reading the files.
     */
    public abstract MappingContainer readPath(String path) throws Exception;
}