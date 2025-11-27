package ContractImovel.model.dao;

import ContractImovel.model.Pagamento;
import ContractImovel.util.jpa.Transactional;

import javax.inject.Inject;
import javax.persistence.TypedQuery;
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
    public void salvar(Pagamento pagamento) {
        try {
            manager.merge(pagamento);
        } catch (PersistenceException e) {
            LOGGER.error("Erro no DAO ao salvar Pagamento", e);
            throw e;
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
            e.printStackTrace();
            throw e;
        }
    }

    public Pagamento buscarPorId(Long id) {
        return manager.find(Pagamento.class, id);
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