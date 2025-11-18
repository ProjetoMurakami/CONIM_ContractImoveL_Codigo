package ContractImovel.model.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import ContractImovel.model.Inquilino;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class inquilinoDao implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(inquilinoDao.class);

    private static final EntityManagerFactory factory =
            Persistence.createEntityManagerFactory("testePU");

    private EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    // ------------------------------
    // SALVAR
    // ------------------------------
    public Inquilino salvar(Inquilino inquilino) {
        LOGGER.info("Salvar DAO... Inquilino = " + inquilino);

        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();

            Inquilino salvo;
            if (inquilino.getId() == null) {
                em.persist(inquilino);
                salvo = inquilino;
            } else {
                salvo = em.merge(inquilino);
            }

            em.getTransaction().commit();
            return salvo;

        } catch (Exception e) {
            LOGGER.error("Erro ao salvar inquilino", e);
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
        }
    }

    // ------------------------------
    // EXCLUIR
    // ------------------------------
    public void excluir(Inquilino inquilino) {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();

            Inquilino i = em.find(Inquilino.class, inquilino.getId());
            if (i != null) {
                em.remove(i);
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            LOGGER.error("Erro ao excluir inquilino ID: " + inquilino.getId(), e);
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
        }
    }

    // ------------------------------
    // BUSCAR POR ID
    // ------------------------------
    public Inquilino buscarPeloCodigo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Inquilino.class, id);
        } finally {
            em.close();
        }
    }

    // ------------------------------
    // BUSCAR TODOS
    // ------------------------------
    @SuppressWarnings("unchecked")
    public List<Inquilino> buscarTodos() {
        EntityManager em = getEntityManager();
        try {
            String query = "SELECT i FROM Inquilino i";
            return em.createQuery(query).getResultList();

        } finally {
            em.close();
        }
    }
}
