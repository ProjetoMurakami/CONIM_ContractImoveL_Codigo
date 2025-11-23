package ContractImovel.service;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ContractImovel.model.Usuario;
import ContractImovel.model.dao.usuarioDao;

@ApplicationScoped
public class usuarioService implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private usuarioDao usuarioDAO;

    public Usuario autenticar(String username, String senha) {
        Usuario user = usuarioDAO.buscarPorUsernameESenha(username, senha);

        if(user == null){
            return null;
        }
        return user;
    }

    public void salvar(Usuario usuario) {
        usuarioDAO.salvar(usuario);
    }

    public void excluir(Usuario usuario) {
        usuarioDAO.excluir(usuario);
    }

    public Usuario buscarPorId(Long id) {
        return usuarioDAO.buscarPorId(id);
    }

    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }
}
