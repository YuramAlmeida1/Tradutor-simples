package br.ufma.ecp;

public class Command {
    public enum Type {
        ADD, SUB, MUL, DIV, PUSH, POP, PRINT
    }

    public Command.Type type;
    public String arg = "";

    public Command(String[] command) {
        type = Command.Type.valueOf(command[0].toUpperCase());
        if (command.length > 1) {
            arg = command[1];
        }
    }

    @Override
    public String toString() {
        return type.name() + " " + arg;
    }
}