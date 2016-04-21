
package controlador;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import modelo.modelo_login;
import vista.vista_estudiante;
import vista.vista_consejo;
import vista.vista_login;
import vista.vista_profesor;
import vista.vista_registro;


public class controlador_login implements ActionListener{
    
vista_login vista;

 public controlador_login(vista_login v){
 vista=v;
 vista.jButton1.addActionListener(this);
 vista.jButton2.addActionListener(this);
 vista.jLabel3.addActionListener(this);
 vista.jPanel1.setBackground(Color.WHITE);
 }
    
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource()==vista.jButton1){
        modelo_login log=new modelo_login();
        String user="",pass="",consultado="";
        user=vista.jTextField1.getText();
        pass=new String(vista.jPasswordField1.getPassword());
        consultado= log.ver(user, pass);
        if(!consultado.equals("")){
            JOptionPane.showMessageDialog(null,"Ingreso Exitoso");
            //------------------------------------
            switch (consultado) {
                case "consejo":{
            vista.hide();
            vista_consejo c=new vista_consejo();
            controlador_consejo cons=new controlador_consejo(c,log.get_cedula(user));
            c.setLocationRelativeTo(null);
            c.show();
                    break;
                }
            
                case "profesor":{
                    vista.hide();
                    vista_profesor pro=new vista_profesor();
                    controlador_profesor cp=new controlador_profesor(log.get_cedula(user),pro);
                    pro.setLocationRelativeTo(null);
                    pro.show();
                    
                    
                    break;
                }
                case "estudiante":{
                    vista.hide();
                   vista_estudiante novo;
                   novo = new vista_estudiante();
                    novo.setVisible(true);
                    
                    
                  
                    break;
                }
            }
            //-------------------------------------
        }else{
           JOptionPane.showMessageDialog(null, "datos erroneos");
           vista.jTextField1.setText("");
           vista.jPasswordField1.setText("");
        }
        
        }else if(e.getSource()==vista.jButton2){
            System.exit(0);
        }else if(e.getSource()==vista.jLabel3){
            //--------------------------------
        vista.hide();
        vista_registro v=new vista_registro();
        controlador_registro log=new controlador_registro(v,"estudiante",0,"");
        v.setLocationRelativeTo(null);
        v.show();
            
            //--------------------------------
        }
        
    }

    private vista_estudiante vista_estudiante() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
