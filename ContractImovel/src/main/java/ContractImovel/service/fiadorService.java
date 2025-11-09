package ContractImovel.service;

import ContractImovel.model.Fiador;
import ContractImovel.model.dao.fiadorDao;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

public class fiadorService implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private fiadorDao fiadorDao;

    public Fiador buscarPorId(Long id) {
        try {
            return fiadorDao.buscarPeloCodigo(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Fiador salvar(Fiador fiador) {
        return fiadorDao.salvar(fiador);
    }

    @Transactional
    public void excluir(Fiador fiador) {
        fiadorDao.excluir(fiador);
    }

    public List<Fiador> buscarTodos() {
        return fiadorDao.buscarTodos();
    }
}