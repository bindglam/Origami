package com.bindglam.origami.api.script.node;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.Token;

public record UnaryOpNode(Token operationToken, Node node) implements Node {
    @Override
    public Position posStart() {
        return operationToken.posStart();
    }

    @Override
    public Position posEnd() {
        return node.posEnd();
    }

    @Override
    public String toString() {
        return "(" + operationToken.toString() + ", " + node.toString() + ")";
    }
}
