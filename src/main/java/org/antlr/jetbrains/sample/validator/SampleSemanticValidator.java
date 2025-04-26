package org.antlr.jetbrains.sample.validator;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.antlr.jetbrains.sample.SampleExternalAnnotator;
import org.antlr.jetbrains.sample.psi.*;

import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SampleSemanticValidator {
    private final Map<String, AssignmentElement> definedSymbols = new HashMap<>();
    private final List<SampleExternalAnnotator.Issue> issues = new ArrayList<>();
    private final Set<String> predefinedVars = Set.of("user_name");
    private final Set<String> predefinedFuncs = Set.of("move_table_to_schema");

    public List<SampleExternalAnnotator.Issue> validate(@NotNull PsiFile file) {
        ApplicationManager.getApplication().runReadAction(() -> {
            // Single pass with collection and validation
            PsiTreeUtil.processElements(file, element -> {
                if (element instanceof AssignmentElement) {
                    processAssignment((AssignmentElement)element);
                }
                else if (element instanceof FunctionCallElement) {
                    validateFunctionCall((FunctionCallElement)element);
                }
                else if (element instanceof VariableReferenceElement) {
                    validateVariable((VariableReferenceElement)element);
                }
                return true;
            });
        });
        return issues;
    }

    private void processAssignment(AssignmentElement assignment) {
        IdentifierPsiElement nameElement = assignment.getNameIdentifier();
        if (nameElement != null) {
            definedSymbols.put(nameElement.getText(), assignment);

            // Validate lambda parameters if function
            if (assignment.isFunction()) {
                validateLambdaParameters(assignment);
            }
        }
    }

    private void validateLambdaParameters(AssignmentElement functionAssignment) {
        LambdaExpressionElement lambda = functionAssignment.getLambdaValue();
        if (lambda != null) {
            lambda.getParameters().forEach(param -> {
                if (predefinedVars.contains(param.getText())) {
                    issues.add(new SampleExternalAnnotator.Issue(
                            "Parameter shadows predefined variable: " + param.getText(),
                            param
                    ));
                }
            });
        }
    }

    private void validateVariable(VariableReferenceElement ref) {
        String name = ref.getReferenceName();
        if (name != null) {
            if (!isSymbolDefined(name)) {
                issues.add(createError(ref, "Undefined variable: " + name));
            }
            else if (isFunctionSymbol(name)) {
                issues.add(createError(ref, name + " is a function, not a variable"));
            }
        }
    }

    private void validateFunctionCall(FunctionCallElement call) {
        String name = call.getCalledName().getText();
        if (name != null) {
            if (predefinedVars.contains(name)) {
                issues.add(createError(call, name + " is a variable, not a function"));
            }
            else if (!isFunctionSymbol(name)) {
                issues.add(createError(call, "Undefined function: " + name));
            }
        }

        // Validate arguments (existing logic)
        for (ArgElement arg : call.getArguments()) {
            if (arg.isKwarg() && !validKwarg(arg.getNameIdentifier().getText())) {
                issues.add(createError(arg, "Invalid keyword argument"));
            }
            validateExpression(arg.getValueExpression());
        }
    }

    private boolean validKwarg(String argName) {
        return true; // TODO: ADD PARSING THIS THING CORRECTLY !!!
    }

    private void validateExpression(ExprElement expr) {
        // TODO: ADD PARSING THIS THING CORRECTLY !!!
    }

    private boolean isFunctionSymbol(String name) {
        if (predefinedFuncs.contains(name)) return true;
        AssignmentElement declaration = definedSymbols.get(name);
        return declaration != null && declaration.isFunction();
    }

    private boolean isSymbolDefined(String name) {
        return predefinedVars.contains(name) || predefinedFuncs.contains(name) || definedSymbols.containsKey(name);
    }

    private SampleExternalAnnotator.Issue createError(PsiElement element, String message) {
        return new SampleExternalAnnotator.Issue(message, element);
    }
}
