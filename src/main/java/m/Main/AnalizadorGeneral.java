/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package m.Main;

import Analizadores.AnalizadorCss;
import Analizadores.AnalizadorHtml;
import Analizadores.AnalizadorJavaScript;
import Token.Token;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cesar
 */
public class AnalizadorGeneral {
    private AnalizadorHtml analizadorHtml;
    private AnalizadorCss analizadorCss;
    private AnalizadorJavaScript analizadorJs;
    private String estadoActual;  // HTML, CSS o JS

    public AnalizadorGeneral() {
        analizadorHtml = new AnalizadorHtml();
        analizadorCss = new AnalizadorCss();
        analizadorJs = new AnalizadorJavaScript();
        estadoActual = "JS";  // Estado inicial por defecto
    }

    public List<Token> analizar(String codigoFuente) {
        List<Token> todosLosTokens = new ArrayList<>();

        // Convertir el código en líneas
        String[] lineas = codigoFuente.split("\n");

        for (String linea : lineas) {
            // Verificar si la línea contiene un token de estado
            if (esTokenDeEstado(linea.trim())) {
                cambiarEstado(linea.trim());
                continue;  // No analizamos la línea si es un token de estado
            }

            // Según el estado actual, usamos el analizador correspondiente
            if (estadoActual.equals("HTML")) {
                todosLosTokens.addAll(analizadorHtml.analizar(linea));
            } else if (estadoActual.equals("CSS")) {
                todosLosTokens.addAll(analizadorCss.analizar(linea));
            } else if (estadoActual.equals("JS")) {
                todosLosTokens.addAll(analizadorJs.analizar(linea));
            }
        }

        return todosLosTokens;  // Retorna todos los tokens generados
    }

    // Método para verificar si una línea es un token de estado
    private boolean esTokenDeEstado(String linea) {
        return linea.equals(">>[html]") || linea.equals(">>[css]") || linea.equals(">>[js]");
    }

    // Método para cambiar el estado actual según el token de estado encontrado
    private void cambiarEstado(String token) {
        switch (token) {
            case ">>[html]":
                estadoActual = "HTML";
                break;
            case ">>[css]":
                estadoActual = "CSS";
                break;
            case ">>[js]":
                estadoActual = "JS";
                break;
        }
    }
}