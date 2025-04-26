package org.antlr.jetbrains.sample;

import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.antlr.jetbrains.sample.psi.AssignmentElement;
import org.antlr.jetbrains.sample.psi.IdentifierPsiElement;
import org.antlr.jetbrains.sample.psi.VariableReferenceElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class SampleFindUsagesProvider implements FindUsagesProvider {
	@Override
	public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
		return psiElement instanceof AssignmentElement ||
				psiElement instanceof VariableReferenceElement ||
				(psiElement instanceof IdentifierPsiElement &&
						psiElement.getParent() instanceof AssignmentElement);
	}

	@Override
	public @Nullable String getHelpId(@NotNull PsiElement psiElement) {
		return "reference.GSQL";
	}

	@Override
	public @NotNull String getType(@NotNull PsiElement element) {
		if (element instanceof AssignmentElement) {
			return ((AssignmentElement)element).isFunction() ? "function" : "variable";
		}
		return "reference";
	}

	@Override
	public @NotNull String getDescriptiveName(@NotNull PsiElement element) {
		if (element instanceof PsiNamedElement) {
			String name = ((PsiNamedElement)element).getName();
			return name != null ? name : "";
		}
		return "";
	}

	@Override
	public @NotNull String getNodeText(@NotNull PsiElement element, boolean useFullName) {
		return getDescriptiveName(element);
	}
}
