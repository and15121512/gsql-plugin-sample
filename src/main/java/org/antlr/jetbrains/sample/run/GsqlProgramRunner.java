package org.antlr.jetbrains.sample.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionManager;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.GenericProgramRunner;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.execution.ui.ExecutionConsole;
import com.intellij.execution.ui.RunContentDescriptor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GsqlProgramRunner extends GenericProgramRunner<GsqlRunConfiguration> {
    @Override
    public @NotNull String getRunnerId() {
        return "GsqlProgramRunner";
    }

    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        return DefaultRunExecutor.EXECUTOR_ID.equals(executorId) &&
                profile instanceof GsqlRunConfiguration;
    }

    @Override
    protected RunContentDescriptor doExecute(@NotNull RunProfileState state,
                                             @NotNull ExecutionEnvironment env) throws ExecutionException {
        // Get configuration and script content
        GsqlRunConfiguration config = (GsqlRunConfiguration)env.getRunProfile();
        String script = config.getScriptContent();

        // Create console and get both components
        TextConsoleBuilder builder = TextConsoleBuilderFactory.getInstance()
                .createBuilder(env.getProject());
        ConsoleView consoleView = builder.getConsole();
        JComponent component = consoleView.getComponent();

        // Print script to console
        consoleView.print("=== GSQL SCRIPT ===\n", ConsoleViewContentType.NORMAL_OUTPUT);
        consoleView.print(script + "\n", ConsoleViewContentType.NORMAL_OUTPUT);
        consoleView.print("=== EXECUTION COMPLETE ===\n", ConsoleViewContentType.NORMAL_OUTPUT);

        // Create descriptor with proper types
        return new RunContentDescriptor(
                consoleView,          // ExecutionConsole
                new GsqlAutoTerminatingProcessHandler(),
                component,           // JComponent
                config.getName()
        );
    }
}
