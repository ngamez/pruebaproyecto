/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import modelo.modelo_registro;
import vista.vista_consejo;
import vista.vista_login;
import vista.vista_registro;

public class controlador_registro implements ActionListener{
   vista_registro registro;
   String tipo;
   int ac;
   String cedulac;
    public controlador_registro(vista_registro r,String t,int a,String cedu){    
    registro=r;
    tipo=t;
    ac=a;
    cedulac=cedu;
    registro.jButton1.addActionListener(this);
    registro.jButton2.addActionListener(this);
    }

 
    public void actionPerformed(ActionEvent e) {
    if(e.getSource()==registro.jButton1){
        modelo_registro reg=new modelo_registro();
        String nom=registro.jTextField1.getText(),
               ape=registro.jTextField2.getText(),
               ced=registro.jTextField3.getText(),
               tel=registro.jTextField4.getText(),
              corr=registro.jTextField5.getText(),
               usu=registro.jTextField7.getText(),
              pass=registro.jTextField8.getText(),
              paip=registro.jTextField6.getText();
        
     if(!nom.equals("")&&!ape.equals("")&&!ced.equals("")&&!tel.equals("")&&!corr.equals("")&&!usu.equals("")&&!pass.equals("")&&!paip.equals("")){   
        int res=reg.registrar(nom, ape, ced, tel, corr, paip, tipo, usu, pass);
        if(res==0){
        JOptionPane.showMessageDialog(null,"registro exitoso");
        
        if(ac==0){
        registro.hide();
        vista_login v=new vista_login();
        controlador_login log=new controlador_login(v);
        v.setLocationRelativeTo(null);
        v.show();
        }else{
           registro.hide();
           vista_consejo c=new vista_consejo();
            controlador_consejo cons=new controlador_consejo(c,cedulac);
            c.setLocationRelativeTo(null);
            c.show();
        }
        }
    }else{
         JOptionPane.showMessageDialog(null,"casillas vacias");
     }
    }else if(e.getSource()==registro.jButton2){
        registro.hide();
        if(ac==0){
        vista_login v=new vista_login();
        controlador_login log=new controlador_login(v);
        v.setLocationRelativeTo(null);
        v.show();
        }else{
            vista_consejo c=new vista_consejo();
            controlador_consejo cons=new controlador_consejo(c,cedulac);
            c.setLocationRelativeTo(null);
            c.show();
        }
    } 
    }
    
    
    
}
