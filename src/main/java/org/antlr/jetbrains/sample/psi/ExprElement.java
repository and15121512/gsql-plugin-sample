package org.antlr.jetbrains.sample.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class ExprElement extends SamplePsiElement {
    public ExprElement(@NotNull ASTNode node) {
        super(node);
    }

    // Shared expression methods...
}
