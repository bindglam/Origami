package com.bindglam.origami.api.script;

import com.bindglam.origami.api.script.exceptions.InvalidSyntaxException;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.node.*;
import com.bindglam.origami.api.utils.ThrowingSupplier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public final class Parser {
    private final List<Token> tokens;
    private int tokenIdx = -1;

    private Token currentToken = null;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;

        advance();
    }

    public void advance() {
        tokenIdx++;

        updateCurrentToken();
    }

    public Token reverse(int amount) {
        tokenIdx -= amount;
        updateCurrentToken();
        return currentToken;
    }

    private void updateCurrentToken() {
        if(tokenIdx >= 0 && tokenIdx < tokens.size())
            currentToken = tokens.get(tokenIdx);
    }

    public Node parse() throws ScriptException {
        Node node = statements();

        if(currentToken.type() != Token.Type.EOF)
            throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected '+', '-', '*', '/', '^', '==', '!=', '<', '>', <=', '>=', 'AND' or 'OR'");

        return node;
    }

    private ListNode statements() throws ScriptException {
        List<Node> statements = new ArrayList<>();
        Position posStart = Objects.requireNonNull(currentToken.posStart()).clone();

        while(currentToken.type() == Token.Type.NEWLINE)
            advance();

        Node statement = expr();
        statements.add(statement);

        boolean moreStatements = true;
        while(true) {
            int newlineCnt = 0;
            while(currentToken.type() == Token.Type.NEWLINE) {
                advance();

                newlineCnt++;
            }

            if(newlineCnt == 0)
                moreStatements = false;
            if(!moreStatements) break;

            try {
                statement = expr();
            } catch (ScriptException e) {
                moreStatements = false;
                continue;
            }

            statements.add(statement);
        }

        return new ListNode(statements, posStart, Objects.requireNonNull(currentToken.posEnd()).clone());
    }

    private Node expr() throws ScriptException {
        if(currentToken.matches(Token.Type.KEYWORD, "VAR")) {
            advance();

            if(currentToken.type() != Token.Type.IDENTIFIER)
                throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected identifier");

            Token varName = currentToken;

            advance();

            if(currentToken.type() != Token.Type.EQUAL)
                throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected '='");

            advance();

            Node expr = expr();

            return new VarAssignNode(varName, expr);
        }

        return binOp(this::compExpr, (token) -> token.type() == Token.Type.KEYWORD && (Objects.equals(token.value(), "AND") || Objects.equals(token.value(), "OR")), null);
    }

    private Node compExpr() throws ScriptException {
        if(currentToken.matches(Token.Type.KEYWORD, "NOT")) {
            Token operationToken = currentToken;

            advance();

            Node node = compExpr();

            return new UnaryOpNode(operationToken, node);
        }

        return binOp(this::arithExpr, List.of(Token.Type.EQUAL_EQUAL, Token.Type.NOT_EQUAL, Token.Type.LESS_THAN, Token.Type.GREATER_THAN, Token.Type.LESS_THAN_EQUAL, Token.Type.GREATER_THAN_EQUAL), null);
    }

    private Node arithExpr() throws ScriptException {
        return binOp(this::term, List.of(Token.Type.PLUS, Token.Type.MINUS), null);
    }

    private Node term() throws ScriptException {
        return binOp(this::factor, List.of(Token.Type.MUL, Token.Type.DIV), null);
    }

    private Node factor() throws ScriptException {
        Token tok = currentToken;

        switch(tok.type()) {
            case PLUS, MINUS:
                advance();

                Node factor = this.factor();

                return new UnaryOpNode(tok, factor);
        }

        return power();
    }

    private Node power() throws ScriptException {
        return binOp(this::call, List.of(Token.Type.POW), this::factor);
    }

    private Node call() throws ScriptException {
        Node atom = atom();

        if(currentToken.type() == Token.Type.LPAREN) {
            advance();

            List<Node> argNodes = new ArrayList<>();

            if(currentToken.type() == Token.Type.RPAREN) {
                advance();
            } else {
                argNodes.add(expr());

                while(currentToken.type() == Token.Type.COMMA) {
                    advance();

                    argNodes.add(expr());
                }

                if(currentToken.type() != Token.Type.RPAREN)
                    throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected ',' or ')'");

                advance();
            }

            return new CallNode(atom, argNodes);
        }

        return atom;
    }

    private Node atom() throws ScriptException {
        Token tok = currentToken;

        switch(tok.type()) {
            case NUMBER:
                advance();

                return new NumberNode(tok);

            case STRING:
                advance();

                return new StringNode(tok);

            case IDENTIFIER:
                advance();

                return new VarAccessNode(tok);

            case LPAREN:
                advance();

                Node expr = expr();

                if(currentToken.type() == Token.Type.RPAREN) {
                    advance();

                    return expr;
                } else {
                    throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected ')'");
                }

            case KEYWORD:
                if(tok.matches(Token.Type.KEYWORD, "IF")) {
                    return ifExpr();
                } else if(tok.matches(Token.Type.KEYWORD, "FOR")) {
                    return forExpr();
                } else if(tok.matches(Token.Type.KEYWORD, "WHILE")) {
                    return whileExpr();
                } else if(tok.matches(Token.Type.KEYWORD, "FUNC")) {
                    return funcDef();
                }
        }

        throw new InvalidSyntaxException(tok.posStart(), tok.posEnd(), "Expected number, identifier, '+', '-', or '('");
    }

    private Node ifExpr() throws ScriptException {
        return ifExprCases("IF");
    }

    private IfNode ifExprB() throws ScriptException {
        return ifExprCases("ELIF");
    }

    private Node ifExprC() throws ScriptException {
        Node elseCase = null;

        if(currentToken.matches(Token.Type.KEYWORD, "ELSE")) {
            advance();

            if(currentToken.type() == Token.Type.NEWLINE) {
                advance();

                elseCase = statements();

                if(currentToken.matches(Token.Type.KEYWORD, "END")) {
                    advance();
                } else {
                    throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected 'END'");
                }
            } else {
                elseCase = expr();
            }
        }

        return elseCase;
    }

    private IfNode ifExprBorC() throws ScriptException {
        List<IfNode.Case> cases = new ArrayList<>();
        Node elseCase;

        if(currentToken.matches(Token.Type.KEYWORD, "ELIF")) {
            IfNode allCases = ifExprB();
            cases = allCases.cases();
            elseCase = allCases.elseCase();
        } else {
            elseCase = ifExprC();
        }

        return new IfNode(cases, elseCase);
    }

    private IfNode ifExprCases(String caseKeyword) throws ScriptException {
        List<IfNode.Case> cases = new ArrayList<>();
        Node elseCase = null;

        if(!currentToken.matches(Token.Type.KEYWORD, caseKeyword))
            throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected '" + caseKeyword + "'");

        advance();

        Node condition = expr();

        if(!currentToken.matches(Token.Type.KEYWORD, "THEN"))
            throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected 'THEN'");

        advance();

        if(currentToken.type() == Token.Type.NEWLINE) {
            advance();

            ListNode statements = statements();

            cases.add(new IfNode.Case(condition, statements));

            if(currentToken.matches(Token.Type.KEYWORD, "END")) {
                advance();
            } else {
                IfNode allCases = ifExprBorC();

                cases.addAll(allCases.cases());
                elseCase = allCases.elseCase();
            }
        } else {
            Node expr = expr();

            cases.add(new IfNode.Case(condition, expr));

            IfNode allCases = ifExprBorC();
            cases.addAll(allCases.cases());
            elseCase = allCases.elseCase();
        }

        return new IfNode(cases, elseCase);
    }

    private Node forExpr() throws ScriptException {
        if(!currentToken.matches(Token.Type.KEYWORD, "FOR"))
            throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected 'FOR'");

        advance();

        if(currentToken.type() != Token.Type.IDENTIFIER)
            throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected identifier");

        Token varName = currentToken;

        advance();

        if(currentToken.type() != Token.Type.EQUAL)
            throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected '='");

        advance();

        Node startValue = expr();

        if(!currentToken.matches(Token.Type.KEYWORD, "TO"))
            throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected 'TO'");

        advance();

        Node endValue = expr();
        Node stepValue = null;

        if(currentToken.matches(Token.Type.KEYWORD, "STEP")) {
            advance();

            stepValue = expr();
        }

        if(!currentToken.matches(Token.Type.KEYWORD, "THEN"))
            throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected 'THEN'");

        advance();

        Node body;

        if(currentToken.type() == Token.Type.NEWLINE) {
            advance();

            body = statements();

            if(!currentToken.matches(Token.Type.KEYWORD, "END"))
                throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected 'END'");

            advance();
        } else {
            body = expr();
        }

        return new ForNode(varName, startValue, endValue, stepValue, body);
    }

    private Node whileExpr() throws ScriptException {
        if(!currentToken.matches(Token.Type.KEYWORD, "WHILE"))
            throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected 'WHILE'");

        advance();

        Node condition = expr();

        if(!currentToken.matches(Token.Type.KEYWORD, "THEN"))
            throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected 'THEN'");

        advance();

        Node body;

        if(currentToken.type() == Token.Type.NEWLINE) {
            advance();

            body = statements();

            if(!currentToken.matches(Token.Type.KEYWORD, "END"))
                throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected 'END'");

            advance();
        } else {
            body = expr();
        }

        return new WhileNode(condition, body);
    }

    private Node funcDef() throws ScriptException {
        if(!currentToken.matches(Token.Type.KEYWORD, "FUNC"))
            throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected 'FUNC'");

        advance();

        Token varNameToken = null;
        if(currentToken.type() == Token.Type.IDENTIFIER) {
            varNameToken = currentToken;

            advance();

            if(currentToken.type() != Token.Type.LPAREN)
                throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected '('");
        } else {
            if(currentToken.type() != Token.Type.LPAREN)
                throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected identifier or '('");
        }

        advance();

        List<Token> argNameTokens = new ArrayList<>();

        if(currentToken.type() == Token.Type.IDENTIFIER) {
            argNameTokens.add(currentToken);

            advance();

            while(currentToken.type() == Token.Type.COMMA) {
                advance();

                if(currentToken.type() != Token.Type.IDENTIFIER)
                    throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected identifier");

                argNameTokens.add(currentToken);

                advance();
            }

            if(currentToken.type() != Token.Type.RPAREN)
                throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected ',' or ')'");
        } else {
            if(currentToken.type() != Token.Type.RPAREN)
                throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected identifier or ')'");
        }

        advance();

        Node body;

        if(currentToken.type() == Token.Type.ARROW) {
            advance();

            body = expr();
        } else if(currentToken.type() == Token.Type.NEWLINE) {
            advance();

            body = statements();

            if(!currentToken.matches(Token.Type.KEYWORD, "END"))
                throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected 'END'");

            advance();
        } else {
            throw new InvalidSyntaxException(currentToken.posStart(), currentToken.posEnd(), "Expected '->' or NEWLINE");
        }

        return new FuncDefNode(varNameToken, argNameTokens, body);
    }


    private Node binOp(ThrowingSupplier<Node, ScriptException> leftFunc, Predicate<Token> operations, @Nullable ThrowingSupplier<Node, ScriptException> rightFunc) throws ScriptException {
        if(rightFunc == null)
            rightFunc = leftFunc;

        Node left = leftFunc.get();

        while(operations.test(currentToken)) {
            Token operationToken = currentToken;

            advance();

            Node right = rightFunc.get();

            left = new BinOpNode(left, operationToken, right);
        }

        return left;
    }

    private Node binOp(ThrowingSupplier<Node, ScriptException> leftFunc, List<Token.Type> operations, @Nullable ThrowingSupplier<Node, ScriptException> rightFunc) throws ScriptException {
        return this.binOp(leftFunc, (token) -> operations.contains(token.type()), rightFunc);
    }
}
