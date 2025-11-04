package com.bindglam.origami.api.manager;

import com.bindglam.origami.api.script.Script;

import java.util.Optional;

public interface ScriptManager extends ManagerBase {
    void compileAll();

    Optional<Script> getScript(String id);
}
