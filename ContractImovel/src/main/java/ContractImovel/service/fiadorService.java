package ContractImovel.service;

import ContractImovel.model.Fiador;
import ContractImovel.model.dao.fiadorDao;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

public class fiadorService implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(fiadorService.class);
    
    @Inject
    private fiadorDao fiadorDao;

    public Fiador buscarPorId(Long id) {
        try {
            return fiadorDao.buscarPeloCodigo(id);
        } catch (Exception e) {
            LOGGER.error("Erro ao buscar pelo id de Fiador: " + id, e);
            return null;
        }
    }

    public void salvar(Fiador fiador) {
        fiadorDao.salvar(fiador);
    }

    public void excluir(Fiador fiador) {
        fiadorDao.excluir(fiador);
    }

    public List<Fiador> buscarTodos() {
        return fiadorDao.buscarTodos();
    }
}