package ContractImovel.model.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ContractImovel.model.Inquilino;
import ContractImovel.util.jpa.Transactional;

public class inquilinoDao implements Serializable{
    private static final long serialVersionUID = 1L;
    @Inject
    private EntityManager manager;

    private static final Logger LOGGER = LoggerFactory.getLogger(inquilinoDao.class);

    @Transactional
    public Inquilino salvar(Inquilino inquilino) throws PersistenceException{

        LOGGER.info("salvar DAO... pagamento = " + inquilino);

        try {
			return manager.merge(inquilino);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw e;
		}
    }

    @Transactional
    public void excluir(Inquilino inquilino) throws PersistenceException{

        try {
			Inquilino i = manager.find(Inquilino.class, inquilino.getId());
			manager.remove(i);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw e;
		} 
    }

    public Inquilino buscarPeloCodigo(Long id) {
		return manager.find(Inquilino.class, id);
	}

     @SuppressWarnings("unchecked")
     public List<Inquilino> buscarTodos() {
		String query="select i from Inquilino i";
		
		Query q = manager.createQuery(query);
		
		return q.getResultList();
	}	
}
