package ContractImovel.model.dao;

import ContractImovel.model.Pagamento;
import ContractImovel.util.jpa.Transactional;

import javax.inject.Inject;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

public class pagamentoDao implements Serializable{
    private static final long serialVersionUID = 1L;
    @Inject
    private EntityManager manager;

    private static final Logger LOGGER = LoggerFactory.getLogger(pagamentoDao.class);

    @Transactional
    public Pagamento salvar(Pagamento pagamento) {
        try {
            if (pagamento.getId() == null) {
                manager.persist(pagamento);
                return pagamento;
            } else {
                return manager.merge(pagamento);
            }
        } catch (PersistenceException e) {
            LOGGER.error("Erro no DAO ao salvar Pagamento", e);
            throw e;
        }
    }

    @Transactional
    public void excluir(Pagamento pagamento) throws PersistenceException {
        
        try {
            Pagamento pag = manager.find(Pagamento.class, pagamento.getId());
            if (pag != null) {
                manager.remove(pag);
                manager.flush();
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Pagamento buscarPorId(Long id) {
        return manager.find(Pagamento.class, id);
    }
    
    @SuppressWarnings("unchecked")
    public List<Pagamento> buscarPorContrato(Long contratoId) {
        return manager.createQuery("FROM Pagamento p WHERE p.contratoLocacao.id = :contratoId ORDER BY p.dataVencimento")
                     .setParameter("contratoId", contratoId)
                     .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Pagamento> buscarTodos() {
        String query ="select p from Pagamento p";

        Query q = manager.createQuery(query);

        return q.getResultList();
    }
}