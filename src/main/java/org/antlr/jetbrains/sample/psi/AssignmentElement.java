package org.antlr.jetbrains.sample.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.jetbrains.sample.lexer.SampleElementTypes;
import org.antlr.jetbrains.sample.parser.CoreParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AssignmentElement extends SamplePsiElement implements PsiNamedElement {
    public AssignmentElement(@NotNull ASTNode node) { super(node); }

    public @Nullable IdentifierPsiElement getNameIdentifier() {
        ASTNode idNode = (ASTNode) findChildByType(SampleElementTypes.L_ID());
        return idNode != null ? (IdentifierPsiElement) idNode.getPsi() : null;
    }

    public boolean isFunction() {
        PsiElement value = getValue();
        if (value != null) {
            // Check all children recursively for lambda
            return hasLambdaChild(value);
        }
        return false;
    }

    private boolean hasLambdaChild(PsiElement element) {
        for (PsiElement child : element.getChildren()) {
            if (child instanceof LambdaExpressionElement) {
                return true;
            }
            if (child.getNode().getElementType() == SampleElementTypes.T_LAMBDA()) {
                return true;
            }
            if (hasLambdaChild(child)) { // Recursive check
                return true;
            }
        }
        return false;
    }

    public @Nullable PsiElement getValue() {
        ASTNode eqNode = (ASTNode) findChildByType(SampleElementTypes.T_EQUAL());
        if (eqNode != null) {
            ASTNode nextNode = eqNode.getTreeNext();
            while (nextNode != null) {
                IElementType type = nextNode.getElementType();
                if (type instanceof RuleIElementType &&
                        ((RuleIElementType)type).getRuleIndex() == CoreParser.RULE_expr) {
                    return nextNode.getPsi();
                }
                nextNode = nextNode.getTreeNext();
            }
        }
        return null;
    }

    public @Nullable LambdaExpressionElement getLambdaValue() {
        PsiElement value = getValue();
        if (value != null) {
            return findLambdaInChildren(value);
        }
        return null;
    }

    private @Nullable LambdaExpressionElement findLambdaInChildren(PsiElement element) {
        for (PsiElement child : element.getChildren()) {
            if (child instanceof LambdaExpressionElement) {
                return (LambdaExpressionElement) child;
            }
            LambdaExpressionElement lambda = findLambdaInChildren(child);
            if (lambda != null) {
                return lambda;
            }
        }
        return null;
    }

    @Override
    public PsiReference getReference() {
        PsiElement nameId = getNameIdentifier();
        return nameId != null ? nameId.getReference() : null;
    }

    @Override
    public String getName() {
        PsiElement id = getNameIdentifier();
        return id != null ? ((PsiNamedElement)id).getName() : null;
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        PsiElement id = getNameIdentifier();
        if (id instanceof IdentifierPsiElement) {
            return ((IdentifierPsiElement)id).setName(name);
        }
        throw new IncorrectOperationException("No identifier to rename");
    }
}
