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
	public Imovel salvar(Imovel imovel) throws PersistenceException {
		
		LOGGER.info("salvar DAO... imovel = " + imovel);
		
		try {
			return manager.merge(imovel);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Transactional
	public void excluir(Imovel imovel) throws PersistenceException {

		try {
			Imovel i = manager.find(Imovel.class, imovel.getId());
			manager.remove(i);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
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
}
