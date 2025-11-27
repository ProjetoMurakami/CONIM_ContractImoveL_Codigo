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

public class imovelDao implements Serializable{
    private static final long serialVersionUID = 1L;
    @Inject
    private EntityManager manager;

    private static final Logger LOGGER = LoggerFactory.getLogger(imovelDao.class);
	
	@Transactional 
    public void salvar(Imovel imovel) throws PersistenceException {
        try {
            manager.merge(imovel);
        } catch (PersistenceException e) {
            LOGGER.error("Erro ao salvar imóvel", e);
            throw e;
        }
    }
    
    @Transactional 
    public void excluir(Imovel imovel) throws PersistenceException {
        try {
            Imovel imovelGerenciado = manager.find(Imovel.class, imovel.getId());
            if (imovelGerenciado != null) {
                manager.remove(imovelGerenciado);
                manager.flush();
            } else
                LOGGER.error("Imovel a ser excluído não existe");
        } catch (PersistenceException e) {
            LOGGER.error("Erro ao excluir imóvel ID: " + imovel.getId(), e);
            throw e;
        }
    }
	
	public Imovel buscarPeloCodigo(Long id) {
		return manager.find(Imovel.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Imovel> buscarTodos() {
		
		String query="select i from Imovel i";
		
		Query q = manager.createQuery(query);
		
		return q.getResultList();
	}	

	@SuppressWarnings("unchecked")
    public List<Imovel> buscarDisponiveis() {

        String query = "SELECT i FROM Imovel i " +
                    "WHERE i.statusImovel = 'DISPONIVEL' " +
                    "AND i.id NOT IN (SELECT c.imovel.id FROM ContratoLocacao c)";
        Query q = manager.createQuery(query);

        return q.getResultList();
    }
}