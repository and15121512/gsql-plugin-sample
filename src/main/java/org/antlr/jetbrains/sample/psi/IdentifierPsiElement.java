package org.antlr.jetbrains.sample.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode;
import org.jetbrains.annotations.NotNull;

public class IdentifierPsiElement extends ANTLRPsiLeafNode implements PsiNamedElement {
    public IdentifierPsiElement(@NotNull IElementType type, @NotNull CharSequence text) {
        super(type, text);
    }

    @Override
    public String getName() {
        return getText();
    }

    @Override
    public PsiReference getReference() {
        return new SampleReference(this);
    }

    @Override
    public PsiElement setName(@NotNull String newName) throws IncorrectOperationException {
        // Create new leaf node with the updated name
        LeafPsiElement newElement = new LeafPsiElement(getElementType(), newName) {
            @Override
            public PsiReference getReference() {
                return new SampleReference(this);
            }
        };
        return this.replace(newElement);
    }
}
