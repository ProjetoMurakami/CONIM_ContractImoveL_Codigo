package ContractImovel.model.dao;

import ContractImovel.model.Imovel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

public class imovelDao implements Serializable {

    private static final long serialVersionUID = 1L;

    private static EntityManagerFactory factory = 
            Persistence.createEntityManagerFactory("testePU");

    private EntityManager getManager() {
        return factory.createEntityManager();
    }

    public Imovel salvar(Imovel imovel) {
        EntityManager manager = getManager();
        try {
            manager.getTransaction().begin();
            Imovel salvo = manager.merge(imovel);
            manager.getTransaction().commit();
            return salvo;
        } catch (Exception e) {
            manager.getTransaction().rollback();
            throw e;
        } finally {
            manager.close();
        }
    }

    public void excluir(Imovel imovel) {
        EntityManager manager = getManager();
        try {
            manager.getTransaction().begin();
            Imovel gerenciado = manager.find(Imovel.class, imovel.getId());
            if (gerenciado != null) {
                manager.remove(gerenciado);
            }
            manager.getTransaction().commit();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            throw e;
        } finally {
            manager.close();
        }
    }

    public Imovel buscarPeloCodigo(Long id) {
        EntityManager manager = getManager();
        try {
            return manager.find(Imovel.class, id);
        } finally {
            manager.close();
        }
    }

    public List<Imovel> buscarTodos() {
        EntityManager manager = getManager();
        try {
            return manager.createQuery("select i from Imovel i", Imovel.class)
                    .getResultList();
        } finally {
            manager.close();
        }
    }

    public List<Imovel> buscarDisponiveis() {
        EntityManager manager = getManager();
        try {
            return manager.createQuery(
                    "select i from Imovel i where i.statusImovel = 'DISPONIVEL'", 
                    Imovel.class
            ).getResultList();
        } finally {
            manager.close();
        }
    }
}
