/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "consejo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Consejo.findAll", query = "SELECT c FROM Consejo c"),
    @NamedQuery(name = "Consejo.findByCedula", query = "SELECT c FROM Consejo c WHERE c.cedula = :cedula"),
    @NamedQuery(name = "Consejo.findByDescripcion", query = "SELECT c FROM Consejo c WHERE c.descripcion = :descripcion")})
public class Consejo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cedula")
    private String cedula;
    @Column(name = "descripcion")
    private String descripcion;
    @JoinColumn(name = "cedula", referencedColumnName = "cedula", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Usuarios usuarios;

    public Consejo() {
    }

    public Consejo(String cedula) {
        this.cedula = cedula;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cedula != null ? cedula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Consejo)) {
            return false;
        }
        Consejo other = (Consejo) object;
        if ((this.cedula == null && other.cedula != null) || (this.cedula != null && !this.cedula.equals(other.cedula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.model.Consejo[ cedula=" + cedula + " ]";
    }
    
}
