package com.bindglam.origami.api.script;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class Position implements Cloneable {
    public static final Position NONE = new Position("<unknown>", "<unknwon>", -1, 0, -1);

    private int idx;
    private int line;
    private int column;
    private final String fn;
    private final String text;

    public Position(String fn, String text, int idx, int line, int column) {
        this.fn = fn;
        this.text = text;
        this.column = column;
        this.line = line;
        this.idx = idx;
    }

    public Position(String fn, String text) {
        this(fn, text, -1, 0, -1);
    }

    public void advance(@Nullable Character currentChar) {
        idx++;
        column++;

        if(Objects.equals(currentChar, '\n')) {
            line++;
            column = 0;
        }
    }

    @Override
    protected Position clone() {
        try {
            return (Position) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getFn() {
        return fn;
    }

    public String getText() {
        return text;
    }
}
