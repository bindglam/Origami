package com.bindglam.origami.api.script.node;

import com.bindglam.origami.api.script.Position;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record IfNode(List<Case> cases, @Nullable Node elseCase) implements Node {
    @Override
    public Position posStart() {
        return cases.getFirst().condition.posStart();
    }

    @Override
    public Position posEnd() {
        if(elseCase != null)
            return elseCase.posEnd();
        return cases.getLast().condition.posEnd();
    }

    public record Case(Node condition, Node expr) {
    }
}
