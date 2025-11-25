package ContractImovel.service;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ContractImovel.model.Usuario;
import ContractImovel.model.dao.usuarioDao;

@ApplicationScoped
public class usuarioService implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(usuarioService.class);
    
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
        try {
            return usuarioDAO.buscarPorId(id);
        } catch (Exception e) {
            LOGGER.error("Erro ao buscar pelo id de Usuario: " + id, e);
            return null;
        }
    }

    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }
}
