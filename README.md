# Trabalho 1 — Tradutor de Aquecimento

Mini-compilador que traduz expressões aritméticas em notação infixada para notação pós-fixa (RPN) e as executa em uma máquina de pilha. Trabalho da disciplina de Compiladores, baseado no Capítulo 2 do livro-texto ("Aquecimento: um simples tradutor").

## 👥 Autor

- **Nome:** Yuram Almeida Santos
- **Curso:** Engenharia da Computação 

## 🎯 Objetivo

Implementar, passo a passo, um tradutor dirigido por sintaxe que:

1. Recebe uma expressão como `let a = 42 + 5; print a + 6;`
2. Traduz para uma sequência de comandos em notação pós-fixa (`push`, `pop`, `add`, `sub`, `mul`, `div`, `print`)
3. Executa essa sequência em um interpretador simples com pilha e mapa de variáveis

## 🏗️ Arquitetura

O projeto está dividido em três camadas, seguindo a separação clássica de responsabilidades de um compilador:
┌─────────────┐ tokens ┌──────────┐ comandos ┌───────────────┐
│ Scanner │ ──────────► │ Parser │ ───────────► │ Interpretador │
│ (análise │ │ (análise │ │ (execução) │
│ léxica) │ │ sintática│ │ │
└─────────────┘ └──────────┘ └───────────────┘


| Classe | Responsabilidade |
|---|---|
| `TokenType` | Enumeração dos tipos de token (NUMBER, IDENT, PLUS, MINUS, MUL, DIV, EQ, SEMICOLON, LET, PRINT, EOF) |
| `Token` | Estrutura que carrega o **tipo** e o **lexema** de cada token |
| `Scanner` | Análise léxica: transforma o fluxo de caracteres em um fluxo de tokens |
| `Parser` | Análise sintática descendente recursiva + tradução dirigida por sintaxe |
| `Command` | Estrutura que representa um comando pós-fixo (`PUSH`, `ADD`, `POP`, ...) |
| `Interpretador` | Executa os comandos sobre uma pilha e um mapa de variáveis |
| `Main` | Ponto de entrada, liga Scanner → Parser → Interpretador |

## 📜 Gramática
statements -> statement*
statement -> printStatement | letStatement
letStatement -> 'let' identifier '=' expression ';'
printStatement -> 'print' expression ';'
expression -> term oper
oper -> + term oper
| - term oper
| * term oper
| / term oper
| ε
term -> number | identifier
number -> [0-9]+
identifier -> [a-zA-Z_][a-zA-Z0-9_]*


> **Observação sobre precedência:** a gramática acima **não implementa precedência de operadores**. Expressões como `4 + 2 * 3` são avaliadas da esquerda para a direita, resultando em `(4+2)*3 = 18`, e não em `4+(2*3) = 10`. Isso é consistente com o Capítulo 2 do livro-texto, cuja extensão pede apenas o suporte aos operadores `*` e `/`, sem exigir precedência.

## ⚙️ Funcionalidades

- ✅ Números com múltiplos dígitos (ex: `42`, `876`)
- ✅ Identificadores (ex: `preco`, `a`, `b`)
- ✅ Palavras reservadas `let` e `print`
- ✅ Operadores `+`, `-`, `*`, `/`
- ✅ Comando de atribuição `let x = ...;`
- ✅ Comando de impressão `print ...;`
- ✅ Ignora espaços em branco, tabs e quebras de linha
- ✅ Erros léxicos e sintáticos com mensagens descritivas

## 🧪 Exemplo de uso

### Entrada (`Main.java`)

```java
String input = "let a = 4 + 2 * 3;\n" +
               "let b = 10 - 8 / 2;\n" +
               "print a + b;";
Saída (comandos pós-fixos gerados pelo Parser)
text
push 4
push 2
add
push 3
mul
pop a
push 10
push 8
sub
push 2
div
pop b
push a
push b
add
print
Saída (execução pelo Interpretador)
text
19
🚀 Como executar
Pré-requisitos
Java 11 ou superior

IntelliJ IDEA (recomendado) ou qualquer IDE/compilador Java

Passos
Clone o repositório:

bash
git clone https://github.com/seu-usuario/tradutor-aquecimento.git
Abra o projeto no IntelliJ IDEA

Edite a string input em Main.java com a expressão desejada

Execute a classe Main

Observe os comandos gerados e o resultado da execução no console

📂 Estrutura do projeto
text
TradutorAquecimento/
├── src/
│   └── br/ufma/ecp/
│       ├── Main.java
│       ├── TokenType.java
│       ├── Token.java
│       ├── Scanner.java
│       ├── Parser.java
│       ├── Command.java
│       └── Interpretador.java
├── .gitignore
└── README.md


 Conceitos praticados
Análise léxica (Scanner)

Tokens e lexemas

Palavras reservadas via tabela de símbolos (Map<String, TokenType>)

Gramática livre de contexto

Eliminação de recursão à esquerda (expr -> term oper)

Análise sintática descendente recursiva

Tradução dirigida por sintaxe

Notação pós-fixa (RPN)

Máquina de pilha (interpretador)


```java
package br.ufma.ecp;

/**
 * Trabalho 1 — Tradutor de Aquecimento (Compiladores)
 *
 * Ponto de entrada do mini-compilador. Este arquivo liga as três camadas
 * do projeto:
 *
 *   1. Scanner        -> análise léxica (chars -> tokens)
 *   2. Parser         -> análise sintática + tradução para pós-fixa
 *   3. Interpretador  -> execução dos comandos em uma máquina de pilha
 *
 * -----------------------------------------------------------------------
 * NOTA SOBRE O DESENVOLVIMENTO:
 * O trabalho foi construído de forma incremental, arquivo por arquivo,
 * seguindo o roteiro do tutorial do Capítulo 2 do livro-texto. Cada
 * arquivo (Token, TokenType, Scanner, Parser, Command, Interpretador)
 * foi adicionado individualmente ao repositório, refletindo a evolução
 * das etapas: 1) análise léxica, 2) análise sintática, 3) tradução,
 * 4) execução.
 * -----------------------------------------------------------------------
 *
 * Exemplo de entrada:
 *     let a = 42 + 2;
 *     let b = 15 + 3;
 *     print a + b;
 *
 * Saída esperada (comandos pós-fixos + execução):
 *     push 42 / push 2 / add / pop a / push 15 / push 3 / add /
 *     pop b / push a / push b / add / print
 *     Resultado: 62
 */
public class Main {
    public static void main(String[] args) {
        String input = "let a = 42 + 2;\n" +
                "let b = 15 + 3;\n" +
                "print a + b;";

        Parser p = new Parser(input.getBytes());
        p.parse();

        System.out.println("--- comandos gerados ---");
        System.out.println(p.output());

        System.out.println("--- execução ---");
        Interpretador i = new Interpretador(p.output());
        i.run();
    }
}
