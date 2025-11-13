package ContractImovel.model.dao;

import ContractImovel.model.Inquilino;
import ContractImovel.enums.TiposCliente;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Field;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class inquilinoDaoTest {

    private EntityManager em;
    private inquilinoDao inquilinoDao;
    private EntityManagerFactory emf;

    @BeforeAll
    void setUpAll() {
        emf = Persistence.createEntityManagerFactory("test-persistence-unit");
    }

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        inquilinoDao = new inquilinoDao();

        try {
            Field field = inquilinoDao.getClass().getDeclaredField("manager");
            field.setAccessible(true);
            field.set(inquilinoDao, em);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao injetar EntityManager", e);
        }

        em.getTransaction().begin();
        em.createQuery("DELETE FROM Inquilino").executeUpdate();
        em.getTransaction().commit();
    }

    @AfterEach
    void tearDown() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }

    @AfterAll
    void tearDownAll() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    void testSalvarNovoInquilino() {
        Inquilino inquilino = criarInquilinoExemplo();

        em.getTransaction().begin();
        Inquilino inquilinoSalvo = inquilinoDao.salvar(inquilino);
        em.getTransaction().commit();

        assertNotNull(inquilinoSalvo.getId());
        assertEquals("João Silva", inquilinoSalvo.getNome());
        assertEquals("12345678901", inquilinoSalvo.getCpf());
        assertEquals(TiposCliente.PESSOA_FISICA, inquilinoSalvo.getCategoria());
    }

    @Test
    void testAtualizarInquilino() {
        Inquilino inquilino = criarInquilinoExemplo();
        em.getTransaction().begin();
        Inquilino inquilinoSalvo = inquilinoDao.salvar(inquilino);
        em.getTransaction().commit();

        inquilinoSalvo.setNome("João Silva Atualizado");
        inquilinoSalvo.setRendaMensal(6000.0);

        em.getTransaction().begin();
        Inquilino inquilinoAtualizado = inquilinoDao.salvar(inquilinoSalvo);
        em.getTransaction().commit();

        assertEquals("João Silva Atualizado", inquilinoAtualizado.getNome());
        assertEquals(6000.0, inquilinoAtualizado.getRendaMensal());
        assertEquals(inquilinoSalvo.getId(), inquilinoAtualizado.getId());
    }

    @Test
    void testBuscarPeloCodigo() {

        Inquilino inquilino = criarInquilinoExemplo();
        em.getTransaction().begin();
        Inquilino inquilinoSalvo = inquilinoDao.salvar(inquilino);
        em.getTransaction().commit();

        Inquilino inquilinoEncontrado = inquilinoDao.buscarPeloCodigo(inquilinoSalvo.getId());

        assertNotNull(inquilinoEncontrado);
        assertEquals(inquilinoSalvo.getId(), inquilinoEncontrado.getId());
        assertEquals("João Silva", inquilinoEncontrado.getNome());
    }

    @Test
    void testBuscarTodos() {
        em.getTransaction().begin();
        inquilinoDao.salvar(criarInquilinoExemplo());
        inquilinoDao.salvar(criarInquilinoExemplo());
        em.getTransaction().commit();

        List<Inquilino> inquilinos = inquilinoDao.buscarTodos();

        assertNotNull(inquilinos);
        assertEquals(2, inquilinos.size());
    }

    @Test
    void testExcluirInquilino() {

        Inquilino inquilino = criarInquilinoExemplo();
        em.getTransaction().begin();
        Inquilino inquilinoSalvo = inquilinoDao.salvar(inquilino);
        em.getTransaction().commit();

        Long id = inquilinoSalvo.getId();

        em.getTransaction().begin();
        inquilinoDao.excluir(inquilinoSalvo);
        em.getTransaction().commit();

        Inquilino inquilinoExcluido = em.find(Inquilino.class, id);
        assertNull(inquilinoExcluido);
    }

    @Test
    void testBuscarPeloCodigoNaoExistente() {

        Inquilino inquilino = inquilinoDao.buscarPeloCodigo(999L);

        assertNull(inquilino);
    }

    private Inquilino criarInquilinoExemplo() {
        Inquilino inquilino = new Inquilino();
        inquilino.setNome("João Silva");
        inquilino.setEmail("joao@example.com");
        inquilino.setCpf("12345678901");
        inquilino.setTelefone("11999999999");
        inquilino.setRendaMensal(5000.0);
        inquilino.setCategoria(TiposCliente.PESSOA_FISICA);
        return inquilino;
    }
}