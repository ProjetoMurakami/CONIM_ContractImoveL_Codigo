package ContractImovel.model.dao;

import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ContractImovel.model.Usuario;
import ContractImovel.util.jpa.Transactional;

@Named
public class usuarioDao implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager manager;

    private static final Logger LOGGER = LoggerFactory.getLogger(usuarioDao.class);

    @Transactional
    public void salvar(Usuario usuario) {
        try{
            manager.merge(usuario);
        } catch (PersistenceException e) {
            LOGGER.error("Erro no DAO ao salvar Pagamento", e);
            throw e;
        }
    }

    @Transactional
    public void excluir(Usuario usuario) throws PersistenceException {
        
        try {
            Usuario user = manager.find(Usuario.class, usuario.getId());
            if (user != null) {
                manager.remove(user);
                manager.flush();
            }else
                LOGGER.error("Usuario a ser excluído não existe");
        } catch (PersistenceException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Usuario buscarPorId(Long id) {
        return manager.find(Usuario.class, id);
    }

    public java.util.List<Usuario> listarTodos() {
        return manager.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }

    public Usuario buscarPorUsernameESenha(String username, String senha) {
        try {
            return manager.createQuery(
        "SELECT u FROM Usuario u WHERE u.username = :username AND u.senha = :senha", Usuario.class)
                    .setParameter("username", username)
                    .setParameter("senha", senha)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
