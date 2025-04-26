package org.antlr.jetbrains.sample.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import org.antlr.intellij.adaptor.psi.ScopeNode;
import org.antlr.jetbrains.sample.Icons;
import org.antlr.jetbrains.sample.SampleFileType;
import org.antlr.jetbrains.sample.SampleLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;

public class SamplePSIFileRoot extends PsiFileBase implements ScopeNode {
    //private static final Logger LOG = Logger.getInstance(SamplePSIFileRoot.class);

    public SamplePSIFileRoot(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, SampleLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return SampleFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Sample Language File";
    }

    @Override
    public Icon getIcon(int flags) {
        return Icons.SAMPLE_ICON;
    }

    @Override
    public ScopeNode getContext() {
        return null;
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                       @NotNull ResolveState state,
                                       PsiElement lastParent,
                                       @NotNull PsiElement place) {
        // Process variable and function declarations
        for (AssignmentElement assignment : PsiTreeUtil.findChildrenOfType(this, AssignmentElement.class)) {
            PsiElement nameIdentifier = assignment.getNameIdentifier();
            if (nameIdentifier instanceof PsiNamedElement) {
                if (!processor.execute((PsiNamedElement) nameIdentifier, state)) {
                    return false;
                }
            }
        }

        // Process lambda parameters if needed
        for (LambdaExpressionElement lambda : PsiTreeUtil.findChildrenOfType(this, LambdaExpressionElement.class)) {
            for (IdentifierPsiElement param : lambda.getParameters()) {
                if (!processor.execute(param, state)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Nullable
    @Override
    public PsiElement resolve(PsiNamedElement element) {
        // For PsiNameIdentifierOwner elements (like assignments)
        if (element instanceof PsiNameIdentifierOwner) {
            return ((PsiNameIdentifierOwner)element).getNameIdentifier();
        }

        // For elements that implement PsiReference (like identifiers)
        if (element instanceof PsiElement) {
            PsiReference reference = ((PsiElement)element).getReference();
            if (reference != null) {
                return reference.resolve();
            }
        }

        return null;
    }

    /**
     * Helper method to get all top-level declarations in the file
     */
    public Collection<PsiNamedElement> getTopLevelDeclarations() {
        Collection<PsiNamedElement> declarations = new ArrayList<>();

        processDeclarations(new PsiScopeProcessor() {
            @Override
            public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
                if (element instanceof PsiNamedElement) {
                    declarations.add((PsiNamedElement) element);
                }
                return true;
            }
        }, ResolveState.initial(), null, this);

        return declarations;
    }
}
