package org.antlr.jetbrains.sample.validator;

import java.util.List;
import java.util.Map;

public class PredefinedSymbols {

    public static final Map<String, VariableDescriptor> VARIABLES = Map.of(
            "user_name", new VariableDescriptor("User name variable from CTL", "custom_b2c_sql_fw")
    );

    public static final Map<String, FunctionDescriptor> FUNCTIONS = Map.of(
            "move_table_to_schema", new FunctionDescriptor(
                    "Move table function",
                    List.of("sourceTable", "targetTable") // Parameter names
            )
    );

    public static class VariableDescriptor {
        public final String description;
        public final String defaultValue;

        public VariableDescriptor(
                String description,
                String defaultValue
        ) {
            this.description = description;
            this.defaultValue = defaultValue;
        }
    }

    public static class FunctionDescriptor {
        public final String description;
        public final List<String> parameters;

        public FunctionDescriptor(
                String description,
                List<String> parameters
        ) {
            this.description = description;
            this.parameters = parameters;
        }
    }
}
