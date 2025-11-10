package ContractImovel.service;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import ContractImovel.model.Usuario;
import ContractImovel.model.dao.usuarioDao;

@ApplicationScoped
public class usuarioService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private usuarioDao usuarioDAO;

    public Usuario autenticar(String username, String senha) {
        return usuarioDAO.buscarPorUsernameESenha(username, senha);
    }

    @Transactional
    public void salvar(Usuario usuario) {
        usuarioDAO.salvar(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }
}
