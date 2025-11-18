package ContractImovel.model.dao;

import ContractImovel.model.Pagamento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.util.List;

public class pagamentoDao implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(pagamentoDao.class);

    private static final EntityManagerFactory factory =
            Persistence.createEntityManagerFactory("testePU");

    private EntityManager getEntityManager() {
        return factory.createEntityManager();
    }

    // ------------------------------------
    // SALVAR
    // ------------------------------------
    public Pagamento salvar(Pagamento pagamento) {

        LOGGER.info("Salvar DAO... Pagamento = " + pagamento);

        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();

            Pagamento salvo;
            if (pagamento.getId() == null) {
                em.persist(pagamento);
                salvo = pagamento;
            } else {
                salvo = em.merge(pagamento);
            }

            em.getTransaction().commit();
            return salvo;

        } catch (PersistenceException e) {
            LOGGER.error("Erro ao salvar Pagamento", e);
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
        }
    }

    // ------------------------------------
    // EXCLUIR
    // ------------------------------------
    public void excluir(Pagamento pagamento) {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();

            Pagamento pag = em.find(Pagamento.class, pagamento.getId());
            if (pag != null) {
                em.remove(pag);
            }

            em.getTransaction().commit();

        } catch (PersistenceException e) {
            LOGGER.error("Erro ao excluir Pagamento ID: " + pagamento.getId(), e);
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
        }
    }

    // ------------------------------------
    // BUSCAR POR ID
    // ------------------------------------
    public Pagamento buscarPorId(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pagamento.class, id);
        } finally {
            em.close();
        }
    }

    // ------------------------------------
    // BUSCAR POR CONTRATO
    // ------------------------------------
    @SuppressWarnings("unchecked")
    public List<Pagamento> buscarPorContrato(Long contratoId) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "FROM Pagamento p WHERE p.contratoLocacao.id = :contratoId ORDER BY p.dataVencimento"
            )
            .setParameter("contratoId", contratoId)
            .getResultList();

        } finally {
            em.close();
        }
    }

    // ------------------------------------
    // BUSCAR TODOS
    // ------------------------------------
    @SuppressWarnings("unchecked")
    public List<Pagamento> buscarTodos() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pagamento p").getResultList();
        } finally {
            em.close();
        }
    }
}
