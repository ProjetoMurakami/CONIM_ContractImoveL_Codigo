package ContractImovel.model.dao;

import java.io.Serializable;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ContractImovel.model.Imovel;
import ContractImovel.util.jpa.Transactional;

public class imovelDao implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Inject
    private EntityManager manager;

    private static final Logger LOGGER = LoggerFactory.getLogger(imovelDao.class);
    
    @Transactional 
    public Imovel salvar(Imovel imovel) throws PersistenceException {
        LOGGER.info("salvar DAO... imovel = {}", imovel);
        try {
            Imovel imovelSalvo = manager.merge(imovel);
            manager.flush(); 
            LOGGER.info("Imóvel salvo com ID: {}", imovelSalvo.getId());
            return imovelSalvo;
        } catch (PersistenceException e) {
            LOGGER.error("Erro ao salvar imóvel", e);
            throw e;
        }
    }
    
    @Transactional 
    public void excluir(Imovel imovel) throws PersistenceException {
        LOGGER.info("Excluir DAO... imovel = {}", imovel);
        try {
            // Recarrega a entidade para garantir que está no contexto de persistência
            Imovel imovelGerenciado = manager.find(Imovel.class, imovel.getId());
            if (imovelGerenciado != null) {
                manager.remove(imovelGerenciado);
                manager.flush();
                LOGGER.info("Imóvel excluído com ID: {}", imovel.getId());
            } else {
                LOGGER.warn("Imóvel não encontrado para exclusão ID: {}", imovel.getId());
            }
        } catch (PersistenceException e) {
            LOGGER.error("Erro ao excluir imóvel ID: {}", imovel.getId(), e);
            throw e;
        }
    }
    
    public Imovel buscarPeloCodigo(Long id) {
        return manager.find(Imovel.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Imovel> buscarTodos() {
        String query = "SELECT i FROM Imovel i";
        Query q = manager.createQuery(query);
        return q.getResultList();
    }    

    @SuppressWarnings("unchecked")
    public List<Imovel> buscarDisponiveis() {
        String query = "SELECT i FROM Imovel i WHERE i.statusImovel = 'DISPONIVEL'";
        Query q = manager.createQuery(query);
        return q.getResultList();
    }
}