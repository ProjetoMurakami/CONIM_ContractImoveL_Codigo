package ContractImovel.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ContractImovel.model.ContratoLocacao;
import ContractImovel.model.dao.contratoLocacaoDao;

public class contratoLocacaoService implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(contratoLocacaoService.class);
    
    @Inject
    private contratoLocacaoDao contratoLocacaoDao;

    public void salvar(ContratoLocacao contratoLocacao){
        contratoLocacaoDao.salvar(contratoLocacao);
    }

    public void excluir(ContratoLocacao contratoLocacao){
        contratoLocacaoDao.excluir(contratoLocacao);
    }

    public List<ContratoLocacao> buscarTodos(){
        return contratoLocacaoDao.buscarTodos();
    }
    
    public ContratoLocacao buscarPorId(Long contratoId) {
        try {
            return contratoLocacaoDao.buscarPeloCodigo(contratoId);
        } catch (Exception e) {
            LOGGER.error("Erro ao buscar pelo id de contrato: " + contratoId, e);
            return null;
        }
    }
}
