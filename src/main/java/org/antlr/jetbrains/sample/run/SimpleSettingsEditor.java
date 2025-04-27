package org.antlr.jetbrains.sample.run;

import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SimpleSettingsEditor extends SettingsEditor<GsqlRunConfiguration> {
    @Override
    protected void resetEditorFrom(@NotNull GsqlRunConfiguration config) {}

    @Override
    protected void applyEditorTo(@NotNull GsqlRunConfiguration config) {}

    @Override
    protected @NotNull JComponent createEditor() {
        return new JLabel("<html>No configuration needed.<br>Will execute the currently opened GSQL file.</html>");
    }
}
