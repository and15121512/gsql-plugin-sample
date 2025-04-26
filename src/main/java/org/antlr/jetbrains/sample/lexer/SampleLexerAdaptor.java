package org.antlr.jetbrains.sample.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.antlr.jetbrains.sample.SampleLanguage;
import org.antlr.jetbrains.sample.parser.TokenLexer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.IntStream;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class SampleLexerAdaptor extends ANTLRLexerAdaptor {
    public SampleLexerAdaptor() {
        super(SampleLanguage.INSTANCE, new TokenLexer(CharStreams.fromString("")));
    }

    @Override
    public IElementType getTokenType() {
        Token currentToken = getCurrentToken();
        if (currentToken == null) {
            return null;
        }
        int tokenType = currentToken.getType();
        if (tokenType == Token.EOF) {
            return null;
        }

        List<TokenIElementType> types = PSIElementTypeFactory.getTokenIElementTypes(SampleLanguage.INSTANCE);
        if (tokenType >= 0 && tokenType < types.size()) {
            return types.get(tokenType);
        }
        return TokenType.BAD_CHARACTER;
    }
}
