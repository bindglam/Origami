package com.bindglam.origami.api.script.node;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.Token;

public record VarAccessNode(Token name) implements Node {
    @Override
    public Position posStart() {
        return name.posStart();
    }

    @Override
    public Position posEnd() {
        return name.posEnd();
    }
}
