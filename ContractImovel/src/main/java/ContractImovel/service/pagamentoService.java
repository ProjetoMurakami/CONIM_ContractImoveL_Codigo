package ContractImovel.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import ContractImovel.model.Pagamento;
import ContractImovel.model.dao.PagamentoDao;

public class pagamentoService implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private PagamentoDao pagamentoDao;
    
    public List<Pagamento> listarTodos() {
        return pagamentoDao.buscarTodos();
    }
    
    public Pagamento buscarPorId(Long id) {
        Pagamento pagamento = pagamentoDao.buscarPeloCodigo(id);
        if (pagamento == null) {
            throw new RuntimeException("Pagamento não encontrado com o ID: " + id);
        }
        return pagamento;
    }
    
    public Pagamento salvar(Pagamento pagamento) {
        return pagamentoDao.salvar(pagamento);
    }
    
    public void excluir(Pagamento pagamento) {
        pagamentoDao.excluir(pagamento);
    }
    
    public void excluirPorId(Long id) {
        Pagamento pagamento = buscarPorId(id);
        pagamentoDao.excluir(pagamento);
    }
}