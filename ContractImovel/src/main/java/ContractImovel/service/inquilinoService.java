package ContractImovel.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import ContractImovel.model.Inquilino;
import ContractImovel.model.dao.InquilinoDao;

public class inquilinoService implements Serializable{
    
    private static final long serialVersionUID = 1L;
	@Inject	
	private InquilinoDao inquilinoDao;

    public void salvar(Inquilino inquilino){
        inquilinoDao.salvar(inquilino);
    }

    public void excluir(Inquilino inquilino){
        this.inquilinoDao.excluir(inquilino);
    }

    public List<Inquilino> buscarTodos() {
		
		return inquilinoDao.buscarTodos();
	}
}
