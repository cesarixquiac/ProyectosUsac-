/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Token;

/**
 *
 * @author cesar
 */
public class Token {
    private String tipo;    // Tipo de token ("Etiqueta", "Palabra Reservada")
    private String valor;   // Valor del token ("<div>", "if")
    private int fila;       // Fila donde fue encontrado el token
    private int columna;    // Columna donde fue encontrado el token
    private String lenguaje; // Lenguaje del token (HTML, CSS, JS)

    public Token(String tipo, String valor, int fila, int columna, String lenguaje) {
        this.tipo = tipo;
        this.valor = valor;
        this.fila = fila;
        this.columna = columna;
        this.lenguaje = lenguaje;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    
}
