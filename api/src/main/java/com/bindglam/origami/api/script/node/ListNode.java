package com.bindglam.origami.api.script.node;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.Token;

import java.util.List;

public record ListNode(List<Node> elements, Position posStart, Position posEnd) implements Node {
    @Override
    public Position posStart() {
        return posStart;
    }

    @Override
    public Position posEnd() {
        return posEnd;
    }

    @Override
    public String toString() {
        return "[" + String.join(",", elements.stream().map(Node::toString).toList()) + "]";
    }
}
