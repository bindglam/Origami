package com.bindglam.origami.api.script.node;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.Token;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ForNode(Token varNameToken, Node startValue, Node endValue, @Nullable Node stepValue, Node body) implements Node {
    @Override
    public Position posStart() {
        return varNameToken.posStart();
    }

    @Override
    public Position posEnd() {
        return body.posEnd();
    }
}
