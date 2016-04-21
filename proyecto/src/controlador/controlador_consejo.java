/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import modelo.modelo_consejo;
import vista.vista_consejo;
import vista.vista_estudiante;
import vista.vista_login;
import vista.vista_registro;


public class controlador_consejo implements ActionListener{
vista_consejo consejo;
      
        String cedu;
    public controlador_consejo(vista_consejo c,String ce){
        consejo=c;
        cedu=ce;
        consejo.jButton1.addActionListener(this);
        consejo.jButton2.addActionListener(this);
        consejo.jButton3.addActionListener(this);
        consejo.jButton4.addActionListener(this);
        consejo.jButton5.addActionListener(this);
        consejo.jButton6.addActionListener(this);
        consejo.jButton7.addActionListener(this);
        modelo_consejo cons=new modelo_consejo();
        cons.cargar(consejo,ce);
      
    }

    controlador_consejo(vista_estudiante o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void actionPerformed(ActionEvent e) {
       
       if(e.getSource()==consejo.jButton1){
        consejo.hide();
        vista_registro v=new vista_registro();
        controlador_registro log=new controlador_registro(v,"consejo",1,cedu);
        v.setLocationRelativeTo(null);
        v.show();
 }else if(e.getSource()==consejo.jButton2){
     consejo.hide();
        vista_registro v=new vista_registro();
        controlador_registro log=new controlador_registro(v,"profesor",1,cedu);
        v.setLocationRelativeTo(null);
        v.show();
 }else if(e.getSource()==consejo.jButton3){
     consejo.hide();
        vista_registro v=new vista_registro();
        controlador_registro log=new controlador_registro(v,"estudiante",1,cedu);
        v.setLocationRelativeTo(null);
        v.show();
 }else if(e.getSource()==consejo.jButton4){
     consejo.jTextArea1.setText("Administrado:\n"
             + "-tiene el control de todo\n"
             + "-super usuario\n");
 }else if(e.getSource()==consejo.jButton5){
     consejo.jTextArea1.setText("Profesor:\n"
             + "-tiene control limitado a su area\n"
             + "-usuario comun\n");
 }else if(e.getSource()==consejo.jButton6){
     consejo.jTextArea1.setText("Estudiante:\n"
             + "-tiene control limitado a su area\n"
             + "-usuario comun\n");
 }else if(e.getSource()==consejo.jButton7){
     consejo.hide();
           vista_login l=new vista_login();
           controlador_login cl=new controlador_login(l);
           l.setLocationRelativeTo(null);
           l.show();
     
 }
        
        
    }
  
    
    
    
}
