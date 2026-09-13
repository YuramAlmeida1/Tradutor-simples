package br.ufma.ecp;

public class Parser {
    private Scanner scan;
    private Token currentToken;
    private StringBuilder output = new StringBuilder();

    public Parser(byte[] input) {
        scan = new Scanner(input);
        currentToken = scan.nextToken();
    }

    public String output() {
        return output.toString();
    }

    private void emit(String cmd) {
        output.append(cmd).append(System.lineSeparator());
    }

    private void nextToken() {
        currentToken = scan.nextToken();
    }

    private void match(TokenType t) {
        if (currentToken.type == t) {
            nextToken();
        } else {
            throw new Error("syntax error: expected " + t + " but found " + currentToken.type);
        }
    }

    void number() {
        emit("push " + currentToken.lexeme);
        match(TokenType.NUMBER);
    }

    void term() {
        if (currentToken.type == TokenType.NUMBER) {
            number();
        } else if (currentToken.type == TokenType.IDENT) {
            emit("push " + currentToken.lexeme);
            match(TokenType.IDENT);
        } else {
            throw new Error("syntax error in term");
        }
    }

    void oper() {
        if (currentToken.type == TokenType.PLUS) {
            match(TokenType.PLUS);
            term();
            emit("add");
            oper();
        } else if (currentToken.type == TokenType.MINUS) {
            match(TokenType.MINUS);
            term();
            emit("sub");
            oper();
        } else if (currentToken.type == TokenType.MUL) {
            match(TokenType.MUL);
            term();
            emit("mul");
            oper();
        } else if (currentToken.type == TokenType.DIV) {
            match(TokenType.DIV);
            term();
            emit("div");
            oper();
        }
    }

    void expr() {
        term();
        oper();
    }

    void letStatement() {
        match(TokenType.LET);
        String id = currentToken.lexeme;
        match(TokenType.IDENT);
        match(TokenType.EQ);
        expr();
        emit("pop " + id);
        match(TokenType.SEMICOLON);
    }

    void printStatement() {
        match(TokenType.PRINT);
        expr();
        emit("print");
        match(TokenType.SEMICOLON);
    }

    void statement() {
        if (currentToken.type == TokenType.LET) {
            letStatement();
        } else if (currentToken.type == TokenType.PRINT) {
            printStatement();
        } else {
            throw new Error("syntax error in statement");
        }
    }

    void statements() {
        while (currentToken.type != TokenType.EOF) {
            statement();
        }
    }

    public void parse() {
        statements();
    }
}