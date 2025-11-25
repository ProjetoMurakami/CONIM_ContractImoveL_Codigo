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
    public void salvar(Fiador fiador) throws PersistenceException {
        try {
            manager.merge(fiador);
        } catch (PersistenceException e) {
            LOGGER.error("Erro ao salvar Fiador", e);
            throw e;
        }
    }

    @Transactional
    public void excluir(Fiador fiador) throws PersistenceException {
        
        try{
            Fiador fiadorExcluido = manager.find(Fiador.class, fiador.getId());
            if(fiadorExcluido != null){
                manager.remove(fiadorExcluido);
                manager.flush();
            }else
                LOGGER.error("Fiador a ser excluído não existe");
        } catch (PersistenceException e){
            LOGGER.error("Erro ao excluir fiador ID: " + fiador.getId(), e);
            throw e;
        }
    }

    public Fiador buscarPeloCodigo(Long id) {
        return manager.find(Fiador.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Fiador> buscarTodos() {
        String query="select f from Fiador f";

        Query q = manager.createQuery(query);

        return q.getResultList();
    }
}
