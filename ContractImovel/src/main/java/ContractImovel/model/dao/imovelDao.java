package ContractImovel.model.dao;

import ContractImovel.model.Imovel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

public class imovelDao implements Serializable {

    private static final long serialVersionUID = 1L;

    private static EntityManagerFactory factory = 
            Persistence.createEntityManagerFactory("testePU");

    private EntityManager getManager() {
        return factory.createEntityManager();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(imovelDao.class);
	
	@Transactional 
    public void salvar(Imovel imovel) throws PersistenceException {
        try {
            manager.merge(imovel);
        } catch (PersistenceException e) {
            LOGGER.error("Erro ao salvar imóvel", e);
            throw e;
        } finally {
            manager.close();
        }
    }

    public void excluir(Imovel imovel) {
        EntityManager manager = getManager();
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
        } finally {
            manager.close();
        }
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
