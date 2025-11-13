package ContractImovel.model.dao;

import ContractImovel.model.Imovel;
import ContractImovel.enums.StatusImovel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
class imovelDaoTest {

    @PersistenceContext
    private EntityManager em;
    @Inject
    private imovelDao imovelDao;
    private static EntityManagerFactory emf;

    @BeforeEach
    void setUp() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("test-persistence-unit");
        }
        em = emf.createEntityManager();
        imovelDao = new imovelDao();

        try {
            var field = imovelDao.getClass().getDeclaredField("manager");
            field.setAccessible(true);
            field.set(imovelDao, em);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        em.getTransaction().begin();
        em.createQuery("DELETE FROM Imovel").executeUpdate();
        em.getTransaction().commit();
    }

    @Test
    void testSalvarImovel() {

        Imovel imovel = criarImovelExemplo();

        em.getTransaction().begin();
        Imovel imovelSalvo = imovelDao.salvar(imovel);
        em.getTransaction().commit();

        assertNotNull(imovelSalvo.getId());
        assertEquals("12345-678", imovelSalvo.getCEP());
        assertEquals(StatusImovel.DISPONIVEL, imovelSalvo.getStatusImovel());
    }

    @Test
    void testBuscarPeloCodigo() {
        Imovel imovel = criarImovelExemplo();
        em.getTransaction().begin();
        Imovel imovelSalvo = imovelDao.salvar(imovel);
        em.getTransaction().commit();

        Imovel imovelEncontrado = imovelDao.buscarPeloCodigo(imovelSalvo.getId());

        assertNotNull(imovelEncontrado);
        assertEquals(imovelSalvo.getId(), imovelEncontrado.getId());
        assertEquals("São Paulo", imovelEncontrado.getCidade());
    }

    @Test
    void testBuscarTodos() {
        em.getTransaction().begin();
        imovelDao.salvar(criarImovelExemplo());
        imovelDao.salvar(criarImovelExemplo());
        em.getTransaction().commit();

        List<Imovel> imoveis = imovelDao.buscarTodos();

        assertNotNull(imoveis);
        assertEquals(2, imoveis.size());
    }

    @Test
    void testBuscarDisponiveis() {
        em.getTransaction().begin();
        
        Imovel disponivel = criarImovelExemplo();
        disponivel.setStatusImovel(StatusImovel.DISPONIVEL);
        imovelDao.salvar(disponivel);
        
        Imovel alugado = criarImovelExemplo();
        alugado.setStatusImovel(StatusImovel.ALUGADO);
        imovelDao.salvar(alugado);
        
        em.getTransaction().commit();

        List<Imovel> disponiveis = imovelDao.buscarDisponiveis();

        assertNotNull(disponiveis);
        assertEquals(1, disponiveis.size());
        assertEquals(StatusImovel.DISPONIVEL, disponiveis.get(0).getStatusImovel());
    }

    @Test
    void testExcluirImovel() {
        Imovel imovel = criarImovelExemplo();
        em.getTransaction().begin();
        Imovel imovelSalvo = imovelDao.salvar(imovel);
        em.getTransaction().commit();

        Long id = imovelSalvo.getId();

        em.getTransaction().begin();
        imovelDao.excluir(imovelSalvo);
        em.getTransaction().commit();

        Imovel imovelExcluido = em.find(Imovel.class, id);
        assertNull(imovelExcluido);
    }

    private Imovel criarImovelExemplo() {
        Imovel imovel = new Imovel();
        imovel.setCEP("12345-678");
        imovel.setCidade("São Paulo");
        imovel.setBairro("Centro");
        imovel.setEndereco("Rua Teste");
        imovel.setNumero("123");
        imovel.setValorAluguel(1500.0);
        imovel.setStatusImovel(StatusImovel.DISPONIVEL);
        imovel.setQuartos(2);
        imovel.setBanheiros(1);
        imovel.setArea(60.0);
        imovel.setObservacoes("Teste");
        return imovel;
    }
}