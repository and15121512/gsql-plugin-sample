package org.antlr.jetbrains.sample.run;

import com.intellij.execution.lineMarker.*;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class GsqlRunLineMarkerProvider extends RunLineMarkerContributor {
    @Override
    public @Nullable Info getInfo(@NotNull PsiElement element) {
        // Only show on the first token of the file
        if (isFirstTokenInFile(element)) {
            return new Info(
                    AllIcons.RunConfigurations.TestState.Run,
                    new AnAction[]{new RunMyScriptAction()},
                    e -> "Run GSQL Script"
            );
        }
        return null;
    }

    private boolean isFirstTokenInFile(PsiElement element) {
        PsiFile file = element.getContainingFile();
        if (file == null) return false;

        PsiElement firstLeaf = PsiTreeUtil.getDeepestFirst(file);
        return element == firstLeaf;
    }
}
