package org.antlr.jetbrains.sample.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SamplePsiElement extends ANTLRPsiNode {
    public SamplePsiElement(@NotNull ASTNode node) {
        super(node);
    }

    protected @Nullable PsiElement findChildByType(IElementType type) {
        ASTNode childNode = getNode().findChildByType(type);
        return childNode != null ? childNode.getPsi() : null;
    }
}
