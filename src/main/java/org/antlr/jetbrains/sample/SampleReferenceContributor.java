package org.antlr.jetbrains.sample;

import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.antlr.jetbrains.sample.psi.IdentifierPsiElement;
import org.jetbrains.annotations.NotNull;

public class SampleReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement(IdentifierPsiElement.class),
                new PsiReferenceProvider() {
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(
                            @NotNull PsiElement element,
                            @NotNull ProcessingContext context) {
                        return new PsiReference[]{((IdentifierPsiElement)element).getReference()};
                    }
                }
        );
    }
}
