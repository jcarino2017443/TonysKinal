
package org.jefricarino.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.jefricarino.report.GenerarReporte;
import org.jefricarino.system.Principal;


public class MenuPrincipalController implements Initializable  {
    private Principal escenarioPrincipal;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaDatosProgramador (){
        escenarioPrincipal.ventanaDatosProgramador();
    }
    
    public void ventanaEmpresas(){
        escenarioPrincipal.ventanaEmpresas();
    }
    
    public void ventanaProductos(){
        escenarioPrincipal.ventanaProductos();
    }
    public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
    }
    
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
    
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
    
    public void ventanaPlatos(){
        escenarioPrincipal.ventanaPlatos();
    }
    
    public void ventanaServicios_has_Empleados(){
        escenarioPrincipal.ventanaServicios_has_Empleados();
    }
    
    public void ventanaServicios_has_Platos(){
        escenarioPrincipal.ventanaServicio_has_Platos();
    }
    
    public void ventanaProductos_has_Platos(){
        escenarioPrincipal.ventanaProductos_has_Platos();
    }
    
    public void ImprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoEmpresa", null);
        GenerarReporte.mostrarReporte("ReporteEmpresas.jasper", "Reporte Empresas", parametros);
    }
    
    public void ReporteProductos(){
        Map parametros = new HashMap();
        parametros.put("codigoProducto", null);
        GenerarReporte.mostrarReporte("ReporteProductos.jasper", "Reporte Productos", parametros);
    }
    
    public void ReporteTipoPlatos(){
        Map parametros = new HashMap();
        parametros.put("codigoTipoPlato", null);
        GenerarReporte.mostrarReporte("ReporterTipoPlato.jasper", "Reporte Tipo Platos", parametros);
    }
    
}
