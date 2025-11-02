package com.bindglam.origami.api.script.node;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.Token;

public record BinOpNode(Node leftNode, Token operationToken, Node rightNode) implements Node {
    @Override
    public Position posStart() {
        return leftNode.posStart();
    }

    @Override
    public Position posEnd() {
        return rightNode.posEnd();
    }

    @Override
    public String toString() {
        return "(" + leftNode.toString() + ", " + operationToken.toString() + ", " + rightNode.toString() + ")";
    }
}
