package com.bindglam.origami.api.script.node;

import com.bindglam.origami.api.script.Position;
import com.bindglam.origami.api.script.Token;

import java.util.List;

public record FuncDefNode(Token varName, List<Token> argNames, Node body) implements Node {
    @Override
    public Position posStart() {
        if(varName != null)
            return varName.posStart();
        else if(!argNames.isEmpty())
            return argNames.getFirst().posStart();
        return body.posStart();
    }

    @Override
    public Position posEnd() {
        return body.posEnd();
    }
}
