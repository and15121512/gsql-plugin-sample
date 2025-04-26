package org.antlr.jetbrains.sample;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Processor;
import com.intellij.util.QueryExecutor;
import org.antlr.jetbrains.sample.psi.AssignmentElement;
import org.antlr.jetbrains.sample.psi.IdentifierPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class SampleUsageSearcher implements QueryExecutor<PsiReference, ReferencesSearch.SearchParameters> {
    private static final Logger LOG = Logger.getInstance(SampleUsageSearcher.class);

    @Override
    public boolean execute(@NotNull ReferencesSearch.SearchParameters params,
                           @NotNull Processor<? super PsiReference> consumer) {
        try {
            PsiElement target = params.getElementToSearch();
            if (!isSearchable(target)) return true;

            String targetName = getName(target);
            if (targetName == null) return true;

            return findUsages(target, targetName, consumer);
        } catch (Exception e) {
            LOG.error("Error in usage search", e);
            return true;
        }
    }

    private boolean isSearchable(PsiElement element) {
        return element instanceof AssignmentElement ||
                (element instanceof IdentifierPsiElement &&
                        element.getParent() instanceof AssignmentElement);
    }

    private @Nullable String getName(PsiElement element) {
        if (element instanceof AssignmentElement) {
            return ((AssignmentElement)element).getName();
        }
        if (element instanceof IdentifierPsiElement) {
            return ((IdentifierPsiElement)element).getName();
        }
        return null;
    }

    private boolean findUsages(PsiElement target,
                               String targetName,
                               Processor<? super PsiReference> consumer) {
        AtomicBoolean continueSearch = new AtomicBoolean(true);

        PsiTreeUtil.processElements(target.getContainingFile(), IdentifierPsiElement.class,
                element -> {
                    if (!continueSearch.get()) return false;

                    if (targetName.equals(element.getName())) {
                        PsiReference ref = element.getReference();
                        if (ref != null && ref.isReferenceTo(target)) {
                            if (!consumer.process(ref)) {
                                continueSearch.set(false);
                            }
                        }
                    }
                    return true;
                });

        return continueSearch.get();
    }
}
