package com.codingShuttle.project.Lovable.enums;

public enum ChatEventType {
    //THIS EVENT SHOWS ""THOUGH FOR 7S"
    THOUGHT,

    //THIS IS USED TO SHOW ACTUAL MESSAGE OF LLM
    MESSAGE,

    //THIS SHOWS FILE EDIT EVENT EX "2files edited " like this
    FILE_EDIT,

    //whenever our llm calls any ai tool it give logs
    TOOL_LOG
}
