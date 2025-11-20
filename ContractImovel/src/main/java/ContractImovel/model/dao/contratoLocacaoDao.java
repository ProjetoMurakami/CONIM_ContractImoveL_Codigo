package ContractImovel.model.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import ContractImovel.model.ContratoLocacao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class contratoLocacaoDao implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(contratoLocacaoDao.class);

    private static final EntityManagerFactory factory =
            Persistence.createEntityManagerFactory("testePU");

    private EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    // ------------------------------
    // SALVAR
    // ------------------------------
    public ContratoLocacao salvar(ContratoLocacao contratoLocacao) {
        LOGGER.info("Salvar DAO... Contrato = " + contratoLocacao);

        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            ContratoLocacao salvo = em.merge(contratoLocacao);
            em.getTransaction().commit();
            return salvo;

        } catch (Exception e) {
            LOGGER.error("Erro ao salvar contrato", e);
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
        }
    }

    // ------------------------------
    // EXCLUIR
    // ------------------------------
    public void excluir(ContratoLocacao contratoLocacao) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();

            ContratoLocacao c = em.find(ContratoLocacao.class, contratoLocacao.getId());
            if (c != null) {
                em.remove(c);
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            LOGGER.error("Erro ao excluir contrato ID: " + contratoLocacao.getId(), e);
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
        }
    }

    // ------------------------------
    // BUSCAR POR ID
    // ------------------------------
    public ContratoLocacao buscarPeloCodigo(Long id) {
        EntityManager em = getEntityManager();

        try {
            return em.find(ContratoLocacao.class, id);
        } finally {
            em.close();
        }
    }

    // ------------------------------
    // BUSCAR TODOS
    // ------------------------------
    @SuppressWarnings("unchecked")
    public List<ContratoLocacao> buscarTodos() {
        EntityManager em = getEntityManager();

        try {
            String query = "SELECT c FROM ContratoLocacao c " +
                    "LEFT JOIN FETCH c.imovel " +
                    "LEFT JOIN FETCH c.inquilino " +
                    "LEFT JOIN FETCH c.fiador";

            return em.createQuery(query).getResultList();

        } finally {
            em.close();
        }
    }
}
