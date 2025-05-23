package org.antlr.jetbrains.sample;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.antlr.jetbrains.sample.validator.InterpreterStyleValidatorLegacy;
import org.antlr.jetbrains.sample.validator.SampleSemanticValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/** An external annotator is an object that analyzes code in a document
 *  and annotates the PSI elements with errors or warnings. Because such
 *  analysis can be expensive, we don't want it in the GUI event loop. Jetbrains
 *  provides this external annotator mechanism to run these analyzers out of band.
 */
public class SampleExternalAnnotator extends ExternalAnnotator<PsiFile, List<SampleExternalAnnotator.Issue>> {
    // NOTE: can't use instance vars as only 1 instance

	public static class Issue {
		String msg;
		PsiElement offendingNode;
		public Issue(String msg, PsiElement node) { this.msg = msg; offendingNode = node; }
	}

	/** Called first; in our case, just return file and do nothing */
	@Override
	@Nullable
	public PsiFile collectInformation(@NotNull PsiFile file) {
		return file;
	}

	/** Called 2nd; look for trouble in file and return list of issues.
	 *
	 *  For most custom languages, you would not reimplement your semantic
	 *  analyzer using PSI trees. Instead, here is where you would call out to
	 *  your custom languages compiler or interpreter to get error messages
	 *  or other bits of information you'd like to annotate the document with.
	 */
	@Nullable
	@Override
	public List<Issue> doAnnotate(final PsiFile file) {
		//return new InterpreterStyleValidatorLegacy().validate(file);
		return new SampleSemanticValidator().validate(file);
	}

	/** Called 3rd to actually annotate the editor window */
	@Override
	public void apply(@NotNull PsiFile file,
					  List<Issue> issues,
					  @NotNull AnnotationHolder holder)
	{
		for (Issue issue : issues) {
			TextRange range = issue.offendingNode.getTextRange();
			holder.createErrorAnnotation(range, issue.msg);
		}
	}
}
