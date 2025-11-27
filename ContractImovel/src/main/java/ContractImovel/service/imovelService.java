package ContractImovel.service;

import ContractImovel.model.Imovel;
import ContractImovel.model.dao.imovelDao;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

public class imovelService implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(imovelService.class);
    
    @Inject
    private imovelDao imovelDao;

    public Imovel buscarPorId(Long id) {
        try {
            return imovelDao.buscarPeloCodigo(id);
        } catch (Exception e) {
            LOGGER.error("Erro ao buscar pelo id de Imovel: " + id, e);
            return null;
        }
    }

    public void salvar(Imovel imovel) {
        try {
            imovelDao.salvar(imovel);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar imóvel: " + e.getMessage(), e);
        }
    }

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