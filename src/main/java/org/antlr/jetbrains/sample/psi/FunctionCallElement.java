package org.antlr.jetbrains.sample.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import org.antlr.jetbrains.sample.lexer.SampleElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FunctionCallElement extends SamplePsiElement {
    public FunctionCallElement(@NotNull ASTNode node) { super(node); }

    public @Nullable IdentifierPsiElement getCalledName() {
        return (IdentifierPsiElement)findChildByType(SampleElementTypes.L_ID());
    }

    public List<ArgElement> getArguments() {
        return PsiTreeUtil.getChildrenOfTypeAsList(
                findChildByType(SampleElementTypes.T_OPEN_P()),
                ArgElement.class
        );
    }

    @Override
    public PsiReference getReference() {
        IdentifierPsiElement nameElement = getCalledName();
        return nameElement != null ? nameElement.getReference() : null;
    }
}
