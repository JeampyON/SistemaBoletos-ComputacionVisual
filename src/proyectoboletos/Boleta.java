/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoboletos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
/**
 *
 * @author Jean
 */
public class Boleta {
    private int idBoleta;
    private int idEvento;
    private String codigoUnico;
    private String estado;

    public Boleta() {}

    public int getIdBoleta() { return idBoleta; }
    public void setIdBoleta(int idBoleta) { this.idBoleta = idBoleta; }
    public int getIdEvento() { return idEvento; }
    public void setIdEvento(int idEvento) { this.idEvento = idEvento; }
    public String getCodigoUnico() { return codigoUnico; }
    public void setCodigoUnico(String codigoUnico) { this.codigoUnico = codigoUnico; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    
    public static boolean guardarBoletoPNG(String codigoTexto) {
        try {
            String caracteres = "0123456789*";
            String[] patrones = {
                "000110100","100100001","001100001","101100000","000110001",
                "100110000","001110000","000100101","100100100","001100100",
                "010010100"
            };
            String textoCompleto = "*" + codigoTexto + "*";
            BufferedImage img = new BufferedImage(360, 110, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = img.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 360, 110);
            
            int x = 25;
            for (int c = 0; c < textoCompleto.length(); c++) {
                int indice = caracteres.indexOf(textoCompleto.charAt(c));
                if (indice == -1) continue;
                String patron = patrones[indice];
                for (int i = 0; i < patron.length(); i++) {
                    int grosor = (patron.charAt(i) == '1') ? 6 : 2;
                    if (i % 2 == 0) {
                        g.setColor(Color.BLACK);
                        g.fillRect(x, 15, grosor, 65);
                    }
                    x += grosor;
                }
                x += 2;
            }
            g.setColor(Color.BLACK);
            g.setFont(new Font("Monospaced", Font.BOLD, 16));
            g.drawString(textoCompleto, 90, 100);
            g.dispose();

            // Guarda la imagen directamente en la carpeta del proyecto
            javax.imageio.ImageIO.write(img, "png", new java.io.File("boleto_" + codigoTexto + ".png"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    // Método que genera la imagen del código de barras a partir del número
    public static ImageIcon generarImagenCode39(String codigoTexto) {
        String caracteres = "0123456789*";
        String[] patrones = {
            "000110100", "100100001", "001100001", "101100000", "000110001",
            "100110000", "001110000", "000100101", "100100100", "001100100",
            "010010100"  // Patrón para el asterisco *
        };

        String textoCompleto = "*" + codigoTexto + "*";
        int anchoImagen = 360;
        int altoImagen = 110;

        BufferedImage img = new BufferedImage(anchoImagen, altoImagen, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, anchoImagen, altoImagen);

        int x = 25; 
        for (int c = 0; c < textoCompleto.length(); c++) {
            char letra = textoCompleto.charAt(c);
            int indice = caracteres.indexOf(letra);
            if (indice == -1) continue;

            String patron = patrones[indice];
            for (int i = 0; i < patron.length(); i++) {
                int grosor = (patron.charAt(i) == '1') ? 6 : 2; // '1' barra ancha, '0' delgada
                if (i % 2 == 0) { // Posiciones pares son barras negras, impares son espacios blancos
                    g.setColor(Color.BLACK);
                    g.fillRect(x, 15, grosor, 65);
                }
                x += grosor;
            }
            x += 2; 
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Monospaced", Font.BOLD, 16));
        g.drawString(textoCompleto, 90, 100);
        g.dispose();

        return new ImageIcon(img);
    }
}
