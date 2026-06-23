package org.jefrycruz.report;

import java.io.InputStream;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jefrycruz.db.Conexion;

public class GenerarReporte {

    public static void mostrarReporte(String nombreReporte, String titulo, Map parametros) {
        // Carga el archivo jasper como InputStream
        InputStream reporte = GenerarReporte.class.getResourceAsStream(nombreReporte);

        if (!parametros.containsKey("SubReporteEmpleadoParaFactura")) {
            InputStream SubReporteEmpleado = GenerarReporte.class.getResourceAsStream("/org/jefrycruz/report/SubReporteEmpleadoParaFactura.jasper");
            if (SubReporteEmpleado == null) {
                System.out.println("No se encontro la SubReporte para el reporte Factura, direccion actual: /org/jefrycruz/report/SubReporteEmpleadoParaFactura.jasper");
            }
            parametros.put("SubReporteEmpleadoParaFactura", SubReporteEmpleado);
        }

        if (!parametros.containsKey("SubReporteClienteParaFactura")) {
            InputStream SubReporteCliente = GenerarReporte.class.getResourceAsStream("/org/jefrycruz/report/SubReporteClienteParaFactura.jasper");
            if (SubReporteCliente == null) {
                System.out.println("No se encontro la SubReporte para el reporte Factura, direccion actual: /org/jefrycruz/report/SubReporteClienteParaFactura.jasper");
            }
            parametros.put("SubReporteClienteParaFactura", SubReporteCliente);
        }

        if (!parametros.containsKey("HojaMembretada")) {
            InputStream hoja = GenerarReporte.class.getResourceAsStream("/org/jefrycruz/image/Hoja_membretada.jpg");
            if (hoja == null) {
                System.out.println("No se encontro la imagen de la hoja membretada en /org/jefrycruz/image/Hoja_membretada.jpg");
            }
            parametros.put("HojaMembretada", hoja);
        }

        try {
            // Carga el reporte compilado (.jasper)
            JasperReport reporteMaestro = (JasperReport) JRLoader.loadObject(reporte);

            // Llenar el reporte con parámetros y conexión a base de datos
            JasperPrint reporteImpreso = JasperFillManager.fillReport(
                    reporteMaestro,
                    parametros,
                    Conexion.getInstancia().getConexion()
            );

            // Mostrar visor con reporte
            JasperViewer visor = new JasperViewer(reporteImpreso, false);
            visor.setTitle(titulo);
            visor.setVisible(true);

        } catch (Exception e) {
            System.out.println("Error al generar el reporte:");
            e.printStackTrace();
        }

    }
}
