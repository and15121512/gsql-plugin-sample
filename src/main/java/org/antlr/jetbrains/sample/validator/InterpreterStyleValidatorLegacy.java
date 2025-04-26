package org.antlr.jetbrains.sample.validator;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiRecursiveElementWalkingVisitor;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.antlr.jetbrains.sample.SampleExternalAnnotator;
import org.antlr.jetbrains.sample.parser.CoreParser;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class InterpreterStyleValidatorLegacy {

    public List<SampleExternalAnnotator.Issue> validate(@NotNull PsiFile file) {
        List<SampleExternalAnnotator.Issue> issues = new ArrayList<>();

        // Wrap PSI access in read action
        ApplicationManager.getApplication().runReadAction(() -> {
            Map<String, PsiElement> definedVariables = new HashMap<>();

            file.accept(new PsiRecursiveElementWalkingVisitor() {
                @Override
                public void visitElement(@NotNull PsiElement element) {
                    if (isVariableDefinition(element)) {
                        String name = getVariableName(element);
                        definedVariables.put(name, element);
                    } else if (isVariableUsage(element)) {
                        String name = getUsedVariableName(element);
                        if (
                                !definedVariables.containsKey(name)
                                        && !predefinedVariables().contains(name)
                        ) {
                            issues.add(new SampleExternalAnnotator.Issue(
                                    "Variable or function '" + name + "' used before definition",
                                    element
                            ));
                        }
                    }
                    super.visitElement(element);
                }
            });
        });

        return issues;
    }

    private Set<String> predefinedVariables() {
        return Set.of(
                "user_name",
                "move_table_to_schema"
        );
    }

    private boolean isVariableDefinition(PsiElement element) {
        if (!(element instanceof ANTLRPsiNode)) return false;

        ASTNode node = element.getNode();
        IElementType type = node.getElementType();

        return
                (type instanceof RuleIElementType &&
                        ((RuleIElementType)type).getRuleIndex() == CoreParser.RULE_assigment_stmt);
    }

    private boolean isVariableUsage(PsiElement element) {
        if (!(element instanceof ANTLRPsiNode)) return false;

        ASTNode node = element.getNode();
        IElementType type = node.getElementType();

        return
                (
                        type instanceof RuleIElementType && (
                                ((RuleIElementType)type).getRuleIndex() == CoreParser.RULE_var
                                || ((RuleIElementType)type).getRuleIndex() == CoreParser.RULE_func
                        )
                );
    }

    private String getVariableName(PsiElement definition) {
        for (PsiElement child : definition.getChildren()) {
            if (child.getNode().getElementType().toString().equals("L_ID")) {
                return child.getText();
            }
        }
        return "UNKNOWN_VARIABLE";
    }

    private String getUsedVariableName(PsiElement call) {
        for (PsiElement child : call.getChildren()) {
            if (child.getNode().getElementType().toString().equals("L_ID")) {
                return child.getText();
            }
        }
        return "MALFORMED_USAGE";
    }
}