package org.antlr.jetbrains.sample.psi;

import com.intellij.lang.ASTFactory;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.antlr.jetbrains.sample.lexer.SampleElementTypes;
import org.jetbrains.annotations.NotNull;

public class VariableReferenceElement extends SamplePsiElement implements PsiNamedElement {
    public VariableReferenceElement(@NotNull ASTNode node) {
        super(node);
    }

    public String getReferenceName() {
        return getName(); // Reuse the existing getName() implementation
    }

    @Override
    public String getName() {
        String text = getText();
        if (text.startsWith("${$") && text.endsWith("}")) {
            return text.substring(3, text.length() - 1);
        } else if (text.startsWith("$")) {
            return text.substring(1);
        }
        return text;
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        Project project = getProject();
        PsiManager manager = PsiManager.getInstance(project);
        IElementType type = getNode().getElementType();

        String newText;
        String currentText = getText();

        if (currentText.startsWith("${$") && currentText.endsWith("}")) {
            newText = "${$" + name + "}";
        } else if (currentText.startsWith("$")) {
            newText = "$" + name;
        } else {
            newText = name;
        }

        // Create through parser
        PsiElement newElement = SampleElementFactory.createElement(project, type, newText);
        if (newElement == null) {
            throw new IncorrectOperationException("Failed to create renamed element");
        }

        // Replace in the tree
        return this.replace(newElement);
    }

    @Override
    public PsiReference getReference() {
        return new SampleReference(this);
    }
}

