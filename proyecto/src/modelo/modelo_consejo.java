/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import vista.vista_consejo;



public class modelo_consejo {
 conexion con=new conexion();
    Connection cn = con.conexion();
    private String ResultSet;
       public String consejon="consejo";

    public String nombrev(){
        try {
           
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT usuario FROM login WHERE tipo ="+consejon);    
            while (rs.next()) {
       consejon=rs.getString(1);
            }
       return consejon;
        } catch (SQLException e) {
        // JOptionPane.showMessageDialog(null,"D:!"+e);
        }
    return consejon;
}

public void cargar(vista_consejo co,String cedula){
    try {
           Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT nombre,apellido FROM usuarios WHERE cedula ="+cedula);     
            while(rs.next()){
                co.jLabel2.setText(rs.getString(1)+" "+rs.getString(2));
            }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
    }
}


}
