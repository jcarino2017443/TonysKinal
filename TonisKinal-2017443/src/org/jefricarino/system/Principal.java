
package org.jefricarino.system;


import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jefricarino.controller.DatosProgramadorController;
import org.jefricarino.controller.EmpleadosController;
import org.jefricarino.controller.EmpresasController;
import org.jefricarino.controller.MenuPrincipalController;
import org.jefricarino.controller.PlatosController;
import org.jefricarino.controller.PresupuestoController;
import org.jefricarino.controller.ProductosController;
import org.jefricarino.controller.Productos_has_PlatosController;
import org.jefricarino.controller.ServiciosController;
import org.jefricarino.controller.Servicios_has_Empleados_Controller;
import org.jefricarino.controller.Servicios_has_PlatosController;
import org.jefricarino.controller.TipoEmpleadoController;
import org.jefricarino.controller.TipoPlatoController;


public class Principal extends Application {
    private final String PAQUETE_VISTA = ("/org/jefricarino/view/");
    private Stage escenarioPrincipal;
    private Scene escena;
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
      
      this.escenarioPrincipal = escenarioPrincipal;
      this.escenarioPrincipal.setTitle("Tonis Kinal App");
      escenarioPrincipal.getIcons().add(new Image("/org/jefricarino/image/logo.png"));   
      menuPrincipal();
      escenarioPrincipal.show();
    }
    
    public void menuPrincipal(){
        try{
            MenuPrincipalController menuPrincipal = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",390,319);
            menuPrincipal.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEmpresas (){
        try{
            EmpresasController empresasController = (EmpresasController)cambiarEscena("EmpresasView.fxml",642,491);
            empresasController.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void ventanaDatosProgramador(){
        try {
            DatosProgramadorController datosProgramador = (DatosProgramadorController)cambiarEscena("DatosProgramadorView.fxml",418,377);
            datosProgramador.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProductos(){
        try {
            ProductosController productosController = (ProductosController)cambiarEscena("ProductosView.fxml",538,410);
            productosController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }   
    }

    public void ventanaPresupuesto(){
        try{
            PresupuestoController presupuestoController = (PresupuestoController)cambiarEscena("PresupuestosView.fxml",530,500);
            presupuestoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
            
    }
    
    public void ventanaTipoEmpleado(){
        try{
            TipoEmpleadoController tipoEmpleadoController = (TipoEmpleadoController)cambiarEscena("TipoEmpleadosView.fxml",568,400);
            tipoEmpleadoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaEmpleado(){
        try{
            EmpleadosController empleadosController = (EmpleadosController)cambiarEscena("EmpleadosView.fxml",956,499);
            empleadosController.setEscenarioPrincipal(this);   
          }catch(Exception e){
              e.printStackTrace();
          }
    }
    
    public void ventanaServicios(){
        try{
            ServiciosController serviciosController = (ServiciosController)cambiarEscena("ServiciosView.fxml",716,504);
            serviciosController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
            
    }
    
    public void ventanaTipoPlato(){
        try{
            TipoPlatoController tipoPlatoController = (TipoPlatoController)cambiarEscena("TipoPlatoView.fxml",584,442);
            tipoPlatoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void ventanaPlatos(){
        try{
            PlatosController platoController = (PlatosController)cambiarEscena("PlatosView.fxml",617,518);
            platoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaServicios_has_Empleados(){
        try{
            Servicios_has_Empleados_Controller servicio_empleado = (Servicios_has_Empleados_Controller)cambiarEscena("Servicios_has_EmpleadosView.fxml",654,444);
            servicio_empleado.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaServicio_has_Platos(){
        try{
            Servicios_has_PlatosController servicio_plato = (Servicios_has_PlatosController)cambiarEscena("Servicios_has_PlatosView.fxml",484,442);
            servicio_plato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaProductos_has_Platos(){
        try{
            Productos_has_PlatosController produPlato = (Productos_has_PlatosController)cambiarEscena("Productos_has_PlatosView.fxml",484,441);
            produPlato.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA + fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA + fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        
        return resultado;
    }
    
}