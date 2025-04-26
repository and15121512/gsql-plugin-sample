package org.antlr.jetbrains.sample;

import com.intellij.psi.PsiElement;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import org.antlr.jetbrains.sample.psi.AssignmentElement;
import org.antlr.jetbrains.sample.psi.IdentifierPsiElement;
import org.antlr.jetbrains.sample.psi.VariableReferenceElement;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SampleRenameProcessor extends RenamePsiElementProcessor {
    @Override
    public boolean canProcessElement(@NotNull PsiElement element) {
        return element instanceof VariableReferenceElement ||
                element instanceof IdentifierPsiElement ||
                element instanceof AssignmentElement;
    }

    @Override
    public void prepareRenaming(@NotNull PsiElement element,
                                @NotNull String newName,
                                @NotNull Map<PsiElement, String> allRenames) {
        // Handle different element types
        if (element instanceof AssignmentElement) {
            PsiElement nameId = ((AssignmentElement)element).getNameIdentifier();
            if (nameId != null) {
                allRenames.put(nameId, newName);
            }
        } else {
            // Find all references to update
            ReferencesSearch.search(element).forEach(reference -> {
                PsiElement refElement = reference.getElement();
                if (refElement instanceof VariableReferenceElement ||
                        refElement instanceof IdentifierPsiElement) {
                    allRenames.put(refElement, newName);
                }
            });
        }
    }
}
