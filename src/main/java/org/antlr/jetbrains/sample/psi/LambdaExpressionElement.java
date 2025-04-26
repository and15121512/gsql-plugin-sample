package org.antlr.jetbrains.sample.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import org.antlr.jetbrains.sample.lexer.SampleElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LambdaExpressionElement extends SamplePsiElement {
    public LambdaExpressionElement(@NotNull ASTNode node) { super(node); }

    public List<IdentifierPsiElement> getParameters() {
        return PsiTreeUtil.getChildrenOfTypeAsList(
                findChildByType(SampleElementTypes.T_OPEN_P()),
                IdentifierPsiElement.class
        );
    }

    public @Nullable BlockElement getBody() {
        return findChildByClass(BlockElement.class);
    }
}
