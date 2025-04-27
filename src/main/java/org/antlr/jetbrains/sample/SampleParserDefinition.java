package org.antlr.jetbrains.sample;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor;
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode;
import org.antlr.jetbrains.sample.lexer.SampleElementTypes;
import org.antlr.jetbrains.sample.lexer.SampleLexerAdaptor;
import org.antlr.jetbrains.sample.parser.CoreParser;
import org.antlr.jetbrains.sample.parser.TokenLexer;
import org.antlr.jetbrains.sample.psi.*;
import org.antlr.jetbrains.sample.psi.SamplePSIFileRoot;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SampleParserDefinition implements ParserDefinition {
	public static final IFileElementType FILE =
		new IFileElementType(SampleLanguage.INSTANCE);

	public static TokenIElementType ID;

	static {
		PSIElementTypeFactory.defineLanguageIElementTypes(SampleLanguage.INSTANCE,
		                                                  CoreParser.tokenNames,
		                                                  CoreParser.ruleNames);
	}

	public static TokenIElementType ID() {
		return SampleElementTypes.L_ID();
	}

	public static final TokenSet COMMENTS =
		PSIElementTypeFactory.createTokenSet(
			SampleLanguage.INSTANCE,
				TokenLexer.L_M_COMMENT,
				TokenLexer.L_S_COMMENT);

	public static final TokenSet WHITESPACE =
		PSIElementTypeFactory.createTokenSet(
			SampleLanguage.INSTANCE,
				TokenLexer.L_WS);

	public static final TokenSet STRING =
		PSIElementTypeFactory.createTokenSet(
			SampleLanguage.INSTANCE,
				TokenLexer.T_ACTION // It's dummy, probably remove later
		);

	@NotNull
	@Override
	public Lexer createLexer(Project project) {
		return new SampleLexerAdaptor();
	}

	@NotNull
	public PsiParser createParser(final Project project) {
		final CoreParser parser = new CoreParser(null);
		return new ANTLRParserAdaptor(SampleLanguage.INSTANCE, parser) {
			@Override
			protected ParseTree parse(Parser parser, IElementType root) {
				// start rule depends on root passed in; sometimes we want to create an ID node etc...
				if ( root instanceof IFileElementType ) {
					return ((CoreParser) parser).program();
				}
				// let's hope it's an ID as needed by "rename function"
				//return ((CoreParser) parser).primary();
				// WHICH RULES COULD BE HERE ??? Just program for now
				return ((CoreParser) parser).program();
			}
		};
	}

	/** "Tokens of those types are automatically skipped by PsiBuilder." */
	@NotNull
	public TokenSet getWhitespaceTokens() {
		return WHITESPACE;
	}

	@NotNull
	public TokenSet getCommentTokens() {
		return COMMENTS;
	}

	@NotNull
	public TokenSet getStringLiteralElements() {
		return STRING;
	}

	public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
		return SpaceRequirements.MAY;
	}

	/** What is the IFileElementType of the root parse tree node? It
	 *  is called from {@link #createFile(FileViewProvider)} at least.
	 */
	@Override
	public IFileElementType getFileNodeType() {
		return FILE;
	}

	/** Create the root of your PSI tree (a PsiFile).
	 *
	 *  From IntelliJ IDEA Architectural Overview:
	 *  "A PSI (Program Structure Interface) file is the root of a structure
	 *  representing the contents of a file as a hierarchy of elements
	 *  in a particular programming language."
	 *
	 *  PsiFile is to be distinguished from a FileASTNode, which is a parse
	 *  tree node that eventually becomes a PsiFile. From PsiFile, we can get
	 *  it back via: {@link PsiFile#getNode}.
	 */
	@Override
	public PsiFile createFile(FileViewProvider viewProvider) {
		return new SamplePSIFileRoot(viewProvider);
	}

	/** Convert from *NON-LEAF* parse node (AST they call it)
	 *  to PSI node. Leaves are created in the AST factory.
	 *  Rename re-factoring can cause this to be
	 *  called on a TokenIElementType since we want to rename ID nodes.
	 *  In that case, this method is called to create the root node
	 *  but with ID type. Kind of strange, but we can simply create a
	 *  ASTWrapperPsiElement to make everything work correctly.
	 *
	 *  RuleIElementType.  Ah! It's that ID is the root
	 *  IElementType requested to parse, which means that the root
	 *  node returned from parsetree->PSI conversion.  But, it
	 *  must be a CompositeElement! The adaptor calls
	 *  rootMarker.done(root) to finish off the PSI conversion.
	 *  See {@link ANTLRParserAdaptor#parse(IElementType root,
	 *  PsiBuilder)}
	 *
	 *  If you don't care to distinguish PSI nodes by type, it is
	 *  sufficient to create a {@link ANTLRPsiNode} around
	 *  the parse tree node
	 */
	@Override
	public PsiElement createElement(ASTNode node) {
		IElementType type = node.getElementType();

		if (type == SampleElementTypes.L_ID()) {
			// Create leaf node with type and text
			return new IdentifierPsiElement(type, node.getText());
		}
		if (type instanceof RuleIElementType) {
			switch (((RuleIElementType)type).getRuleIndex()) {
				case CoreParser.RULE_assigment_stmt:
					return new AssignmentElement(node);
				case CoreParser.RULE_lambda:
					return new LambdaExpressionElement(node);
				case CoreParser.RULE_var:
					return new VariableReferenceElement(node);
				case CoreParser.RULE_func:
					return new FunctionCallElement(node);
				case CoreParser.RULE_expr:
					return createExprElement(node);
				case CoreParser.RULE_arg:
					return new ArgElement(node);
				case CoreParser.RULE_literal:
					return new LiteralElement(node);
				// Add other rule cases as needed
			}
		}
		return new SamplePsiElement(node);
	}

	private PsiElement createExprElement(ASTNode node) {
		// Check first child to determine expr type
		ASTNode firstChild = node.getFirstChildNode();
		if (firstChild != null) {
			IElementType firstType = firstChild.getElementType();
			if (firstType == SampleElementTypes.T_OPEN_P()) {
				return new LambdaExpressionElement(node);
			}
			else if (firstType == SampleElementTypes.L_ID()) {
				// Could be func call or var reference
				ASTNode next = firstChild.getTreeNext();
				if (next != null && next.getElementType() == SampleElementTypes.T_OPEN_P()) {
					return new FunctionCallElement(node);
				}
				return new VariableReferenceElement(node);
			}
		}
		return new LiteralElement(node); // Fallback
	}
}
