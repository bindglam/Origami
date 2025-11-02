package com.bindglam.origami.api.script.node;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.Token;

public record StringNode(Token token) implements Node {
    @Override
    public Position posStart() {
        return token.posStart();
    }

    @Override
    public Position posEnd() {
        return token.posEnd();
    }

    @Override
    public String toString() {
        return token.toString();
    }
}
