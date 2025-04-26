package org.antlr.jetbrains.sample.psi;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

public class LiteralElement extends ExprElement {
    public LiteralElement(@NotNull ASTNode node) {
        super(node);
    }

    public Object getValue() {
        // Implement based on your literal types
        return getText();
    }
}
