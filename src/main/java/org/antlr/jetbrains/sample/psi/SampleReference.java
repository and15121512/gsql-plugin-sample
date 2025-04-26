package org.antlr.jetbrains.sample.psi;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.ThrowableComputable;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SampleReference extends PsiReferenceBase<PsiElement>
        implements PsiPolyVariantReference {

    public SampleReference(@NotNull PsiElement element) {
        super(element, getProperTextRange(element));
    }

    private static TextRange getProperTextRange(PsiElement element) {
        if (element instanceof VariableReferenceElement) {
            String text = element.getText();
            if (text.startsWith("${$") && text.endsWith("}")) {
                return TextRange.from(3, text.length() - 4); // ${$var} -> var
            }
            return TextRange.from(1, text.length() - 1); // $var -> var
        }
        return TextRange.allOf(element.getText());
    }

    @Override
    public @Nullable PsiElement resolve() {
        ResolveResult[] results = multiResolve(false);
        return results.length == 1 ? results[0].getElement() : null;
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        List<ResolveResult> results = new ArrayList<>();
        String name = getNameToResolve();
        PsiFile file = getElement().getContainingFile();

        if (name == null || name.isEmpty()) {
            return ResolveResult.EMPTY_ARRAY;
        }

        // Find all matching declarations
        PsiTreeUtil.processElements(file, AssignmentElement.class, element -> {
            if (name.equals(element.getName())) {
                PsiElement nameId = element.getNameIdentifier();
                if (nameId != null) {
                    results.add(new PsiElementResolveResult(nameId));
                }
            }
            return true;
        });

        return results.toArray(new ResolveResult[0]);
    }

    private @Nullable String getNameToResolve() {
        if (getElement() instanceof VariableReferenceElement) {
            return ((VariableReferenceElement)getElement()).getReferenceName();
        }
        if (getElement() instanceof IdentifierPsiElement) {
            return ((IdentifierPsiElement)getElement()).getName();
        }
        return null;
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        String refName = getNameToResolve();
        if (refName == null) return false;

        if (element instanceof IdentifierPsiElement) {
            return refName.equals(((IdentifierPsiElement)element).getName());
        }
        if (element instanceof AssignmentElement) {
            return refName.equals(((AssignmentElement)element).getName());
        }
        return false;
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newName) throws IncorrectOperationException {
        PsiElement element = getElement();
        if (!element.isValid()) {
            throw new IncorrectOperationException("Element is not valid");
        }

        Project project = element.getProject();

        return WriteCommandAction.runWriteCommandAction(project, (ThrowableComputable<PsiElement, RuntimeException>) () -> {
            PsiElement newElement = createRenamedElement(element, newName);
            return element.replace(newElement);
        });
    }

    private @NotNull PsiElement createRenamedElement(@NotNull PsiElement element,
                                                     @NotNull String newName) {
        Project project = element.getProject();
        IElementType type = element.getNode().getElementType();
        String currentText = element.getText();
        String newText;

        if (element instanceof VariableReferenceElement) {
            if (currentText.startsWith("${$") && currentText.endsWith("}")) {
                newText = "${$" + newName + "}";
            } else if (currentText.startsWith("$")) {
                newText = "$" + newName;
            } else {
                newText = newName;
            }
        } else {
            newText = newName;
        }

        PsiElement newElement = SampleElementFactory.createElement(project, type, newText);
        if (newElement == null) {
            throw new IncorrectOperationException("Failed to create element for: " + newText);
        }
        return newElement;
    }
}
