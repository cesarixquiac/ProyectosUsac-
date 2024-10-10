/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Analizadores;
import Token.Token;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author cesar
 */

public class AnalizadorHtml {

    private List<String> operadoresAritmeticos = Arrays.asList("+", "-", "*", "/", "Mod", "^");
    
     public List<Token> analizar(String codigoFuente) {
        List<Token> listaDeTokens = new ArrayList<>();

        try (BufferedReader lector = new BufferedReader(new StringReader(codigoFuente))) {
            String linea;
            int fila = 1;  // Para llevar la cuenta de la línea en el archivo

            while ((linea = lector.readLine()) != null) {
                // Si la línea no está vacía, intentamos analizarla
                if (!linea.trim().isEmpty()) {
                    // Procesamos la línea carácter por carácter para separar etiquetas y texto
                    procesarLinea(linea.trim(), listaDeTokens, fila);
                }
                fila++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaDeTokens;
    }

    
   // Método para procesar una línea y separar etiquetas del texto
private void procesarLinea(String linea, List<Token> listaDeTokens, int fila) {
    StringBuilder tokenActual = new StringBuilder();
    boolean dentroDeEtiqueta = false;

    for (int i = 0; i < linea.length(); i++) {
        char c = linea.charAt(i);

        if (c == '<') {
            if (tokenActual.length() > 0 && !dentroDeEtiqueta) {
                listaDeTokens.add(new Token("Texto", tokenActual.toString().trim(), fila, 0, "HTML"));
                System.out.println("Línea válida HTML (Texto): " + tokenActual.toString().trim());
                tokenActual.setLength(0);  // Limpiamos el buffer
            }
            dentroDeEtiqueta = true;
            tokenActual.append(c);
        } else if (c == '>') {
            tokenActual.append(c);
            dentroDeEtiqueta = false;

            procesarEtiquetaConAtributos(tokenActual.toString().trim(), listaDeTokens, fila);
            tokenActual.setLength(0);
        } else {
            tokenActual.append(c);
        }
    }

    if (tokenActual.length() > 0 && !dentroDeEtiqueta) {
        listaDeTokens.add(new Token("Texto", tokenActual.toString().trim(), fila, 0, "HTML"));
    }
}

    private void procesarEtiquetaConAtributos(String etiqueta, List<Token> listaDeTokens, int fila) {
    int espacio = etiqueta.indexOf(' ');
    boolean esAutoCerrada = etiqueta.endsWith("/>");
    
    if (espacio == -1) {
        // No hay atributos, es una etiqueta simple
        listaDeTokens.add(new Token("EtiquetaHTML", traducirEtiqueta(etiqueta), fila, 0, "HTML"));
        System.out.println("Línea válida HTML: " +traducirEtiqueta(etiqueta).trim());
        
    } else {
        // Extraemos el nombre de la etiqueta y traducimos la etiqueta principal
        String nombreEtiqueta = etiqueta.substring(0, espacio);
        String etiquetaTraducida = traducirEtiqueta(nombreEtiqueta);
        
        // Procesamos los atributos
        String atributos = etiqueta.substring(espacio + 1, etiqueta.length() - (esAutoCerrada ? 2 : 1));  // Quitar '>' o '/>'
        StringBuilder atributosTraducidos = procesarAtributos(atributos, fila);

        // Construimos la etiqueta traducida con los atributos
        String etiquetaCompleta = etiquetaTraducida + " " + atributosTraducidos.toString().trim() + (esAutoCerrada ? " />" : ">");
        listaDeTokens.add(new Token("EtiquetaHTML", etiquetaCompleta, fila, 0, "HTML"));

        System.out.println("Etiqueta traducida con atributos: " + etiquetaCompleta);
    }
}

// Método para procesar los atributos 
private StringBuilder procesarAtributos(String atributos, int fila) {
    StringBuilder atributosTraducidos = new StringBuilder();
    StringBuilder nombreAtributo = new StringBuilder();
    StringBuilder valorAtributo = new StringBuilder();
    boolean dentroDeValor = false;
    boolean dentroDeComillas = false;
    char comillaActual = '\0';

    for (int i = 0; i < atributos.length(); i++) {
        char c = atributos.charAt(i);

        if (!dentroDeValor && (c == '=')) {
            dentroDeValor = true;
        } else if (dentroDeValor && (c == '"' || c == '\'')) {
            if (!dentroDeComillas) {
                dentroDeComillas = true;
                comillaActual = c;
            } else if (c == comillaActual) {
                dentroDeComillas = false;
                dentroDeValor = false;

                // Añadimos el atributo completo (nombre="valor")
                atributosTraducidos.append(nombreAtributo).append("=\"").append(valorAtributo).append("\" ");

                // Limpiamos los buffers para el siguiente atributo
                nombreAtributo.setLength(0);
                valorAtributo.setLength(0);
            }
        } else if (dentroDeValor) {
            valorAtributo.append(c);
        } else if (!Character.isWhitespace(c)) {
            nombreAtributo.append(c);
        }
    }

    return atributosTraducidos;
}





    // Método auxiliar para verificar si una línea es una etiqueta HTML válida o personalizada
    private boolean esEtiquetaValida(String linea) {
        // Un chequeo básico para ver si la línea comienza con < y termina con >.
        // Se podría expandir para verificar si es una etiqueta personalizada.
        return linea.startsWith("<") && linea.endsWith(">");
    }

    // Método para traducir etiquetas personalizadas a etiquetas HTML estándar
    private String traducirEtiqueta(String etiqueta) {
        // Traducciones de etiquetas personalizadas
        switch (etiqueta) {
            case "<principal>":
                return "<main>";
            case "<principal/>":
                return "</main>";
            case "<encabezado>":
                return "<header>";
            case "<encabezado/>":
                return "</header>";
            case "<navegacion>":
                return "<nav>";
            case "<navegacion/>":
                return "</nav>"; 
            case "<apartado>":
                return "<aside>";
            case "<apartado/>":
                return "</aside>";
            case "<listaordenada>":
                return "<ul>";
            case "<listaordenada/>":
                return "</ul>";
            case "<listadesordenada>":
                return "<ol>";
            case "<listadesordenada/>":
                return "</ol>";
            case "<itemlista>":
                return "<li>";
            case "<itemlista/>":
                return "</li>";
            case "<anclaje>":
                return "<a>";
            case "<anclaje/>":
                return "</a>";
            case "<contenedor":
                return "<div";
            case "<contenedor>":
                return "<div>";
            case "<contenedor/>":
                return "</div>";
            case "<seccion>":
                return "<section>";
            case "<seccion/>":
                return "</section>";
            case "<articulo>":
                return "<article>";
            case "<articulo/>":
                return "</article>";
            case "<titulo1>":
                return "<h1>";
            case "<titulo1/>":
                return "</h1>";
            case "<titulo2>":
                return "<h2>";
            case "<titulo2/>":
                return "</h2>";
            case "<titulo3>":
                return "<h3>";
            case "<titulo3/>":
                return "</h3>";
            case "<titulo4>":
                return "<h4>";
            case "<titulo4/>":
                return "</h4>";
            case "<titulo5>":
                return "<h5>";
            case "<titulo5/>":
                return "</h5>";
            case "<titulo6>":
                return "<h6>";
            case "<titulo6/>":
                return "</h6>";
            case "<titulo1":
                return "<h1";
            case "<titulo2":
                return "<h2";
            case "<titulo3":
                return "<h3";
            case "<titulo4":
                return "<h4";
            case "<titulo5":
                return "<h5";
            case "<titulo6":
                return "<h6";
            case "<parrafo>":
                return "<p>";
            case "<parrafo/>":
                return "</p>";
            case "<span>":
                return "<span>";
            case "<span/>":
                return "</span>";
            case "<entrada":
                return "<input";
              case "<entrada>":
                return "<input>";
             case "<formulario>":
                return "<form>";
            case "<formulario/>":
                return "</form>";
            case "<label>":
                return "<label>";
            case "<label/>":
                return "</label>";
            case "<area":
                return "<textarea";
            case "<area/>":
                return "<textarea/>";
            case "<boton>":
                return "<button>";
            case "<boton/>":
                return "</button>";
            case "<piepagina>":
                return "<footer>";
            case "<piepagina/>":
                return "</footer>";
            
            // Puedes agregar más casos para otras etiquetas personalizadas
            default:
                return etiqueta; // Si no es personalizada, devuelves la etiqueta sin cambios
        }
    }
}