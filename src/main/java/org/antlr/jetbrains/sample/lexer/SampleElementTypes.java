package org.antlr.jetbrains.sample.lexer;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.antlr.jetbrains.sample.SampleLanguage;
import org.antlr.jetbrains.sample.parser.CoreParser;
import org.antlr.jetbrains.sample.parser.TokenLexer;

import java.util.List;

public class SampleElementTypes {
    static {
        // Initialize all token and rule types
        PSIElementTypeFactory.defineLanguageIElementTypes(
                SampleLanguage.INSTANCE,
                TokenLexer.tokenNames,
                CoreParser.ruleNames
        );
    }

    // Helper method to get token types
    private static TokenIElementType getTokenType(int antlrTokenType) {
        List<TokenIElementType> types = PSIElementTypeFactory.getTokenIElementTypes(SampleLanguage.INSTANCE);
        if (antlrTokenType >= 0 && antlrTokenType < types.size()) {
            return types.get(antlrTokenType);
        }
        return null;
    }

    // Token type accessors
    public static TokenIElementType L_ID() { return getTokenType(TokenLexer.L_ID); }
    public static TokenIElementType T_LET() { return getTokenType(TokenLexer.T_LET); }
    public static TokenIElementType T_EQUAL() { return getTokenType(TokenLexer.T_EQUAL); }
    public static TokenIElementType T_SEMICOLON() { return getTokenType(TokenLexer.T_SEMICOLON); }
    public static TokenIElementType T_OPEN_P() { return getTokenType(TokenLexer.T_OPEN_P); }
    public static TokenIElementType T_CLOSE_P() { return getTokenType(TokenLexer.T_CLOSE_P); }
    public static TokenIElementType T_COMMA() { return getTokenType(TokenLexer.T_COMMA); }
    public static TokenIElementType T_LAMBDA() { return getTokenType(TokenLexer.T_LABMDA); }
    public static TokenIElementType T_BEGIN() { return getTokenType(TokenLexer.T_BEGIN); }
    public static TokenIElementType T_END() { return getTokenType(TokenLexer.T_END); }

    // Token groups
    public static final TokenSet KEYWORDS = TokenSet.create(
            T_LET(), T_LAMBDA(), T_BEGIN(), T_END()
    );

    public static final TokenSet OPERATORS = TokenSet.create(
            T_EQUAL(), T_COMMA()
    );

    public static final TokenSet PARENS = TokenSet.create(
            T_OPEN_P(), T_CLOSE_P()
    );
}
