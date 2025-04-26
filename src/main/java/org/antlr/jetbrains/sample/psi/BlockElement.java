package org.antlr.jetbrains.sample.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlockElement extends SamplePsiElement {
    public BlockElement(@NotNull ASTNode node) {
        super(node);
    }

    public List<StatementElement> getStatements() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, StatementElement.class);
    }
}
