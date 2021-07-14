/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jefricarino.controller;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.jefricarino.bean.Empleados;
import org.jefricarino.bean.Servicio_has_Empleado;
import org.jefricarino.bean.Servicios;
import org.jefricarino.db.Conexion;
import org.jefricarino.system.Principal;

/**
 *
 * @author Carino
 */
public class Servicios_has_Empleados_Controller implements Initializable{
    private enum Operaciones {NUEVO,GUARDAR,ELIMINAR,CANCELAR,EDITAR,ACTUALIZAR,REPORTE,NINGUNO}
    private Principal escenarioPrincipal;
    private Operaciones tipoDeOperacion = Operaciones.NINGUNO;
    private ObservableList<Servicio_has_Empleado> listaServiEmpleado;
    private ObservableList<Empleados> listaEmpleado;
    private ObservableList<Servicios> listaServicio;
    
    private DatePicker fecha;
    
    @FXML private GridPane grpFechaEvento;
    @FXML private TextField txtHoraEvento;
    @FXML private TextField txtLugarEvento;
    @FXML private ComboBox cmbCodigoServicio;
    @FXML private ComboBox cmbCodigoEmpleado;
    @FXML private TableView tblServiEmpleados;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colHora;
    @FXML private TableColumn colLugar;
    @FXML private TableColumn colCodigoServicio;
    @FXML private TableColumn colCodigoEmpleado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Button btnEmpleados;
    @FXML private Button btnServicios;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        DatePicker();
        fecha.setDisable(true);
        cmbCodigoServicio.setItems(getServicios());
        cmbCodigoEmpleado.setItems(getEmpleados());
        
    }
    
    public void cargarDatos(){
        tblServiEmpleados.setItems(getServiEmpleados());
        colFecha.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado,Date>("fechaEvento"));
        colHora.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado,String>("horaEvento"));
        colLugar.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado,String>("lugarEvento"));
        colCodigoServicio.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado,Integer>("codigoServicio"));
        colCodigoEmpleado.setCellValueFactory(new PropertyValueFactory<Servicio_has_Empleado,Integer>("codigoEmpleado"));
    }
    
    public void seleccionarElemento(){
        if(tblServiEmpleados.getSelectionModel().getSelectedItem()!=null){
            fecha.selectedDateProperty().set(((Servicio_has_Empleado)tblServiEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento());
            txtHoraEvento.setText(((Servicio_has_Empleado)tblServiEmpleados.getSelectionModel().getSelectedItem()).getHoraEvento());
            txtLugarEvento.setText(((Servicio_has_Empleado)tblServiEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento());
            cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleado(((Servicio_has_Empleado)tblServiEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleado()));
            cmbCodigoServicio.getSelectionModel().select(buscarServicio(((Servicio_has_Empleado)tblServiEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicio()));    
        }
        
    }
    
    public void DatePicker(){
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/jefricarino/resource/DatePicker.css");
        grpFechaEvento.add(fecha, 0, 0);
    }
    
    public ObservableList<Servicio_has_Empleado> getServiEmpleados(){
        ArrayList<Servicio_has_Empleado> lista = new ArrayList<Servicio_has_Empleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_Empleados}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicio_has_Empleado(resultado.getInt("Numero"),
                                                    resultado.getInt("codigoServicio"),
                                                    resultado.getInt("codigoEmpleado"),
                                                    resultado.getDate("fechaEvento"),
                                                    resultado.getString("horaEvento"),
                                                    resultado.getString("lugarEvento")
                ));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServiEmpleado = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Empleados> getEmpleados(){
        ArrayList<Empleados> lista = new ArrayList<Empleados>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Empleados(resultado.getInt("codigoEmpleado"),
                                        resultado.getInt("numeroEmpleado"),
                                        resultado.getString("apellidoEmpleado"),
                                        resultado.getString("nombreEmpleado"),
                                        resultado.getString("direccionEmpleado"),
                                        resultado.getString("telefonoContacto"),
                                        resultado.getString("gradoCocinero"),
                                        resultado.getInt("codigoTipoEmpleado")
                                        
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpleado = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Servicios> getServicios(){
        ArrayList<Servicios> lista = new ArrayList<Servicios>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicios(resultado.getInt("codigoServicio"),
                                        resultado.getDate("fechaServicio"),
                                        resultado.getString("tipoServicio"),
                                        resultado.getString("horaServicio"),
                                        resultado.getString("lugarServicio"),
                                        resultado.getString("telefonoContacto"),
                                        resultado.getInt("codigoEmpresa")
                ));
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServicio = FXCollections.observableArrayList(lista);
    }
    
    public Empleados buscarEmpleado(int codigoEmpleado){
        Empleados lista = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleados(?)}");
            procedimiento.setInt(1, codigoEmpleado);
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista = new Empleados(resultado.getInt("codigoEmpleado"),
                                      resultado.getInt("numeroEmpleado"),
                                       resultado.getString("ApellidoEmpleado"),
                                       resultado.getString("nombreEmpleado"),
                                       resultado.getString("direccionEmpleado"),
                                       resultado.getString("telefonoContacto"),
                                       resultado.getString("gradoCocinero"),
                                       resultado.getInt("codigoTipoEmpleado")
                );
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return lista;
    }
    
    public Servicios buscarServicio(int codigoServicio){
        Servicios lista = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicios(?)}");
            procedimiento.setInt(1, codigoServicio);
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista = new Servicios(resultado.getInt("codigoServicio"),
                                      resultado.getDate("fechaServicio"),
                                      resultado.getString("tipoServicio"),
                                      resultado.getString("horaServicio"),
                                      resultado.getString("lugarServicio"),
                                      resultado.getString("telefonoContacto"),
                                      resultado.getInt("codigoEmpresa")                  
                        );
                    }

        }catch(Exception e){
            e.printStackTrace();
        }
        
        return lista;
    }
    
    private boolean validarCampos(ComboBox codServicio, ComboBox codEmpleado, DatePicker fecha, TextField hora, TextField lugar){
        if(codServicio.getSelectionModel().getSelectedItem() !=null && codEmpleado.getSelectionModel().getSelectedItem() !=null && fecha.getSelectedDate()!=null && !hora.getText().equals("") && !lugar.getText().equals("")){
            return true;
            }else{
                    Alert a = new Alert(AlertType.WARNING);
                    a.setTitle("Validacion de Campos");
                    a.setHeaderText(null);
                    a.setContentText("debe de llenar todos los campos");
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
                cargarDatos();
                tipoDeOperacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                if(validarCampos(cmbCodigoServicio, cmbCodigoEmpleado, fecha,txtHoraEvento,txtLugarEvento)){
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
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
                cargarDatos();
                tipoDeOperacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                if(tblServiEmpleados.getSelectionModel().getSelectedItem()!=null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Esta seguro de Eliminar el registro?","Eliminar Servicio_Empleados",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicios_has_Empleados(?)}");
                            procedimiento.setString(1, ((Servicio_has_Empleado)tblServiEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento());
                            procedimiento.execute();
                            listaServiEmpleado.remove(tblServiEmpleados.getSelectionModel().getFocusedIndex());
                            limpiarControles();
                            cargarDatos();
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
                if(tblServiEmpleados.getSelectionModel().getSelectedItem() !=null){
                    activarControles();
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setDisable(false);
                    tipoDeOperacion = Operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                desactivarControles();
                txtHoraEvento.setDisable(false);
                txtHoraEvento.setEditable(false);
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setDisable(true);
                cargarDatos();
                tipoDeOperacion = Operaciones.NINGUNO;
                break;
        }
    }
    
    public void cancelar(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                desactivarControles();
                txtHoraEvento.setDisable(false);
                txtHoraEvento.setEditable(false);
                limpiarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setDisable(true);
                cargarDatos();
                tipoDeOperacion = Operaciones.NINGUNO;
                break;
            
        }
    }
   
    public void actualizar(){
        try{
            Servicio_has_Empleado registro = (Servicio_has_Empleado)tblServiEmpleados.getSelectionModel().getSelectedItem();
            registro.setCodigoEmpleado(((Empleados)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
            registro.setCodigoServicio(((Servicios)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
            registro.setFechaEvento(fecha.getSelectedDate());
            registro.setHoraEvento(txtHoraEvento.getText());
            registro.setLugarEvento(txtLugarEvento.getText());
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ActualizarServicios_has_Empleados(?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getNumero());
            procedimiento.setInt(2, registro.getCodigoServicio());
            procedimiento.setInt(3, registro.getCodigoEmpleado());
            procedimiento.setDate(4, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(5, registro.getHoraEvento());
            procedimiento.setString(6, registro.getLugarEvento());
            procedimiento.execute();
        }catch(MySQLIntegrityConstraintViolationException e){
            Alert a = new Alert(AlertType.WARNING);
            a.setTitle("Validacion de Laves");
            a.setHeaderText(null);
            a.setContentText("Los codigos del registro ya existen");
            a.showAndWait();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void guardar(){
        Servicio_has_Empleado registro = new Servicio_has_Empleado();
        registro.setCodigoServicio(((Servicios)cmbCodigoServicio.getSelectionModel().getSelectedItem()).getCodigoServicio());
        registro.setCodigoEmpleado(((Empleados)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleado());
        registro.setFechaEvento(fecha.getSelectedDate());
        registro.setHoraEvento(txtHoraEvento.getText());
        registro.setLugarEvento(txtLugarEvento.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicios_has_Empleados(?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoServicio());
            procedimiento.setInt(2, registro.getCodigoEmpleado());
            procedimiento.setDate(3, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(4, registro.getHoraEvento());
            procedimiento.setString(5, registro.getLugarEvento());
            procedimiento.execute();
            listaServiEmpleado.add(registro);
        }catch(MySQLIntegrityConstraintViolationException e){
            Alert a = new Alert(AlertType.WARNING);
            a.setTitle("Validacion de Llaves");
            a.setHeaderText(null);
            a.setContentText("El registro de Codigos ya existe");
            a.showAndWait();
        }
            catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void activarControles(){
        fecha.setDisable(false);
        txtHoraEvento.setEditable(true);
        txtLugarEvento.setEditable(true);
        cmbCodigoServicio.setDisable(false);
        cmbCodigoEmpleado.setDisable(false);
    }
    
    public void desactivarControles(){
        fecha.setDisable(true);
        txtHoraEvento.setEditable(false);
        txtLugarEvento.setEditable(false);
        cmbCodigoServicio.setDisable(true);
        cmbCodigoEmpleado.setDisable(true);
    }
    
    public void limpiarControles (){
        txtHoraEvento.setText("");
        txtLugarEvento.setText("");
        cmbCodigoServicio.getSelectionModel().clearSelection();
        cmbCodigoEmpleado.getSelectionModel().clearSelection();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaEmpleados(){
        escenarioPrincipal.ventanaEmpleado();
    }
    
    public void ventanaServicios(){
        escenarioPrincipal.ventanaServicios();
    }
    
}
