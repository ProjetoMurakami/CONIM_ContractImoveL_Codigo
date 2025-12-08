package ContractImovel.model.dao;

import ContractImovel.model.Pagamento;
import ContractImovel.util.jpa.Transactional;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

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

    @Transactional
    public void salvar(Pagamento pagamento) {
        try {
            manager.merge(pagamento);
        } catch (PersistenceException e) {
            LOGGER.error("Erro ao salvar Pagamento", e);
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
        }
    }

    @Transactional
    public void excluir(Pagamento pagamento) throws PersistenceException {
        try {
            Pagamento pagamengoExcluido = manager.find(Pagamento.class, pagamento.getId());
            if (pagamengoExcluido != null) {
                manager.remove(pagamengoExcluido);
                manager.flush();
            }else
                LOGGER.error("Pagamento a ser excluído não existe");
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
    
    public List<Pagamento> buscarPorContrato(Long contratoId) {
        try {
            TypedQuery<Pagamento> query = manager.createQuery(
                "SELECT p FROM Pagamento p WHERE p.contratoLocacao.id = :contratoId ORDER BY p.dataVencimento", 
                Pagamento.class
            );
            query.setParameter("contratoId", contratoId);
            return query.getResultList();
        } catch (Exception e) {
            LOGGER.error("Erro ao buscar pagamentos do contrato: " + contratoId, e);
            return List.of();
        }
    }
    @SuppressWarnings("unchecked")
    public List<Pagamento> buscarTodos() {
        return manager.createQuery("FROM Pagamento p ORDER BY p.dataVencimento DESC")
                    .getResultList();
    }
    
}
