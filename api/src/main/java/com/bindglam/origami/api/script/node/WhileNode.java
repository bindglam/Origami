package com.bindglam.origami.api.script.node;

import com.bindglam.origami.api.script.Position;

public record WhileNode(Node condition, Node body) implements Node {
    @Override
    public Position posStart() {
        return condition.posStart();
    }

    @Override
    public Position posEnd() {
        return body.posEnd();
    }
}
