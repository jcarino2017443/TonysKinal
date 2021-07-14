
package org.jefricarino.controller;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.jefricarino.bean.TipoPlato;
import org.jefricarino.db.Conexion;
import org.jefricarino.report.GenerarReporte;
import org.jefricarino.system.Principal;


public class TipoPlatoController implements Initializable {
    private enum Operaciones {NUEVO,GUARDAR,ELIMINAR,CANCELAR,EDITAR,ACTUALIZAR,REPORTE,NINGUNO}
    private Principal escenarioPrincipal;
    private Operaciones tipoDeOperacion = Operaciones.NINGUNO;
    private ObservableList<TipoPlato> listaTipoPlato;
    
    @FXML private TextField txtCodigoTipoPlato;
    @FXML private TextField txtDescripcion;
    @FXML private GridPane grpDatos;
    @FXML private TableView tblTipoPlatos;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private TableColumn colDescripcion;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnMenuPrincipal;
    @FXML private Button btnPlato;
            
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos(){
        tblTipoPlatos.setItems(getTipoPlato());
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato,Integer>("codigoTipoPlato"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoPlato,String>("descripcion"));
    }
    
    public void seleccionarElemento(){
        if(tblTipoPlatos.getSelectionModel().getSelectedItem() !=null){        
        txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
        txtDescripcion.setText(((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getDescripcion());
        }
    }
    
    public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoPlatos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                                        resultado.getString("descripcion")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }
    
    private boolean validacion(TextField codTipoPlato, TextField descripcion){
        if(!codTipoPlato.getText().equals("") && !descripcion.getText().equals(""))
            return true;
        else{
            Alert a = new Alert(AlertType.WARNING);
            a.setTitle("Validacion de campos");
            a.setHeaderText(null);
            a.setContentText("Es necesario que todos los campos tengan datos");
            a.showAndWait();
        }
            return false;
    }
    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                cargarDatos();
                tipoDeOperacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                if(validacion(txtCodigoTipoPlato, txtDescripcion)){
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = Operaciones.NINGUNO;
                break;
                }
                  
        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                cargarDatos();
                tipoDeOperacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                if(tblTipoPlatos.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Esta seguro de eliminar este registro?","Eliminar TipoPlato", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoPlato(?)}");
                            procedimiento.setInt(1, ((TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                            procedimiento.execute();
                            listaTipoPlato.remove(tblTipoPlatos.getSelectionModel().getFocusedIndex());
                            limpiarControles();
                            desactivarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
                }  
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblTipoPlatos.getSelectionModel().getSelectedItem() !=null){
                    activarControles();
                    txtCodigoTipoPlato.setEditable(false);
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    tipoDeOperacion = Operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe selccionar un registro");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                cargarDatos();
                tipoDeOperacion = Operaciones.NINGUNO;
                break;
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                cargarDatos();
                tipoDeOperacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                cargarDatos();
                imprimirReporte();                
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("codigoTipoPlato", null);
        GenerarReporte.mostrarReporte("ReporterTipoPlato.jasper", "Reporte Tipo Plato", parametros);
    }
    
    public void guardar(){
        TipoPlato registro = new TipoPlato();
        registro.setCodigoTipoPlato(Integer.parseInt(txtCodigoTipoPlato.getText()));
        registro.setDescripcion(txtDescripcion.getText()); 
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoPlato(?,?)}");
            procedimiento.setInt(1, registro.getCodigoTipoPlato());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
            listaTipoPlato.add(registro);
        }catch(MySQLIntegrityConstraintViolationException e){
            Alert a = new Alert(AlertType.WARNING);
            a.setTitle("Codigo Identificador");
            a.setHeaderText(null);
            a.setContentText("El codigo identificador ya existe, seleccione otro");
            a.showAndWait();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void actualizar(){
        TipoPlato registro = (TipoPlato)tblTipoPlatos.getSelectionModel().getSelectedItem();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{ call sp_ActualizarTipoPlato(?,?)}");
//            registro.setCodigoTipoPlato(Integer.parseInt(txtCodigoTipoPlato.getText()));
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setInt(1, registro.getCodigoTipoPlato());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void activarControles(){
        txtCodigoTipoPlato.setEditable(true);
        txtDescripcion.setEditable(true);
        
    }
    
    public void desactivarControles(){
        txtCodigoTipoPlato.setEditable(false);
        txtDescripcion.setEditable(false);
    }
    
    public void limpiarControles(){
        txtCodigoTipoPlato.setText("");
        txtDescripcion.setText("");
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
      
   
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaPlatos() {
        escenarioPrincipal.ventanaPlatos();
    } 
    
}
