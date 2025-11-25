package ContractImovel.model.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ContractImovel.model.ContratoLocacao;
import ContractImovel.util.jpa.Transactional;

public class contratoLocacaoDao implements Serializable{
    private static final long serialVersionUID = 1L;
    @Inject
    private EntityManager manager;

    private static final Logger LOGGER = LoggerFactory.getLogger(contratoLocacaoDao.class);

    @Transactional
    public void salvar(ContratoLocacao contratoLocacao) throws PersistenceException {
        try {
            manager.merge(contratoLocacao);
        } catch (PersistenceException e) {
            LOGGER.error("Erro ao salvar Contrato", e);
            throw e;
        }
    }

    @Transactional
    public void excluir(ContratoLocacao contratoLocacao) throws PersistenceException {
        try{
            ContratoLocacao contratoExcluir = manager.find(ContratoLocacao.class, contratoLocacao.getId());
            if(contratoExcluir  != null){
                manager.remove(contratoExcluir);
                manager.flush();
            } else
                LOGGER.error("Contrato a ser excluído não existe");
        } catch (PersistenceException e){
            LOGGER.error("Erro ao excluir Contrato ID: " + contratoLocacao.getId(), e);
            throw e;
        }
    }

    public ContratoLocacao buscarPeloCodigo(Long id) {
        return manager.find(ContratoLocacao.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<ContratoLocacao> buscarTodos() {
        String query = "SELECT c FROM ContratoLocacao c " +
                    "LEFT JOIN FETCH c.imovel " +
                    "LEFT JOIN FETCH c.inquilino " +
                    "LEFT JOIN FETCH c.fiador " +
                    "LEFT JOIN FETCH c.corretor";
        
        Query q = manager.createQuery(query);
        return q.getResultList();
    }
}
