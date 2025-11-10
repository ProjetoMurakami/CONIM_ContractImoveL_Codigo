package ContractImovel.model.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ContractImovel.model.Fiador;
import ContractImovel.util.jpa.Transactional;

public class fiadorDao implements Serializable{
    private static final long serialVersionUID = 1L;
    @Inject
    private EntityManager manager;

    private static final Logger LOGGER = LoggerFactory.getLogger(fiadorDao.class);

    @Transactional
    public Fiador salvar(Fiador fiador) throws PersistenceException {

        LOGGER.info("Salvar DAO... Contrato = " + fiador);

        try {
            return manager.merge(fiador);
        } catch (PersistenceException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public void excluir(Fiador fiador) throws PersistenceException {
        
        try{
            Fiador f = manager.find(Fiador.class, fiador.getId());
            manager.remove(f);
            manager.flush();
        } catch (PersistenceException e){
            e.printStackTrace();
            throw e;
        }
    }

    public Fiador buscarPeloCodigo(Long id) {
        return manager.find(Fiador.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Fiador> buscarTodos() {
        String query="select c from Fiador c";

        Query q = manager.createQuery(query);

        return q.getResultList();
    }
}
