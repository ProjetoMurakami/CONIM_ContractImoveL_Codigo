package ContractImovel.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import ContractImovel.model.ContratoLocacao;
import ContractImovel.model.dao.contratoLocacaoDao;

public class contratoLocacaoService implements Serializable{
    
    private static final long serialVersionUID = 1L;
    @Inject
    private contratoLocacaoDao contratoLocacaoDao;

    public void salvar(ContratoLocacao contratoLocacao){
        contratoLocacaoDao.salvar(contratoLocacao);
    }

    public void excluir(ContratoLocacao contratoLocacao){
        this.contratoLocacaoDao.excluir(contratoLocacao);
    }

    public List<ContratoLocacao> buscarTodos(){
        return contratoLocacaoDao.buscarTodos();
    }

    public ContratoLocacao buscarPorId(Long contratoId) {
        ContratoLocacao contrato = contratoLocacaoDao.buscarPeloCodigo(contratoId);
        if (contrato == null){
            throw new RuntimeException("Contrato não encontrado com o ID: " + contratoId);
        }
        return contratoLocacaoDao.buscarPeloCodigo(contratoId);
    }

}
