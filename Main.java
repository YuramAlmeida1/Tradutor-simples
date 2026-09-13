package br.ufma.ecp;

public class Main {
    public static void main(String[] args) {
        String input = "let a = 4 + 2 * 3;\n" +
                "let b = 10 - 8 / 2;\n" +
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