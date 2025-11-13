package ContractImovel.service;

import ContractImovel.model.Imovel;
import ContractImovel.model.dao.imovelDao;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

public class imovelService  {
    
    
    @Inject
    private imovelDao imovelDao;

    public Imovel buscarPorId(Long id) {
        try {
            return imovelDao.buscarPeloCodigo(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public Imovel salvar(Imovel imovel) {
        try {
            return imovelDao.salvar(imovel);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar imóvel: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void excluir(Imovel imovel) {
        try {
            imovelDao.excluir(imovel);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao excluir imóvel: " + e.getMessage(), e);
        }
    }

    public List<Imovel> buscarTodos() {
        return imovelDao.buscarTodos();
    }

    public List<Imovel> buscarDisponiveis(){
        return imovelDao.buscarDisponiveis();
    }
}