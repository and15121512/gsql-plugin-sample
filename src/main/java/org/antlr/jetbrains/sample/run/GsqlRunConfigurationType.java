package org.antlr.jetbrains.sample.run;

import com.intellij.execution.configurations.*;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GsqlRunConfigurationType implements ConfigurationType {
    @Override
    public @NotNull String getDisplayName() {
        return "GSQL Script";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "Execute GSQL scripts";
    }

    @Override
    public Icon getIcon() {
        return AllIcons.FileTypes.Custom;
    }

    @Override
    public @NotNull String getId() {
        return "GSQL_RUN_CONFIGURATION";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{new GsqlConfigurationFactory(this)};
    }

    public static class GsqlConfigurationFactory extends ConfigurationFactory {
        public GsqlConfigurationFactory(ConfigurationType type) {
            super(type);
        }

        @Override
        public @NotNull RunConfiguration createTemplateConfiguration(@NotNull Project project) {
            return new GsqlRunConfiguration(project, this, "GSQL Script");
        }

        @Override
        public @NotNull String getId() {
            return "GSQL_CONFIGURATION_FACTORY";
        }

        @Override
        public boolean isApplicable(@NotNull Project project) {
            return true;
        }
    }
}
