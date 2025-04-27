package org.antlr.jetbrains.sample.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GsqlRunConfiguration extends LocatableConfigurationBase implements RunnerSettings {

    protected GsqlRunConfiguration(Project project, ConfigurationFactory factory, String name) {
        super(project, factory, name);
    }

    @Override
    public @NotNull SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new SimpleSettingsEditor();
    }

    @Override
    public @Nullable RunProfileState getState(@NotNull Executor executor,
                                              @NotNull ExecutionEnvironment env) {
        return new GsqlCommandLineState(env);
    }

    @Override
    public @Nullable String suggestedName() {
        VirtualFile file = getScriptFile(); // Implement this to get current file
        return file != null ? file.getName() : "GSQL Script";
    }

    @Override
    public @NotNull RunConfiguration clone() {
        return new GsqlRunConfiguration(getProject(), getFactory(), getName());
    }

    private @Nullable VirtualFile getScriptFile() {
        FileEditorManager manager = FileEditorManager.getInstance(getProject());
        VirtualFile[] files = manager.getSelectedFiles();
        for (VirtualFile file : files) {
            if ("gsql".equalsIgnoreCase(file.getExtension())) {
                return file;
            }
        }
        return null;
    }

    public String getScriptContent() throws ExecutionException {
        VirtualFile file = getScriptFile(); // Implement your file access logic
        Document doc = FileDocumentManager.getInstance().getDocument(file);
        if (doc == null) throw new ExecutionException("Could not read file");
        return doc.getText();
    }
}
