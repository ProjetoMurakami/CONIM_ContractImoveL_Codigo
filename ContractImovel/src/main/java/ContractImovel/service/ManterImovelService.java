package ContractImovel.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import ContractImovel.model.Imovel;
import ContractImovel.model.dao.ImovelDao;

public class ManterImovelService implements Serializable{

    private static final long serialVersionUID = 1L;
	@Inject	
	private ImovelDao imovelDao;
	
	public void salvar(Imovel imovel) {
		imovelDao.salvar(imovel);
	}
	
	public void excluir(Imovel imovel) {
		this.imovelDao.excluir(imovel);
	}

	public List<Imovel> buscarTodos() {
		
		return imovelDao.buscarTodos();
	}

}
