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
	public void salvar(Inquilino inquilino) {
		try {
			manager.merge(inquilino);
		} catch (PersistenceException e) {
			LOGGER.error("Erro ao salvar Inquilino", e);
			throw e;
		}
	}


    @Transactional
    public void excluir(Inquilino inquilino) throws PersistenceException{

        try {
			Inquilino inquilinoExcluido = manager.find(Inquilino.class, inquilino.getId());
			if(inquilinoExcluido != null){
				manager.remove(inquilinoExcluido);
				manager.flush();
			} else
				LOGGER.error("Inquilino a ser excluído não existe");	
		} catch (PersistenceException e) {
			LOGGER.error("Erro ao excluir Inquilino ID: " + inquilino.getId(), e);
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

	@SuppressWarnings("unchecked")
	public List<Inquilino> buscarDisponiveis(){
		String query = "SELECT i FROM Inquilino i WHERE i.id NOT IN (SELECT c.inquilino.id FROM ContratoLocacao c)";

		Query q = manager.createQuery(query);

		return q.getResultList();
	}
}
