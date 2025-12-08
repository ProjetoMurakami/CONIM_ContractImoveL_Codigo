package ContractImovel.model.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import ContractImovel.model.Inquilino;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class inquilinoDao implements Serializable {
    private static final long serialVersionUID = 1L;

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

    // ------------------------------
    // EXCLUIR
    // ------------------------------
    public void excluir(Inquilino inquilino) {
        EntityManager em = getEntityManager();

        try {
            em.getTransaction().begin();

            Inquilino i = em.find(Inquilino.class, inquilino.getId());
            if (i != null) {
                em.remove(i);
            }

            em.getTransaction().commit();

        } catch (Exception e) {
            LOGGER.error("Erro ao excluir inquilino ID: " + inquilino.getId(), e);
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;

        } finally {
            em.close();
        }
    }

    // ------------------------------
    // BUSCAR POR ID
    // ------------------------------
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
