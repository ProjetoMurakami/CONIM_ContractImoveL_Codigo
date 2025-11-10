package ContractImovel.service;

import ContractImovel.model.Inquilino;
import ContractImovel.model.dao.inquilinoDao;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;
public class inquilinoService implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private inquilinoDao inquilinoDao;

    public Inquilino buscarPorId(Long id) {
        try {
            return inquilinoDao.buscarPeloCodigo(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Inquilino salvar(Inquilino inquilino) {
        return inquilinoDao.salvar(inquilino);
    }

    @Transactional
    public void excluir(Inquilino inquilino) {
        inquilinoDao.excluir(inquilino);
    }

    public List<Inquilino> buscarTodos() {
        return inquilinoDao.buscarTodos();
    }
}