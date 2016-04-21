/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dao;

import com.dao.exceptions.IllegalOrphanException;
import com.dao.exceptions.NonexistentEntityException;
import com.dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.model.Consejo;
import com.model.Estudiantes;
import com.model.Profesores;
import com.model.Login;
import com.model.Usuarios;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USER
 */
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) throws PreexistingEntityException, Exception {
        if (usuarios.getLoginCollection() == null) {
            usuarios.setLoginCollection(new ArrayList<Login>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Consejo consejo = usuarios.getConsejo();
            if (consejo != null) {
                consejo = em.getReference(consejo.getClass(), consejo.getCedula());
                usuarios.setConsejo(consejo);
            }
            Estudiantes estudiantes = usuarios.getEstudiantes();
            if (estudiantes != null) {
                estudiantes = em.getReference(estudiantes.getClass(), estudiantes.getCedula());
                usuarios.setEstudiantes(estudiantes);
            }
            Profesores profesores = usuarios.getProfesores();
            if (profesores != null) {
                profesores = em.getReference(profesores.getClass(), profesores.getCedula());
                usuarios.setProfesores(profesores);
            }
            Collection<Login> attachedLoginCollection = new ArrayList<Login>();
            for (Login loginCollectionLoginToAttach : usuarios.getLoginCollection()) {
                loginCollectionLoginToAttach = em.getReference(loginCollectionLoginToAttach.getClass(), loginCollectionLoginToAttach.getUsuario());
                attachedLoginCollection.add(loginCollectionLoginToAttach);
            }
            usuarios.setLoginCollection(attachedLoginCollection);
            em.persist(usuarios);
            if (consejo != null) {
                Usuarios oldUsuariosOfConsejo = consejo.getUsuarios();
                if (oldUsuariosOfConsejo != null) {
                    oldUsuariosOfConsejo.setConsejo(null);
                    oldUsuariosOfConsejo = em.merge(oldUsuariosOfConsejo);
                }
                consejo.setUsuarios(usuarios);
                consejo = em.merge(consejo);
            }
            if (estudiantes != null) {
                Usuarios oldUsuariosOfEstudiantes = estudiantes.getUsuarios();
                if (oldUsuariosOfEstudiantes != null) {
                    oldUsuariosOfEstudiantes.setEstudiantes(null);
                    oldUsuariosOfEstudiantes = em.merge(oldUsuariosOfEstudiantes);
                }
                estudiantes.setUsuarios(usuarios);
                estudiantes = em.merge(estudiantes);
            }
            if (profesores != null) {
                Usuarios oldUsuariosOfProfesores = profesores.getUsuarios();
                if (oldUsuariosOfProfesores != null) {
                    oldUsuariosOfProfesores.setProfesores(null);
                    oldUsuariosOfProfesores = em.merge(oldUsuariosOfProfesores);
                }
                profesores.setUsuarios(usuarios);
                profesores = em.merge(profesores);
            }
            for (Login loginCollectionLogin : usuarios.getLoginCollection()) {
                Usuarios oldCedulaOfLoginCollectionLogin = loginCollectionLogin.getCedula();
                loginCollectionLogin.setCedula(usuarios);
                loginCollectionLogin = em.merge(loginCollectionLogin);
                if (oldCedulaOfLoginCollectionLogin != null) {
                    oldCedulaOfLoginCollectionLogin.getLoginCollection().remove(loginCollectionLogin);
                    oldCedulaOfLoginCollectionLogin = em.merge(oldCedulaOfLoginCollectionLogin);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuarios(usuarios.getCedula()) != null) {
                throw new PreexistingEntityException("Usuarios " + usuarios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getCedula());
            Consejo consejoOld = persistentUsuarios.getConsejo();
            Consejo consejoNew = usuarios.getConsejo();
            Estudiantes estudiantesOld = persistentUsuarios.getEstudiantes();
            Estudiantes estudiantesNew = usuarios.getEstudiantes();
            Profesores profesoresOld = persistentUsuarios.getProfesores();
            Profesores profesoresNew = usuarios.getProfesores();
            Collection<Login> loginCollectionOld = persistentUsuarios.getLoginCollection();
            Collection<Login> loginCollectionNew = usuarios.getLoginCollection();
            List<String> illegalOrphanMessages = null;
            if (consejoOld != null && !consejoOld.equals(consejoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Consejo " + consejoOld + " since its usuarios field is not nullable.");
            }
            if (estudiantesOld != null && !estudiantesOld.equals(estudiantesNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Estudiantes " + estudiantesOld + " since its usuarios field is not nullable.");
            }
            if (profesoresOld != null && !profesoresOld.equals(profesoresNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Profesores " + profesoresOld + " since its usuarios field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (consejoNew != null) {
                consejoNew = em.getReference(consejoNew.getClass(), consejoNew.getCedula());
                usuarios.setConsejo(consejoNew);
            }
            if (estudiantesNew != null) {
                estudiantesNew = em.getReference(estudiantesNew.getClass(), estudiantesNew.getCedula());
                usuarios.setEstudiantes(estudiantesNew);
            }
            if (profesoresNew != null) {
                profesoresNew = em.getReference(profesoresNew.getClass(), profesoresNew.getCedula());
                usuarios.setProfesores(profesoresNew);
            }
            Collection<Login> attachedLoginCollectionNew = new ArrayList<Login>();
            for (Login loginCollectionNewLoginToAttach : loginCollectionNew) {
                loginCollectionNewLoginToAttach = em.getReference(loginCollectionNewLoginToAttach.getClass(), loginCollectionNewLoginToAttach.getUsuario());
                attachedLoginCollectionNew.add(loginCollectionNewLoginToAttach);
            }
            loginCollectionNew = attachedLoginCollectionNew;
            usuarios.setLoginCollection(loginCollectionNew);
            usuarios = em.merge(usuarios);
            if (consejoNew != null && !consejoNew.equals(consejoOld)) {
                Usuarios oldUsuariosOfConsejo = consejoNew.getUsuarios();
                if (oldUsuariosOfConsejo != null) {
                    oldUsuariosOfConsejo.setConsejo(null);
                    oldUsuariosOfConsejo = em.merge(oldUsuariosOfConsejo);
                }
                consejoNew.setUsuarios(usuarios);
                consejoNew = em.merge(consejoNew);
            }
            if (estudiantesNew != null && !estudiantesNew.equals(estudiantesOld)) {
                Usuarios oldUsuariosOfEstudiantes = estudiantesNew.getUsuarios();
                if (oldUsuariosOfEstudiantes != null) {
                    oldUsuariosOfEstudiantes.setEstudiantes(null);
                    oldUsuariosOfEstudiantes = em.merge(oldUsuariosOfEstudiantes);
                }
                estudiantesNew.setUsuarios(usuarios);
                estudiantesNew = em.merge(estudiantesNew);
            }
            if (profesoresNew != null && !profesoresNew.equals(profesoresOld)) {
                Usuarios oldUsuariosOfProfesores = profesoresNew.getUsuarios();
                if (oldUsuariosOfProfesores != null) {
                    oldUsuariosOfProfesores.setProfesores(null);
                    oldUsuariosOfProfesores = em.merge(oldUsuariosOfProfesores);
                }
                profesoresNew.setUsuarios(usuarios);
                profesoresNew = em.merge(profesoresNew);
            }
            for (Login loginCollectionOldLogin : loginCollectionOld) {
                if (!loginCollectionNew.contains(loginCollectionOldLogin)) {
                    loginCollectionOldLogin.setCedula(null);
                    loginCollectionOldLogin = em.merge(loginCollectionOldLogin);
                }
            }
            for (Login loginCollectionNewLogin : loginCollectionNew) {
                if (!loginCollectionOld.contains(loginCollectionNewLogin)) {
                    Usuarios oldCedulaOfLoginCollectionNewLogin = loginCollectionNewLogin.getCedula();
                    loginCollectionNewLogin.setCedula(usuarios);
                    loginCollectionNewLogin = em.merge(loginCollectionNewLogin);
                    if (oldCedulaOfLoginCollectionNewLogin != null && !oldCedulaOfLoginCollectionNewLogin.equals(usuarios)) {
                        oldCedulaOfLoginCollectionNewLogin.getLoginCollection().remove(loginCollectionNewLogin);
                        oldCedulaOfLoginCollectionNewLogin = em.merge(oldCedulaOfLoginCollectionNewLogin);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuarios.getCedula();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getCedula();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Consejo consejoOrphanCheck = usuarios.getConsejo();
            if (consejoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuarios (" + usuarios + ") cannot be destroyed since the Consejo " + consejoOrphanCheck + " in its consejo field has a non-nullable usuarios field.");
            }
            Estudiantes estudiantesOrphanCheck = usuarios.getEstudiantes();
            if (estudiantesOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuarios (" + usuarios + ") cannot be destroyed since the Estudiantes " + estudiantesOrphanCheck + " in its estudiantes field has a non-nullable usuarios field.");
            }
            Profesores profesoresOrphanCheck = usuarios.getProfesores();
            if (profesoresOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuarios (" + usuarios + ") cannot be destroyed since the Profesores " + profesoresOrphanCheck + " in its profesores field has a non-nullable usuarios field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Login> loginCollection = usuarios.getLoginCollection();
            for (Login loginCollectionLogin : loginCollection) {
                loginCollectionLogin.setCedula(null);
                loginCollectionLogin = em.merge(loginCollectionLogin);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
