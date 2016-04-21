/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.dao.exceptions.IllegalOrphanException;
import com.dao.exceptions.NonexistentEntityException;
import com.dao.exceptions.PreexistingEntityException;
import com.model.Consejo;
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
public class ConsejoJpaController implements Serializable {

    public ConsejoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Consejo consejo) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Usuarios usuariosOrphanCheck = consejo.getUsuarios();
        if (usuariosOrphanCheck != null) {
            Consejo oldConsejoOfUsuarios = usuariosOrphanCheck.getConsejo();
            if (oldConsejoOfUsuarios != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Usuarios " + usuariosOrphanCheck + " already has an item of type Consejo whose usuarios column cannot be null. Please make another selection for the usuarios field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios usuarios = consejo.getUsuarios();
            if (usuarios != null) {
                usuarios = em.getReference(usuarios.getClass(), usuarios.getCedula());
                consejo.setUsuarios(usuarios);
            }
            em.persist(consejo);
            if (usuarios != null) {
                usuarios.setConsejo(consejo);
                usuarios = em.merge(usuarios);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findConsejo(consejo.getCedula()) != null) {
                throw new PreexistingEntityException("Consejo " + consejo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Consejo consejo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Consejo persistentConsejo = em.find(Consejo.class, consejo.getCedula());
            Usuarios usuariosOld = persistentConsejo.getUsuarios();
            Usuarios usuariosNew = consejo.getUsuarios();
            List<String> illegalOrphanMessages = null;
            if (usuariosNew != null && !usuariosNew.equals(usuariosOld)) {
                Consejo oldConsejoOfUsuarios = usuariosNew.getConsejo();
                if (oldConsejoOfUsuarios != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Usuarios " + usuariosNew + " already has an item of type Consejo whose usuarios column cannot be null. Please make another selection for the usuarios field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usuariosNew != null) {
                usuariosNew = em.getReference(usuariosNew.getClass(), usuariosNew.getCedula());
                consejo.setUsuarios(usuariosNew);
            }
            consejo = em.merge(consejo);
            if (usuariosOld != null && !usuariosOld.equals(usuariosNew)) {
                usuariosOld.setConsejo(null);
                usuariosOld = em.merge(usuariosOld);
            }
            if (usuariosNew != null && !usuariosNew.equals(usuariosOld)) {
                usuariosNew.setConsejo(consejo);
                usuariosNew = em.merge(usuariosNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = consejo.getCedula();
                if (findConsejo(id) == null) {
                    throw new NonexistentEntityException("The consejo with id " + id + " no longer exists.");
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
            Consejo consejo;
            try {
                consejo = em.getReference(Consejo.class, id);
                consejo.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The consejo with id " + id + " no longer exists.", enfe);
            }
            Usuarios usuarios = consejo.getUsuarios();
            if (usuarios != null) {
                usuarios.setConsejo(null);
                usuarios = em.merge(usuarios);
            }
            em.remove(consejo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Consejo> findConsejoEntities() {
        return findConsejoEntities(true, -1, -1);
    }

    public List<Consejo> findConsejoEntities(int maxResults, int firstResult) {
        return findConsejoEntities(false, maxResults, firstResult);
    }

    private List<Consejo> findConsejoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Consejo.class));
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

    public Consejo findConsejo(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Consejo.class, id);
        } finally {
            em.close();
        }
    }

    public int getConsejoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Consejo> rt = cq.from(Consejo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
