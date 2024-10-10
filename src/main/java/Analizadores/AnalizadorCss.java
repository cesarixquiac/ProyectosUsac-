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
public class AnalizadorCss {

  public List<Token> analizar(String codigoCSS) {
    List<Token> tokens = new ArrayList<>();
    String[] lineas = codigoCSS.split("\\n");
    int fila = 0;
    
    for (String linea : lineas) {
        fila++;
        linea = linea.trim();

        // Verificar si la línea contiene un selector y una llave de apertura
        if (linea.endsWith("{")) {
            String selector = linea.substring(0, linea.indexOf("{")).trim();
            if (!selector.isEmpty()) {
                tokens.add(new Token("Selector", selector, fila, linea.indexOf(selector), "CSS"));
            }
            tokens.add(new Token("Llave de Apertura", "{", fila, linea.indexOf("{"), "CSS"));
            linea = linea.replace("{", "").trim();  // Remover la llave para continuar procesando
        }
        
        // Detectar la llave de cierre }
        if (linea.endsWith("}")) {
            tokens.add(new Token("Llave de Cierre", "}", fila, linea.indexOf("}"), "CSS"));
            linea = linea.replace("}", "").trim();  // Remover la llave para continuar procesando
        }

        // Detectar propiedades con valores
        if (linea.contains(":")) {
            String[] partes = linea.split(":");
            String propiedad = partes[0].trim();
            String valor = partes[1].trim().replace(";", "");

            // Añadir tokens de propiedad y valor
            tokens.add(new Token("Propiedad", propiedad, fila, linea.indexOf(propiedad), "CSS"));
            tokens.add(new Token("Dos Puntos", ":", fila, linea.indexOf(":"), "CSS"));
            tokens.add(new Token("Valor", valor, fila, linea.indexOf(valor), "CSS"));

            // Detectar el punto y coma si está presente
            if (linea.endsWith(";")) {
                tokens.add(new Token("Punto y Coma", ";", fila, linea.indexOf(";"), "CSS"));
            }
        }
    }

    return tokens;
}

    // Método para procesar una propiedad y su valor
    private void procesarPropiedadYValor(String linea, List<Token> listaDeTokens, int fila) {
        // Dividimos la propiedad del valor usando ':'
        int indiceDosPuntos = linea.indexOf(':');
        String propiedad = linea.substring(0, indiceDosPuntos).trim();
        String valor = linea.substring(indiceDosPuntos + 1, linea.length()).replace(";", "").trim();

        // Creamos tokens para la propiedad y su valor, asignando el lenguaje CSS
        listaDeTokens.add(new Token("Propiedad", propiedad, fila, 0, "CSS"));
        listaDeTokens.add(new Token("Valor", valor, fila, 0, "CSS"));
    }

  
}