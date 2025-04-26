package org.antlr.jetbrains.sample.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import org.antlr.jetbrains.sample.SampleFileType;
import org.antlr.jetbrains.sample.SampleLanguage;
import org.jetbrains.annotations.NotNull;

public class SampleElementFactory {
    public static @NotNull PsiElement createElement(@NotNull Project project,
                                                    @NotNull IElementType type,
                                                    @NotNull String text) {
        // Get the file factory
        PsiFileFactory factory = PsiFileFactory.getInstance(project);

        // Create minimal valid code containing our element
        String dummyContent = createDummyContent(text);

        // Create a dummy file
        PsiFile file = factory.createFileFromText(
                "dummy." + SampleFileType.FILE_EXTENSION,
                SampleLanguage.INSTANCE,
                dummyContent
        );

        // Find and return the matching element
        return findMatchingElement(file, type);
    }

    private static String createDummyContent(String text) {
        // Adjust based on your language's syntax requirements
        if (text.startsWith("$")) {
            return "let x = " + text + ";"; // For variables
        }
        return text + " = null;"; // For identifiers
    }

    private static PsiElement findMatchingElement(PsiFile file, IElementType type) {
        // Walk through the PSI tree to find the first matching element
        for (PsiElement element : file.getChildren()) {
            PsiElement result = findElementOfType(element, type);
            if (result != null) {
                return result;
            }
        }
        throw new IllegalStateException("Could not create element of type: " + type);
    }

    private static PsiElement findElementOfType(PsiElement root, IElementType type) {
        if (root.getNode().getElementType() == type) {
            return root;
        }
        for (PsiElement child : root.getChildren()) {
            PsiElement result = findElementOfType(child, type);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}
