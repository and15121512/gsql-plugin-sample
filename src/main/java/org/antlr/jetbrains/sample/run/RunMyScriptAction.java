package org.antlr.jetbrains.sample.run;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ExecutionEnvironmentBuilder;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProgressIndicatorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RunMyScriptAction extends AnAction {
    @Override
    public void update(@NotNull AnActionEvent e) {
        VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);
        boolean enabled = file != null && "gsql".equals(file.getExtension());
        e.getPresentation().setEnabledAndVisible(enabled);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (project == null || file == null) return;

        // Get or create configuration
        RunManager runManager = RunManager.getInstance(project);
        RunnerAndConfigurationSettings settings = runManager.findConfigurationByName(file.getName());
        if (settings == null) {
            ConfigurationType type = ConfigurationTypeUtil.findConfigurationType(GsqlRunConfigurationType.class);
            settings = runManager.createConfiguration(file.getName(), type.getConfigurationFactories()[0]);
            runManager.addConfiguration(settings);
        }

        // Get the configuration instance
        RunProfile runProfile = settings.getConfiguration();

        // Find runner
        ProgramRunner<?> runner = ProgramRunner.PROGRAM_RUNNER_EP.findExtension(GsqlProgramRunner.class);
        if (runner == null) {
            Messages.showErrorDialog(project, "GSQL runner not registered", "Execution Error");
            return;
        }

        // Execute configuration
        try {
            // Build execution environment correctly
            ExecutionEnvironment env = ExecutionEnvironmentBuilder
                    .create(project, DefaultRunExecutor.getRunExecutorInstance(), runProfile)
                    .build();
            runner.execute(env);
        } catch (ExecutionException ex) {
            Messages.showErrorDialog(project,
                    "Failed to run script: " + ex.getMessage(),
                    "Execution Error");
        }
    }
}
