package org.antlr.jetbrains.sample.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public abstract class StatementElement extends SamplePsiElement {
    public StatementElement(@NotNull ASTNode node) {
        super(node);
    }

    // Common statement methods can be added here
}
