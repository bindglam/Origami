package com.bindglam.origami.api.script.node;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.Token;

public record VarAssignNode(Token name, Node value) implements Node {
    @Override
    public Position posStart() {
        return name.posStart();
    }

    @Override
    public Position posEnd() {
        return value.posEnd();
    }
}
