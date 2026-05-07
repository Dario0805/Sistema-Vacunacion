package sena.adso.captcha.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import sena.adso.captcha.dto.Paciente;
import sena.adso.captcha.dto.RegistroVacunacion;

/**
 * Clase utilitaria para generar certificados PDF de vacunación
 */
public class PDFGenerator {
    
    private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
    private static final Font SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
    private static final Font TEXTO_NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 12);
    private static final Font TEXTO_NEGRITA = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
    private static final Font TEXTO_PEQUEÑO = FontFactory.getFont(FontFactory.HELVETICA, 10);
    
    /**
     * Genera un certificado de vacunación en PDF
     * @param paciente Datos del paciente
     * @param registros Lista de registros de vacunación
     * @return Arreglo de bytes con el contenido del PDF
     * @throws DocumentException Si hay error al generar el PDF
     * @throws IOException Si hay error al cargar el logo
     */
    public static byte[] generarCertificadoVacunacion(Paciente paciente, List<RegistroVacunacion> registros) 
            throws DocumentException, IOException {
        
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        
        document.open();
        
        // Agregar logo (si existe)
        try {
            // Intentar cargar el logo desde varias ubicaciones posibles
            java.net.URL logoUrl = null;
            
            // Intentamos con el nombre exacto proporcionado por el usuario
            logoUrl = PDFGenerator.class.getClassLoader().getResource("logovacunacion.png");
            
            if (logoUrl == null) {
                // Intentos alternativos con otros nombres comunes
                logoUrl = PDFGenerator.class.getClassLoader().getResource("logo.png");
            }
            
            if (logoUrl == null) {
                logoUrl = PDFGenerator.class.getClassLoader().getResource("logo_institucion.png");
            }
            
            if (logoUrl != null) {
                // Si encontramos el logo, lo agregamos al documento
                Image logo = Image.getInstance(logoUrl);
                logo.scaleToFit(150, 150);
                logo.setAlignment(Element.ALIGN_CENTER);
                document.add(logo);
                System.out.println("Logo cargado exitosamente desde: " + logoUrl);
            } else {
                // Si no se encontró el logo, continuamos sin él
                System.out.println("No se encontró el archivo del logo en los recursos");
            }
        } catch (Exception e) {
            // Si hay un error al cargar el logo, simplemente lo omitimos
            System.out.println("No se pudo cargar el logo: " + e.getMessage());
        }
        
        // Título del documento
        Paragraph titulo = new Paragraph("CERTIFICADO DE VACUNACIÓN", TITULO);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingBefore(15);
        titulo.setSpacingAfter(15);
        document.add(titulo);
        
        // Información del paciente
        document.add(new Paragraph("INFORMACIÓN DEL PACIENTE", SUBTITULO));
        document.add(new Paragraph("Nombre: " + paciente.getNombreCompleto(), TEXTO_NORMAL));
        document.add(new Paragraph("Documento de Identidad: " + paciente.getDocumento(), TEXTO_NORMAL));
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        document.add(new Paragraph("Fecha de Nacimiento: " + sdf.format(paciente.getFechaNacimiento()), TEXTO_NORMAL));
        document.add(new Paragraph(" "));
        
        // Tabla de vacunas
        document.add(new Paragraph("HISTORIAL DE VACUNACIÓN", SUBTITULO));
        document.add(new Paragraph(" "));
        
        PdfPTable tabla = new PdfPTable(5); // 5 columnas
        tabla.setWidthPercentage(100); // Usar 100% del ancho disponible
        
        // Encabezados de la tabla
        agregarCeldaEncabezado(tabla, "VACUNA");
        agregarCeldaEncabezado(tabla, "LOTE");
        agregarCeldaEncabezado(tabla, "LABORATORIO");
        agregarCeldaEncabezado(tabla, "FECHA");
        agregarCeldaEncabezado(tabla, "LUGAR");
        
        // Datos de las vacunas
        for (RegistroVacunacion registro : registros) {
            agregarCeldaContenido(tabla, registro.getNombreVacuna());
            agregarCeldaContenido(tabla, registro.getLoteVacuna());
            agregarCeldaContenido(tabla, registro.getLaboratorio());
            agregarCeldaContenido(tabla, sdf.format(registro.getFechaVacunacion()));
            agregarCeldaContenido(tabla, registro.getLugarVacunacion());
        }
        
        document.add(tabla);
        
        // Pie de página
        document.add(new Paragraph(" "));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Este certificado ha sido generado electrónicamente y es válido como comprobante de vacunación.", TEXTO_PEQUEÑO));
        document.add(new Paragraph("Fecha de generación: " + sdf.format(new java.util.Date()), TEXTO_PEQUEÑO));
        
        document.close();
        
        return baos.toByteArray();
    }
    
    /**
     * Agrega una celda de encabezado a la tabla
     * @param tabla Tabla a la que se agrega la celda
     * @param texto Texto de la celda
     */
    private static void agregarCeldaEncabezado(PdfPTable tabla, String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, TEXTO_NEGRITA));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(5);
        tabla.addCell(celda);
    }
    
    /**
     * Agrega una celda de contenido a la tabla
     * @param tabla Tabla a la que se agrega la celda
     * @param texto Texto de la celda
     */
    private static void agregarCeldaContenido(PdfPTable tabla, String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, TEXTO_NORMAL));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setPadding(5);
        tabla.addCell(celda);
    }
}
