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

    @Transactional
    public void salvar(ContratoLocacao contratoLocacao) throws PersistenceException {
        try {
            manager.merge(contratoLocacao);
        } catch (PersistenceException e) {
            LOGGER.error("Erro ao salvar Contrato", e);
            throw e;

        } finally {
            em.close();
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
                    "LEFT JOIN FETCH c.fiador " +
                    "LEFT JOIN FETCH c.corretor";
        
        Query q = manager.createQuery(query);
        return q.getResultList();
    }
}
