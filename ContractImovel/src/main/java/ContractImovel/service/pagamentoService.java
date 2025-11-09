package ContractImovel.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import ContractImovel.model.Pagamento;
import ContractImovel.model.dao.pagamentoDao;
public class pagamentoService implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Inject
    private pagamentoDao pagamentoDao;

    public void salvar(Pagamento pagamento) {
        if (pagamento.getContratoLocacao() == null) {
            throw new IllegalArgumentException("Pagamento precisa estar vinculado a um contrato.");
        }
        
        pagamentoDao.salvar(pagamento);
    }

    public void excluir(Pagamento pagamento) {
        pagamentoDao.excluir(pagamento);
    }

    public Pagamento buscarPorId(Long id) {
        return pagamentoDao.buscarPorId(id);
    }
    
    public List<Pagamento> buscarPorContrato(Long contratoId) {
        return pagamentoDao.buscarPorContrato(contratoId);
    }

    public List<Pagamento> buscarTodos() {
        return pagamentoDao.buscarTodos();
    }
}