package org.antlr.jetbrains.sample;

import com.intellij.lang.DefaultASTFactoryImpl;
import com.intellij.lang.ParserDefinition;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
//import com.intellij.psi.impl.source.tree.PsiCoreCommentImpl;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.antlr.jetbrains.sample.lexer.SampleElementTypes;
import org.antlr.jetbrains.sample.parser.TokenLexer;
import org.antlr.jetbrains.sample.psi.IdentifierPsiElement;
import org.jetbrains.annotations.NotNull;

/** How to create parse tree nodes (Jetbrains calls them AST nodes). Later
 *  non-leaf nodes are converted to PSI nodes by the {@link ParserDefinition}.
 *  Leaf nodes are already considered PSI nodes.  This is mostly just
 *  {@link DefaultASTFactoryImpl} but with comments on the methods that you might want
 *  to override.
 */
public class SampleASTFactory extends DefaultASTFactoryImpl {
	/** Create an internal parse tree node. FileElement for root or a parse tree CompositeElement (not
	 *  PSI) for the token.
	 *  The FileElement is a parse tree node, which is converted to a PsiFile
	 *  by {@link ParserDefinition#createFile}.
	 */
	@NotNull
    @Override
    public CompositeElement createComposite(IElementType type) {
	    return super.createComposite(type);
    }

	/** Create a parse tree (AST) leaf node from a token. Doubles as a PSI leaf node.
	 *  Does not see whitespace tokens.  Default impl makes {@link LeafPsiElement}
	 *   depending on {@link ParserDefinition#getCommentTokens()}.
	 */
	@Override
	public @NotNull LeafElement createLeaf(@NotNull IElementType type, @NotNull CharSequence text) {
		if (type == SampleElementTypes.L_ID()) {
			return new IdentifierPsiElement(type, text);
		}
		return super.createLeaf(type, text);
	}
}
