package com.bindglam.origami.api.script;

import com.bindglam.origami.api.script.exceptions.IllegalCharException;
import com.bindglam.origami.api.script.exceptions.InvalidSyntaxException;
import com.bindglam.origami.api.script.exceptions.ScriptException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public final class Lexer {
    private static final Pattern PATTERN_NUMBER = Pattern.compile("^[\\d]*$");
    private static final Pattern PATTERN_LETTER = Pattern.compile("^[a-zA-Z]*$");
    private static final Pattern PATTERN_CHAR = Pattern.compile("^[\\w]*$");
    private static final List<String> KEYWORDS = List.of("VAR", "AND", "OR", "NOT", "IF", "ELIF", "ELSE", "FOR", "TO", "STEP", "WHILE", "FUNC", "THEN", "END");

    private final String text;

    private final Position pos;
    private Character currentChar = null;

    public Lexer(String fn, String text) {
        this.text = text;

        this.pos = new Position(fn, text);

        advance();
    }

    public void advance() {
        pos.advance(currentChar);

        if(pos.getIdx() < text.length())
            currentChar = text.charAt(pos.getIdx());
        else
            currentChar = null;
    }

    public List<Token> makeTokens() throws ScriptException {
        List<Token> tokens = new ArrayList<>();

        while(currentChar != null) {
            if(" \t".contains(currentChar.toString())) {
                advance();
                continue;
            } else if(";\n".contains(currentChar.toString())) {
                tokens.add(new Token(Token.Type.NEWLINE, null, pos, null));
                advance();
                continue;
            } else if(PATTERN_NUMBER.matcher(currentChar.toString()).matches()) {
                tokens.add(makeNumber());
                continue;
            } else if(PATTERN_LETTER.matcher(currentChar.toString()).matches()) {
                tokens.add(makeIdentifier());
                continue;
            }

            switch (currentChar) {
                case '"':
                    tokens.add(makeString());
                    break;

                case '+':
                    tokens.add(new Token(Token.Type.PLUS, null, pos, null));
                    advance();
                    break;

                case '-':
                    tokens.add(makeMinusOrArrow());
                    break;

                case '*':
                    tokens.add(new Token(Token.Type.MUL, null, pos, null));
                    advance();
                    break;

                case '/':
                    tokens.add(new Token(Token.Type.DIV, null, pos, null));
                    advance();
                    break;

                case '^':
                    tokens.add(new Token(Token.Type.POW, null, pos, null));
                    advance();
                    break;

                case '(':
                    tokens.add(new Token(Token.Type.LPAREN, null, pos, null));
                    advance();
                    break;

                case ')':
                    tokens.add(new Token(Token.Type.RPAREN, null, pos, null));
                    advance();
                    break;

                case '!':
                    tokens.add(makeNotEquals());
                    break;

                case '=':
                    tokens.add(makeEquals());
                    break;

                case '<':
                    tokens.add(makeLessThan());
                    break;

                case '>':
                    tokens.add(makeGreaterThan());
                    break;

                case ',':
                    tokens.add(new Token(Token.Type.COMMA, null, pos, null));
                    advance();
                    break;

                default:
                    Position posStart = pos.clone();
                    Character character = currentChar;
                    advance();

                    throw new IllegalCharException(posStart, pos, "'" + character + "'");
            }
        }

        tokens.add(new Token(Token.Type.EOF, null, pos, null));
        return tokens;
    }

    private Token makeNumber() {
        StringBuilder numStr = new StringBuilder();
        int dotCnt = 0;
        Position posStart = pos.clone();

        while(currentChar != null && (PATTERN_NUMBER.matcher(currentChar.toString()).matches() || currentChar == '.')) {
            if(currentChar == '.') {
                if(dotCnt != 0)
                    break;

                dotCnt += 1;
                numStr.append('.');
            } else {
                numStr.append(currentChar);
            }
            advance();
        }

        return new Token(Token.Type.NUMBER, Double.parseDouble(numStr.toString()), posStart, pos);
    }

    private Token makeString() {
        StringBuilder string = new StringBuilder();
        Position posStart = pos.clone();
        boolean escapeCharacter = false;

        advance();

        Map<Character, Character> escapeChars = Map.of(
                'n', '\n',
                't', '\t'
        );

        while(currentChar != null && (currentChar != '"' || escapeCharacter)) {
            if(escapeCharacter) {
                string.append(escapeChars.getOrDefault(currentChar, currentChar));
            } else {
                if(currentChar == '\\') {
                    escapeCharacter = true;
                } else {
                    string.append(currentChar);
                }
            }

            advance();

            escapeCharacter = false;
        }

        advance();

        return new Token(Token.Type.STRING, string.toString(), posStart, pos);
    }

    private Token makeIdentifier() {
        StringBuilder idStr = new StringBuilder();
        Position posStart = pos.clone();

        while(currentChar != null && PATTERN_CHAR.matcher(currentChar.toString()).matches()) {
            idStr.append(currentChar);
            advance();
        }

        String result = idStr.toString();

        Token.Type tokenType;
        if(KEYWORDS.contains(result))
            tokenType = Token.Type.KEYWORD;
        else
            tokenType = Token.Type.IDENTIFIER;

        return new Token(tokenType, result, posStart, pos);
    }

    private Token makeMinusOrArrow() {
        Token.Type tokenType = Token.Type.MINUS;
        Position posStart = pos.clone();

        advance();

        if(currentChar == '>') {
            advance();

            tokenType = Token.Type.ARROW;
        }

        return new Token(tokenType, null, posStart, pos);
    }

    private Token makeNotEquals() throws ScriptException {
        Position posStart = pos.clone();

        advance();

        if(currentChar == '=') {
            advance();

            return new Token(Token.Type.NOT_EQUAL, null, posStart, pos);
        }

        advance();

        throw new InvalidSyntaxException(posStart, pos, "Expected '='");
    }

    private Token makeEquals() {
        Token.Type tokenType = Token.Type.EQUAL;
        Position posStart = pos.clone();

        advance();

        if(currentChar == '=') {
            advance();
            tokenType = Token.Type.EQUAL_EQUAL;
        }

        return new Token(tokenType, null, posStart, pos);
    }

    private Token makeLessThan() {
        Token.Type tokenType = Token.Type.LESS_THAN;
        Position posStart = pos.clone();

        advance();

        if(currentChar == '=') {
            advance();
            tokenType = Token.Type.LESS_THAN_EQUAL;
        }

        return new Token(tokenType, null, posStart, pos);
    }

    private Token makeGreaterThan() {
        Token.Type tokenType = Token.Type.GREATER_THAN;
        Position posStart = pos.clone();

        advance();

        if(currentChar == '=') {
            advance();
            tokenType = Token.Type.GREATER_THAN_EQUAL;
        }

        return new Token(tokenType, null, posStart, pos);
    }
}
