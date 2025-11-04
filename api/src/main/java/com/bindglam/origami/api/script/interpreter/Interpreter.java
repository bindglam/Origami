package com.bindglam.origami.api.script.interpreter;

import com.bindglam.origami.api.script.Token;
import com.bindglam.origami.api.script.exceptions.RuntimeException;
import com.bindglam.origami.api.script.exceptions.ScriptException;
import com.bindglam.origami.api.script.interpreter.value.*;
import com.bindglam.origami.api.script.interpreter.value.Comparable;
import com.bindglam.origami.api.script.interpreter.value.Number;
import com.bindglam.origami.api.script.node.*;
import com.bindglam.origami.api.utils.ThrowingBiFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.String;
import java.util.*;
import java.util.List;
import java.util.function.Function;

public final class Interpreter {
    private final Map<Class<? extends Node>, ThrowingBiFunction<Node, Context, Value, ScriptException>> visitFunctions = new HashMap<>() {{
        put(NumberNode.class, (node, context) ->
                new com.bindglam.origami.api.script.interpreter.value.Number((double) Objects.requireNonNull(((NumberNode) node).token().value()),
                        node.posStart(), node.posEnd(), context));

        put(StringNode.class, (node, context) ->
                new com.bindglam.origami.api.script.interpreter.value.String((String) Objects.requireNonNull(((StringNode) node).token().value()),
                        node.posStart(), node.posEnd(), context));

        put(ListNode.class, (node, context) -> {
            ListNode listNode = (ListNode) node;

            List<Value> elements = new ArrayList<>();

            for(Node element : listNode.elements()) {
                elements.add(visit(element, context));
            }

            return new com.bindglam.origami.api.script.interpreter.value.List(elements, listNode.posStart(), listNode.posEnd(), context);
        });

        put(VarAccessNode.class, (node, context) -> {
            VarAccessNode varAccessNode = (VarAccessNode) node;

            String varName = (String) varAccessNode.name().value();
            Value value = context.symbolTable().get(varName);

            if(value == null)
                throw new RuntimeException(node.posStart(), node.posEnd(), "'" + varName + "' is not defined", context);

            return value.setPos(node.posStart(), node.posEnd());
        });

        put(VarAssignNode.class, (node, context) -> {
            VarAssignNode varAssignNode = (VarAssignNode) node;

            String varName = (String) varAssignNode.name().value();
            Value value = visit(varAssignNode.value(), context);

            context.symbolTable().set(varName, value);

            return value;
        });

        put(BinOpNode.class, (node, context) -> {
            BinOpNode binOpNode = (BinOpNode) node;

            Value left = Objects.requireNonNull(visit(binOpNode.leftNode(), context));
            Value right = Objects.requireNonNull(visit(binOpNode.rightNode(), context));

            Value result = switch (binOpNode.operationToken().type()) {
                case PLUS -> {
                    if(left instanceof Addable add)
                        yield add.addedTo(right);
                    yield null;
                }
                case MINUS -> {
                    if(left instanceof Subtractable sub)
                        yield sub.subbedTo(right);
                    yield null;
                }
                case MUL -> {
                    if(left instanceof Multipliable mul)
                        yield mul.multedBy(right);
                    yield null;
                }
                case DIV -> {
                    if(left instanceof Divisible div)
                        yield div.divedBy(right);
                    yield null;
                }
                case POW -> {
                    if(left instanceof Number leftNumber && right instanceof Number rightNumber)
                        yield leftNumber.powedBy(rightNumber);
                    yield null;
                }
                case EQUAL_EQUAL -> left.compareEquals(right);
                case NOT_EQUAL -> left.compareEquals(right).not();
                case LESS_THAN -> {
                    if(left instanceof Comparable comp)
                        comp.compareLessThan(right);
                    yield null;
                }
                case GREATER_THAN -> {
                    if(left instanceof Comparable comp)
                        yield comp.compareGreaterThan(right);
                    yield null;
                }
                case LESS_THAN_EQUAL -> {
                    if(left instanceof Comparable comp)
                        yield comp.compareLessThanEquals(right);
                    yield null;
                }
                case GREATER_THAN_EQUAL -> {
                    if(left instanceof Comparable comp)
                        yield comp.compareGreaterThanEquals(right);
                    yield null;
                }

                default -> null;
            };

            if (binOpNode.operationToken().matches(Token.Type.KEYWORD, "AND")) {
                result = new Number(left.isTrue() && right.isTrue() ? 1.0 : 0.0, left.posStart(), right.posEnd(), context);
            } else if (binOpNode.operationToken().matches(Token.Type.KEYWORD, "OR")) {
                result = new Number(left.isTrue() || right.isTrue() ? 1.0 : 0.0, left.posStart(), right.posEnd(), context);
            }

            if(result == null)
                throw new RuntimeException(binOpNode.posStart(), binOpNode.posEnd(), "Unknown operation", context);

            return result.setPos(binOpNode.posStart(), binOpNode.posEnd());
        });

        put(UnaryOpNode.class, (node, context) -> {
            UnaryOpNode unaryOpNode = (UnaryOpNode) node;

            Value value = visit(unaryOpNode.node(), context);
            if(!(value instanceof Number number))
                throw new RuntimeException(node.posStart(), node.posEnd(), "Unknown operation", context);

            if(unaryOpNode.operationToken().type() == Token.Type.MINUS) {
                number = (Number) number.multedBy(new Number(-1));
            } else if(unaryOpNode.operationToken().matches(Token.Type.KEYWORD, "NOT")) {
                number = number.not();
            }

            return number.setPos(unaryOpNode.posStart(), unaryOpNode.posEnd());
        });

        put(IfNode.class, (node, context) -> {
            IfNode ifNode = (IfNode) node;

            for(IfNode.Case ifCase : ifNode.cases()) {
                Value conditionValue = visit(ifCase.condition(), context);
                if(!(conditionValue instanceof Number condition))
                    throw new RuntimeException(node.posStart(), node.posEnd(), "Expected number", context);

                if(condition.isTrue()) {
                    return visit(ifCase.expr(), context);
                }
            }

            if(ifNode.elseCase() != null) {
                return visit(ifNode.elseCase(), context);
            }

            return null;
        });

        put(ForNode.class, (node, context) -> {
            ForNode forNode = (ForNode) node;

            if(!(visit(forNode.startValue(), context) instanceof Number startValue))
                throw new RuntimeException(node.posStart(), node.posEnd(), "Expected number", context);

            if(!(visit(forNode.endValue(), context) instanceof Number endValue))
                throw new RuntimeException(node.posStart(), node.posEnd(), "Expected number", context);

            Number stepValue = new Number(1.0);
            if(forNode.stepValue() != null) {
                Value value = visit(forNode.stepValue(), context);
                if(!(value instanceof Number))
                    throw new RuntimeException(node.posStart(), node.posEnd(), "Expected number", context);

                stepValue = (Number) value;
            }

            double i = startValue.value();
            Function<Double, Boolean> condition;
            if(stepValue.value() >= 0)
                condition = (j) -> j < endValue.value();
            else
                condition = (j) -> j > endValue.value();

            while(condition.apply(i)) {
                context.symbolTable().set((String) forNode.varNameToken().value(), new Number(i));

                i += stepValue.value();

                visit(forNode.body(), context);
            }

            return null;
        });

        put(WhileNode.class, (node, context) -> {
            WhileNode whileNode = (WhileNode) node;

            while(true) {
                Value conditionValue = visit(whileNode.condition(), context);
                if(!(conditionValue instanceof Number condition))
                    throw new RuntimeException(node.posStart(), node.posEnd(), "Expected number", context);

                if(!condition.isTrue())
                    break;

                visit(whileNode.body(), context);
            }

            return null;
        });

        put(FuncDefNode.class, (node, context) -> {
            FuncDefNode funcDefNode = (FuncDefNode) node;

            String funcName = null;
            if(funcDefNode.varName() != null)
                funcName = (String) funcDefNode.varName().value();
            Node bodyNode = funcDefNode.body();
            List<String> argNames = funcDefNode.argNames().stream().map((tok) -> (String) tok.value()).toList();

            Value funcValue = new com.bindglam.origami.api.script.interpreter.value.Function(funcName, bodyNode, argNames, funcDefNode.posStart(), funcDefNode.posEnd(), context);

            if(funcDefNode.varName() != null)
                context.symbolTable().set(funcName, funcValue);

            return funcValue;
        });

        put(CallNode.class, (node, context) -> {
            CallNode callNode = (CallNode) node;

            List<Value> args = new ArrayList<>();

            AbstractFunction valueToCall = (AbstractFunction) visit(callNode.toCall(), context);
            if(valueToCall == null)
                throw new RuntimeException(callNode.posStart(), callNode.posEnd(), "not defined", context);
            valueToCall = (AbstractFunction) valueToCall.setPos(callNode.posStart(), callNode.posEnd()).setContext(context);

            for(Node argNode : callNode.args()) {
                args.add(visit(argNode, context));
            }

            return valueToCall.execute(args);
        });
    }};

    public @Nullable Value visit(@NotNull Node node, Context context) throws ScriptException {
        for (Map.Entry<Class<? extends Node>, ThrowingBiFunction<Node, Context, Value, ScriptException>> entry : visitFunctions.entrySet()) {
            if(!entry.getKey().isInstance(node))
                continue;

            return entry.getValue().apply(node, context);
        }

        throw new RuntimeException(node.posStart(), node.posEnd(), "No visit method defined", context);
    }
}
