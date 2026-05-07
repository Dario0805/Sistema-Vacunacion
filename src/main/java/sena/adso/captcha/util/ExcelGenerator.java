package sena.adso.captcha.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sena.adso.captcha.dto.RegistroVacunacion;

/**
 * Utilidad para generar archivos Excel
 */
public class ExcelGenerator {
    
    /**
     * Genera un archivo Excel con registros de vacunación
     * @param registros Lista de registros a incluir
     * @return Arreglo de bytes con el contenido del Excel
     * @throws IOException Si hay error al generar el Excel
     */
    public static byte[] generarExcelVacunacion(List<RegistroVacunacion> registros) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Registros de Vacunación");
        
        // Crear estilo para encabezados
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);
        
        // Crear fila de encabezados
        Row headerRow = sheet.createRow(0);
        String[] columns = {
            "ID", "Paciente", "Documento", "Vacuna", "Lote", 
            "Laboratorio", "Fecha Vacunación", "Personal Médico", "Lugar", "Observaciones"
        };
        
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Crear filas de datos
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int rowNum = 1;
        
        for (RegistroVacunacion registro : registros) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(registro.getId());
            row.createCell(1).setCellValue(registro.getNombrePaciente());
            row.createCell(2).setCellValue(registro.getDocumentoPaciente());
            row.createCell(3).setCellValue(registro.getNombreVacuna());
            row.createCell(4).setCellValue(registro.getLoteVacuna());
            row.createCell(5).setCellValue(registro.getLaboratorio());
            row.createCell(6).setCellValue(sdf.format(registro.getFechaVacunacion()));
            row.createCell(7).setCellValue(registro.getNombrePersonalMedico());
            row.createCell(8).setCellValue(registro.getLugarVacunacion());
            row.createCell(9).setCellValue(registro.getObservaciones());
        }
        
        // Ajustar ancho de columnas
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // Escribir en ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        return outputStream.toByteArray();
    }
}
