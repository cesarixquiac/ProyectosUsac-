/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Analizadores;

import Token.Token;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cesar
 */
public class AnalizadorJavaScript {

    private List<Token> tokens;  // Lista de tokens

    public AnalizadorJavaScript() {
        tokens = new ArrayList<>();
    }

    // Método principal que recibe el código fuente y lo analiza
    public List<Token> analizar(String codigoFuente) {
        tokens.clear(); // Limpiar tokens anteriores
        int fila = 1;
        int columna = 1;

        // Convertir el código en un array de caracteres
        char[] caracteres = codigoFuente.toCharArray();
        StringBuilder valorActual = new StringBuilder();

        for (int i = 0; i < caracteres.length; i++) {
            char actual = caracteres[i];

            // Ignorar espacios en blanco
            if (Character.isWhitespace(actual) && actual != '\n') {
                columna++;
                continue;
            }
            
             // Detectar punto y coma
                if (actual == ';') {
                    tokens.add(new Token("Punto y Coma", String.valueOf(actual), fila, columna, "JavaScript"));
                    columna++;
                    continue;
                }
                    // Detectar saltos de línea
            if (actual == '\n') {
                tokens.add(new Token("Salto de Línea", "\n", fila, columna, "JavaScript"));  // Agregar un salto de línea real
                fila++;
                columna = 1;  // Reiniciar la columna para la nueva línea
                continue;
}

            // Identificar comentarios (de una sola línea)
            if (actual == '/' && i + 1 < caracteres.length && caracteres[i + 1] == '/') {
                valorActual.append("//");
                i += 2;  // Saltar los dos caracteres "//"
                
                // Capturar el resto del comentario
                while (i < caracteres.length && caracteres[i] != '\n') {
                    valorActual.append(caracteres[i++]);
                }

                tokens.add(new Token("Comentario", valorActual.toString(), fila, columna, "JavaScript"));
                columna += valorActual.length();
                valorActual.setLength(0);  // Limpiar el valor actual para la próxima iteración
                continue;
            }

            // Identificar operadores aritméticos y otros símbolos
            if (esOperadorSimple(actual)) {
                tokens.add(new Token("Operador", String.valueOf(actual), fila, columna, "JavaScript"));
                columna++;
                continue;
            }

            // Identificar operadores compuestos como "==" o "&&"
            if (i < caracteres.length - 1) {
                char siguiente = caracteres[i + 1];
                String compuesto = String.valueOf(actual) + siguiente;

                if (esOperadorCompuesto(compuesto)) {
                    tokens.add(new Token("Operador", compuesto, fila, columna, "JavaScript"));
                    i++;  // Avanzar el índice para saltar el segundo carácter del operador
                    columna += 2;
                    continue;
                }
            }

            // Identificar identificadores o palabras reservadas
            if (Character.isLetter(actual)) {
                valorActual.append(actual);
                while (i + 1 < caracteres.length && (Character.isLetterOrDigit(caracteres[i + 1]) || caracteres[i + 1] == '_')) {
                    valorActual.append(caracteres[++i]);
                }

                String valorIdentificador = valorActual.toString();
                if (esPalabraReservada(valorIdentificador)) {
                    tokens.add(new Token("Palabra Reservada", valorIdentificador, fila, columna, "JavaScript"));
                } else {
                    tokens.add(new Token("Identificador", valorIdentificador, fila, columna, "JavaScript"));
                }
                columna += valorActual.length();
                valorActual.setLength(0);  // Limpiar el valor actual para la próxima iteración
                continue;
            }

            // Identificar números (Enteros o Decimales)
            if (Character.isDigit(actual)) {
                valorActual.append(actual);
                boolean esDecimal = false;

                while (i + 1 < caracteres.length && (Character.isDigit(caracteres[i + 1]) || (!esDecimal && caracteres[i + 1] == '.'))) {
                    if (caracteres[i + 1] == '.') {
                        esDecimal = true;
                    }
                    valorActual.append(caracteres[++i]);
                }

                tokens.add(new Token(esDecimal ? "Decimal" : "Entero", valorActual.toString(), fila, columna, "JavaScript"));
                columna += valorActual.length();
                valorActual.setLength(0);  // Limpiar el valor actual
                continue;
            }

            // Identificar cadenas de texto
            if (actual == '"' || actual == '\'' || actual == '`') {
                char comilla = actual;
                valorActual.append(comilla);
                i++;  // Avanzar el índice para saltar la comilla de apertura

                while (i < caracteres.length && caracteres[i] != comilla) {
                    valorActual.append(caracteres[i++]);
                }

                valorActual.append(comilla);  // Añadir la comilla de cierre
                tokens.add(new Token("Cadena", valorActual.toString(), fila, columna, "JavaScript"));
                columna += valorActual.length();
                valorActual.setLength(0);  // Limpiar el valor actual
                continue;
            }
        }

        return tokens;  // Devolver la lista de tokens encontrados
    }

    // Métodos auxiliares para verificar los tipos de tokens
    private boolean esOperadorSimple(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '=' || c == '<' || c == '>' || c == '!' || c == ';' || c == ',' || c == '.' || c == ':' || c == '(' || c == ')' || c == '{' || c == '}' || c == '[' || c == ']';
    }

    private boolean esOperadorCompuesto(String op) {
        return op.equals("==") || op.equals("!=") || op.equals("<=") || op.equals(">=") || op.equals("||") || op.equals("&&") || op.equals("++") || op.equals("--");
    }

    private boolean esPalabraReservada(String palabra) {
        String[] palabrasReservadas = {
            "function", "const", "let", "document", "event", "alert", "for", "while", "if", "else", "return", "console.log", "null", "true", "false"
        };
        for (String reservada : palabrasReservadas) {
            if (palabra.equals(reservada)) {
                return true;
            }
        }
        return false;
    }
}