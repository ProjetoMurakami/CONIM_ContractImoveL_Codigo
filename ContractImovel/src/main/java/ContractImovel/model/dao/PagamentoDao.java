package ContractImovel.model.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ContractImovel.model.Pagamento;
import ContractImovel.util.jpa.Transactional;

public class PagamentoDao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Inject
    private EntityManager manager;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PagamentoDao.class);


	@Transactional
	public Pagamento salvar(Pagamento pagamento) throws PersistenceException {
		
		LOGGER.info("salvar DAO... pagamento = " + pagamento);
		
		try {
			return manager.merge(pagamento);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Transactional
	public void excluir(Pagamento pagamento) throws PersistenceException {

		try {
			Pagamento i = manager.find(Pagamento.class, pagamento.getId());
			manager.remove(i);
			manager.flush();
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw e;
		} 
	}

    public Pagamento buscarPeloCodigo(Long id) {
        return manager.find(Pagamento.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Pagamento> buscarTodos() {
        String query="select i from Pagamento i";
		
		Query q = manager.createQuery(query);
		
		return q.getResultList();
    }
}
