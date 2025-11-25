package ContractImovel.service;

import ContractImovel.model.Inquilino;
import ContractImovel.model.dao.inquilinoDao;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
public class inquilinoService implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(inquilinoService.class);
    
    @Inject
    private inquilinoDao inquilinoDao;

    public Inquilino buscarPorId(Long id) {
        try {
            return inquilinoDao.buscarPeloCodigo(id);
        } catch (Exception e) {
            LOGGER.error("Erro ao buscar pelo id de inquilino: " + id, e);
            return null;
        }
    }

    public void salvar(Inquilino inquilino) {
        inquilinoDao.salvar(inquilino);
    }

    public void excluir(Inquilino inquilino) {
        inquilinoDao.excluir(inquilino);
    }

    public List<Inquilino> buscarTodos() {
        return inquilinoDao.buscarTodos();
    }
}