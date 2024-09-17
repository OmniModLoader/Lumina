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

package org.omnimc.lumina.paser.parsers.fabric;

import org.omnimc.lumina.paser.IParser;
import org.omnimc.lumina.paser.MappingContainer;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class FabricParser implements IParser {

    @Override
    public void run(String line, MappingContainer container) {
        String[] tokens = line.split("\t");

        switch (tokens[0]) {
            case "CLASS":
                container.addClassName(tokens[1], tokens[2]);
                break;
            case "METHOD":
                handleMethods(tokens[1], tokens[3], tokens[4], tokens[2], container);
                break;
            case "FIELD":
                handleFields(tokens[1], tokens[3], tokens[4], container);
                break;
            default:
                break;
        }
    }

    private void handleMethods(String parentClass, String obfuscatedName, String unObfuscatedName, String descriptor, MappingContainer container) {
        container.addMethodName(parentClass, obfuscatedName + descriptor, unObfuscatedName);
    }

    private void handleFields(String parentClass, String obfuscatedName, String unObfuscatedName, MappingContainer container) {
        container.addFieldName(parentClass, obfuscatedName, unObfuscatedName);
    }
}