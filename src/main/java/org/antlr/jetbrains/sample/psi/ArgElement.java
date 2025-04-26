package org.antlr.jetbrains.sample.psi;

import com.intellij.lang.ASTNode;
import org.antlr.jetbrains.sample.lexer.SampleElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArgElement extends SamplePsiElement {
    public ArgElement(@NotNull ASTNode node) {
        super(node);
    }

    public @Nullable IdentifierPsiElement getNameIdentifier() {
        return (IdentifierPsiElement) findChildByType(SampleElementTypes.L_ID());
    }

    public @Nullable ExprElement getValueExpression() {
        return findChildByClass(ExprElement.class);
    }

    public boolean isKwarg() {
        return getNameIdentifier() != null &&
                findChildByType(SampleElementTypes.T_EQUAL()) != null;
    }
}
