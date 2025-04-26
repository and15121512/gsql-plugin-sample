package org.antlr.jetbrains.sample;

import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.refactoring.rename.RenameDialog;
import com.intellij.refactoring.rename.RenameHandler;
import com.intellij.refactoring.rename.RenameRefactoringDialog;
import org.antlr.jetbrains.sample.psi.AssignmentElement;
import org.antlr.jetbrains.sample.psi.IdentifierPsiElement;
import org.antlr.jetbrains.sample.psi.VariableReferenceElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class SampleRenameHandler implements RenameHandler {

    @Override
    public boolean isRenaming(@NotNull DataContext dataContext) {
        return isAvailableOnDataContext(dataContext);
    }

    @Override
    public boolean isAvailableOnDataContext(@NotNull DataContext dataContext) {
        PsiElement element = LangDataKeys.PSI_ELEMENT.getData(dataContext);
        return element instanceof VariableReferenceElement ||
                element instanceof IdentifierPsiElement ||
                element instanceof AssignmentElement;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file, DataContext dataContext) {
        PsiElement element = LangDataKeys.PSI_ELEMENT.getData(dataContext);
        if (element != null) {
            renameElement(project, element, editor);
        }
    }

    private void renameElement(@NotNull Project project, @NotNull PsiElement element, @Nullable Editor editor) {
        PsiElement elementToRename = element;
        String currentName = "";

        if (element instanceof AssignmentElement) {
            PsiElement nameId = ((AssignmentElement)element).getNameIdentifier();
            if (nameId != null) {
                elementToRename = nameId;
                currentName = ((PsiNamedElement)nameId).getName();
            }
        } else if (element instanceof PsiNamedElement) {
            currentName = ((PsiNamedElement)element).getName();
        }

        RenameDialog dialog = new RenameDialog(project, elementToRename, null, editor);
        if (!currentName.isEmpty()) {
            dialog.addSuggestedNames(List.of(currentName));
        }
        dialog.show();
    }

    @Override
    public void invoke(@NotNull Project project, @NotNull PsiElement[] elements, @NotNull DataContext dataContext) {
        Editor editor = CommonDataKeys.EDITOR.getData(dataContext);
        for (PsiElement element : elements) {
            renameElement(project, element, editor);
        }
    }
}
