package org.antlr.jetbrains.sample.validator;

import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.light.LightElement;
import org.antlr.jetbrains.sample.SampleLanguage;

public class PredefinedSymbolElement extends LightElement {
    private final String name;
    private final String defaultValue;

    public PredefinedSymbolElement(PsiManager manager, String name, String defaultValue) {
        super(manager, SampleLanguage.INSTANCE);
        this.name = name;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String toString() {
        return "Predefined:" + name;
    }
}
