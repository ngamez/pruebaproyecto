/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.dao.exceptions.IllegalOrphanException;
import com.dao.exceptions.NonexistentEntityException;
import com.dao.exceptions.PreexistingEntityException;
import com.model.Estudiantes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.model.Usuarios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USER
 */
public class EstudiantesJpaController implements Serializable {

    public EstudiantesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estudiantes estudiantes) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Usuarios usuariosOrphanCheck = estudiantes.getUsuarios();
        if (usuariosOrphanCheck != null) {
            Estudiantes oldEstudiantesOfUsuarios = usuariosOrphanCheck.getEstudiantes();
            if (oldEstudiantesOfUsuarios != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Usuarios " + usuariosOrphanCheck + " already has an item of type Estudiantes whose usuarios column cannot be null. Please make another selection for the usuarios field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios usuarios = estudiantes.getUsuarios();
            if (usuarios != null) {
                usuarios = em.getReference(usuarios.getClass(), usuarios.getCedula());
                estudiantes.setUsuarios(usuarios);
            }
            em.persist(estudiantes);
            if (usuarios != null) {
                usuarios.setEstudiantes(estudiantes);
                usuarios = em.merge(usuarios);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstudiantes(estudiantes.getCedula()) != null) {
                throw new PreexistingEntityException("Estudiantes " + estudiantes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estudiantes estudiantes) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudiantes persistentEstudiantes = em.find(Estudiantes.class, estudiantes.getCedula());
            Usuarios usuariosOld = persistentEstudiantes.getUsuarios();
            Usuarios usuariosNew = estudiantes.getUsuarios();
            List<String> illegalOrphanMessages = null;
            if (usuariosNew != null && !usuariosNew.equals(usuariosOld)) {
                Estudiantes oldEstudiantesOfUsuarios = usuariosNew.getEstudiantes();
                if (oldEstudiantesOfUsuarios != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Usuarios " + usuariosNew + " already has an item of type Estudiantes whose usuarios column cannot be null. Please make another selection for the usuarios field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usuariosNew != null) {
                usuariosNew = em.getReference(usuariosNew.getClass(), usuariosNew.getCedula());
                estudiantes.setUsuarios(usuariosNew);
            }
            estudiantes = em.merge(estudiantes);
            if (usuariosOld != null && !usuariosOld.equals(usuariosNew)) {
                usuariosOld.setEstudiantes(null);
                usuariosOld = em.merge(usuariosOld);
            }
            if (usuariosNew != null && !usuariosNew.equals(usuariosOld)) {
                usuariosNew.setEstudiantes(estudiantes);
                usuariosNew = em.merge(usuariosNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = estudiantes.getCedula();
                if (findEstudiantes(id) == null) {
                    throw new NonexistentEntityException("The estudiantes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudiantes estudiantes;
            try {
                estudiantes = em.getReference(Estudiantes.class, id);
                estudiantes.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estudiantes with id " + id + " no longer exists.", enfe);
            }
            Usuarios usuarios = estudiantes.getUsuarios();
            if (usuarios != null) {
                usuarios.setEstudiantes(null);
                usuarios = em.merge(usuarios);
            }
            em.remove(estudiantes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estudiantes> findEstudiantesEntities() {
        return findEstudiantesEntities(true, -1, -1);
    }

    public List<Estudiantes> findEstudiantesEntities(int maxResults, int firstResult) {
        return findEstudiantesEntities(false, maxResults, firstResult);
    }

    private List<Estudiantes> findEstudiantesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estudiantes.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Estudiantes findEstudiantes(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estudiantes.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstudiantesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estudiantes> rt = cq.from(Estudiantes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
