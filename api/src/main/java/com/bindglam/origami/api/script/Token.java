package com.bindglam.origami.api.script;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record Token(Type type, @Nullable Object value, @Nullable Position posStart, @Nullable Position posEnd) {
    public Token {
        if(posStart != null) {
            posStart = posStart.clone();
            posEnd = posStart.clone();
            posEnd.advance(null);
        }

        if(posEnd != null) {
            posEnd = posEnd.clone();
        }
    }

    public boolean matches(Type type, Object value) {
        return this.type == type && Objects.equals(this.value, value);
    }

    @Override
    public String toString() {
        if(value != null)
            return type.name() + ":" + value;
        return type.name();
    }

    public enum Type {
        NUMBER,
        STRING,

        IDENTIFIER,
        KEYWORD,

        PLUS,
        MINUS,
        MUL,
        DIV,
        POW,

        LPAREN,
        RPAREN,
        EQUAL,

        EQUAL_EQUAL,
        NOT_EQUAL,
        LESS_THAN,
        GREATER_THAN,
        LESS_THAN_EQUAL,
        GREATER_THAN_EQUAL,

        COMMA,
        ARROW,

        NEWLINE,
        EOF;
    }
}
