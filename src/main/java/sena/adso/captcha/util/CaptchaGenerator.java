package sena.adso.captcha.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 * Clase utilitaria para generar CAPTCHA personalizado
 */
public class CaptchaGenerator {
    
    private static final String CARACTERES = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
    private static final int LONGITUD = 6;
    private static final int ANCHO = 150;
    private static final int ALTO = 50;
    
    /**
     * Genera un texto aleatorio para el CAPTCHA
     * @return Texto aleatorio
     */
    public static String generarTextoCaptcha() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(LONGITUD);
        
        for (int i = 0; i < LONGITUD; i++) {
            int indice = random.nextInt(CARACTERES.length());
            sb.append(CARACTERES.charAt(indice));
        }
        
        return sb.toString();
    }
    
    /**
     * Genera la imagen del CAPTCHA en base64 para mostrar en un img tag
     * @param texto Texto a mostrar en el CAPTCHA
     * @return String con la imagen en formato base64
     */
    public static String generarImagenCaptcha(String texto) {
        BufferedImage imagen = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = imagen.createGraphics();
        
        // Fondo
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, ANCHO, ALTO);
        
        // Líneas aleatorias para confundir
        Random random = new Random();
        g2d.setColor(new Color(220, 220, 220));
        for (int i = 0; i < 20; i++) {
            int x1 = random.nextInt(ANCHO);
            int y1 = random.nextInt(ALTO);
            int x2 = random.nextInt(ANCHO);
            int y2 = random.nextInt(ALTO);
            g2d.drawLine(x1, y1, x2, y2);
        }
        
        // Dibuja el texto
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        for (int i = 0; i < texto.length(); i++) {
            // Color aleatorio para cada carácter
            g2d.setColor(new Color(
                random.nextInt(100), 
                random.nextInt(100), 
                random.nextInt(100)
            ));
            
            // Posición ligeramente aleatoria
            int x = 15 + i * 20 + random.nextInt(5);
            int y = 35 + random.nextInt(5) - random.nextInt(5);
            
            // Rotación aleatoria
            double rotacion = (random.nextDouble() - 0.5) * 0.5;
            g2d.rotate(rotacion, x, y);
            g2d.drawString(texto.charAt(i) + "", x, y);
            g2d.rotate(-rotacion, x, y);
        }
        
        g2d.dispose();
        
        // Convertir a base64
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(imagen, "png", baos);
            byte[] imageBytes = baos.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    /**
     * Valida si el texto ingresado coincide con el CAPTCHA
     * @param textoIngresado Texto ingresado por el usuario
     * @param textoReal Texto real del CAPTCHA
     * @return true si coinciden
     */
    public static boolean validarCaptcha(String textoIngresado, String textoReal) {
        return textoIngresado != null && textoReal != null && textoIngresado.equals(textoReal);
    }
}
