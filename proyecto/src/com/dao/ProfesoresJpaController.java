/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.dao.exceptions.IllegalOrphanException;
import com.dao.exceptions.NonexistentEntityException;
import com.dao.exceptions.PreexistingEntityException;
import com.model.Profesores;
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
public class ProfesoresJpaController implements Serializable {

    public ProfesoresJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Profesores profesores) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Usuarios usuariosOrphanCheck = profesores.getUsuarios();
        if (usuariosOrphanCheck != null) {
            Profesores oldProfesoresOfUsuarios = usuariosOrphanCheck.getProfesores();
            if (oldProfesoresOfUsuarios != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Usuarios " + usuariosOrphanCheck + " already has an item of type Profesores whose usuarios column cannot be null. Please make another selection for the usuarios field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios usuarios = profesores.getUsuarios();
            if (usuarios != null) {
                usuarios = em.getReference(usuarios.getClass(), usuarios.getCedula());
                profesores.setUsuarios(usuarios);
            }
            em.persist(profesores);
            if (usuarios != null) {
                usuarios.setProfesores(profesores);
                usuarios = em.merge(usuarios);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProfesores(profesores.getCedula()) != null) {
                throw new PreexistingEntityException("Profesores " + profesores + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Profesores profesores) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profesores persistentProfesores = em.find(Profesores.class, profesores.getCedula());
            Usuarios usuariosOld = persistentProfesores.getUsuarios();
            Usuarios usuariosNew = profesores.getUsuarios();
            List<String> illegalOrphanMessages = null;
            if (usuariosNew != null && !usuariosNew.equals(usuariosOld)) {
                Profesores oldProfesoresOfUsuarios = usuariosNew.getProfesores();
                if (oldProfesoresOfUsuarios != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Usuarios " + usuariosNew + " already has an item of type Profesores whose usuarios column cannot be null. Please make another selection for the usuarios field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usuariosNew != null) {
                usuariosNew = em.getReference(usuariosNew.getClass(), usuariosNew.getCedula());
                profesores.setUsuarios(usuariosNew);
            }
            profesores = em.merge(profesores);
            if (usuariosOld != null && !usuariosOld.equals(usuariosNew)) {
                usuariosOld.setProfesores(null);
                usuariosOld = em.merge(usuariosOld);
            }
            if (usuariosNew != null && !usuariosNew.equals(usuariosOld)) {
                usuariosNew.setProfesores(profesores);
                usuariosNew = em.merge(usuariosNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = profesores.getCedula();
                if (findProfesores(id) == null) {
                    throw new NonexistentEntityException("The profesores with id " + id + " no longer exists.");
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
            Profesores profesores;
            try {
                profesores = em.getReference(Profesores.class, id);
                profesores.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profesores with id " + id + " no longer exists.", enfe);
            }
            Usuarios usuarios = profesores.getUsuarios();
            if (usuarios != null) {
                usuarios.setProfesores(null);
                usuarios = em.merge(usuarios);
            }
            em.remove(profesores);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Profesores> findProfesoresEntities() {
        return findProfesoresEntities(true, -1, -1);
    }

    public List<Profesores> findProfesoresEntities(int maxResults, int firstResult) {
        return findProfesoresEntities(false, maxResults, firstResult);
    }

    private List<Profesores> findProfesoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Profesores.class));
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

    public Profesores findProfesores(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Profesores.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfesoresCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Profesores> rt = cq.from(Profesores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
