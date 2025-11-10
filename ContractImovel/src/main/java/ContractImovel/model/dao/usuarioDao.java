package ContractImovel.model.dao;

import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import ContractImovel.model.Usuario;

@Named
public class usuarioDao implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager em;

    public Usuario buscarPorUsernameESenha(String username, String senha) {
        try {
            return em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.username = :username AND u.senha = :senha", Usuario.class)
                    .setParameter("username", username)
                    .setParameter("senha", senha)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void salvar(Usuario usuario) {
        if (usuario.getId() == null) {
            em.persist(usuario);
        } else {
            em.merge(usuario);
        }
    }

    public java.util.List<Usuario> listarTodos() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }
}
