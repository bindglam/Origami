package com.bindglam.origami.api.script.node;

import com.bindglam.origami.api.script.Position;

import java.util.List;

public record CallNode(Node toCall, List<Node> args) implements Node {
    @Override
    public Position posStart() {
        return toCall.posStart();
    }

    @Override
    public Position posEnd() {
        if(!args.isEmpty())
            return args.getLast().posEnd();
        return toCall.posEnd();
    }
}
