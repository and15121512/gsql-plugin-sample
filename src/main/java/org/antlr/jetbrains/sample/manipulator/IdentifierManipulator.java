package org.antlr.jetbrains.sample.manipulator;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulator;
import org.antlr.jetbrains.sample.psi.IdentifierPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IdentifierManipulator implements ElementManipulator<IdentifierPsiElement> {

    // Version with TextRange (primary implementation)
    @Override
    public @Nullable IdentifierPsiElement handleContentChange(
            @NotNull IdentifierPsiElement element,
            @NotNull TextRange range,
            @NotNull String newContent) {
        String oldText = element.getText();
        String newText = oldText.substring(0, range.getStartOffset()) +
                newContent +
                oldText.substring(range.getEndOffset());
        return new IdentifierPsiElement(element.getNode().getElementType(), newText);
    }

    // Version without TextRange (delegates to primary implementation)
    @Override
    public @Nullable IdentifierPsiElement handleContentChange(
            @NotNull IdentifierPsiElement element,
            @NotNull String newContent) {
        return handleContentChange(element, TextRange.allOf(element.getText()), newContent);
    }

    @Override
    public @NotNull TextRange getRangeInElement(@NotNull IdentifierPsiElement element) {
        return TextRange.allOf(element.getText());
    }
}
