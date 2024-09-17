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

package swap;

import org.omnimc.lumina.paser.MappingContainer;
import org.omnimc.lumina.writer.LuminaWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <b><a href=https://github.com/CadenCCC>Caden</a></b>
 * @since 1.0.0
 */
public class SwapMapping {
    private final MappingContainer mainMappings;
    private final MappingContainer secondaryMappings;

    public SwapMapping(MappingContainer mainMappings, MappingContainer secondaryMappings) {
        this.mainMappings = mainMappings;
        this.secondaryMappings = secondaryMappings;
    }

    public void swap(String location) throws IOException {
        MappingContainer newParsing = new MappingContainer();

        swapClasses(newParsing);
        swapMethods(newParsing);
        swapFields(newParsing);

        LuminaWriter writer = new LuminaWriter(newParsing);
        writer.writeTo(location);
        writer.flush();
        writer.close();
    }

    private void swapClasses(MappingContainer newParsing) {
        for (Map.Entry<String, String> entry : mainMappings.getClassNames().entrySet()) {
            String mainObfuscatedName = entry.getKey();
            String mainUnobfuscatedName = entry.getValue();

            for (Map.Entry<String, String> secondEntry : secondaryMappings.getClassNames().entrySet()) {
                String secondObfuscatedName = secondEntry.getKey();
                String secondUnobfuscatedName = secondEntry.getValue();

                if (secondObfuscatedName.equals(mainObfuscatedName)) {
                    newParsing.addClassName(secondUnobfuscatedName, mainUnobfuscatedName);
                }
            }
        }
    }

    private void swapMethods(MappingContainer newParsing) {
        for (Map.Entry<String, HashMap<String, String>> entry : mainMappings.getMethodNames().entrySet()) {
            String className = entry.getKey();

            for (Map.Entry<String, HashMap<String, String>> secondEntry : secondaryMappings.getMethodNames().entrySet()) {
                String secondClassName = secondEntry.getKey();

                if (!className.equals(secondClassName)) {
                    continue;
                }

                for (Map.Entry<String, String> methodEntry : entry.getValue().entrySet()) {
                    String firstObfuscatedName = methodEntry.getKey();
                    String firstUnobfuscatedName = methodEntry.getValue();

                    for (Map.Entry<String, String> secondMethodEntry : secondEntry.getValue().entrySet()) {
                        String secondObfuscatedName = secondMethodEntry.getKey();
                        String secondUnobfuscatedName = secondMethodEntry.getValue();

                        if (firstObfuscatedName.equals(secondObfuscatedName)) {
                            String[] split = firstObfuscatedName.split("\\(");
                            String descriptor = "(" + split[1];
                            newParsing.addMethodName(secondClassName, secondUnobfuscatedName, firstUnobfuscatedName + descriptor);
                        }
                    }
                }
            }
        }
    }

    private void swapFields(MappingContainer newParsing) {
        for (Map.Entry<String, HashMap<String, String>> entry : mainMappings.getFieldNames().entrySet()) {
            String className = entry.getKey();

            for (Map.Entry<String, HashMap<String, String>> secondEntry : secondaryMappings.getFieldNames().entrySet()) {
                String secondClassName = secondEntry.getKey();

                if (!className.equals(secondClassName)) {
                    continue;
                }

                for (Map.Entry<String, String> fieldEntry : entry.getValue().entrySet()) {
                    String firstObfuscatedName = fieldEntry.getKey();
                    String firstUnobfuscatedName = fieldEntry.getValue();

                    for (Map.Entry<String, String> secondMethodEntry : secondEntry.getValue().entrySet()) {
                        String secondObfuscatedName = secondMethodEntry.getKey();
                        String secondUnobfuscatedName = secondMethodEntry.getValue();

                        if (firstObfuscatedName.equals(secondObfuscatedName)) {
                            newParsing.addFieldName(secondClassName, secondUnobfuscatedName, firstUnobfuscatedName);
                        }
                    }
                }
            }
        }
    }


}