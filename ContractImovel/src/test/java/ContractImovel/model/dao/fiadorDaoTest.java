package ContractImovel.model.dao;

import ContractImovel.model.Fiador;
import ContractImovel.enums.TiposCliente;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Field;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class fiadorDaoTest {

    private EntityManager em;
    private fiadorDao fiadorDao;
    private EntityManagerFactory emf;

    @BeforeAll
    void setUpAll() {
        emf = Persistence.createEntityManagerFactory("test-persistence-unit");
    }

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        fiadorDao = new fiadorDao();

        try {
            Field field = fiadorDao.getClass().getDeclaredField("manager");
            field.setAccessible(true);
            field.set(fiadorDao, em);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao injetar EntityManager", e);
        }

        em.getTransaction().begin();
        em.createQuery("DELETE FROM Fiador").executeUpdate();
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
    void testSalvarFiador() {

        Fiador fiador = criarFiadorExemplo();

        em.getTransaction().begin();
        Fiador fiadorSalvo = fiadorDao.salvar(fiador);
        em.getTransaction().commit();

        assertNotNull(fiadorSalvo.getId());
        assertEquals("João Silva", fiadorSalvo.getNome());
        assertEquals("12345678901", fiadorSalvo.getCpf());
        assertEquals(TiposCliente.PESSOA_FISICA, fiadorSalvo.getCategoria());
    }

    @Test
    void testAtualizarFiador() {

        Fiador fiador = criarFiadorExemplo();
        em.getTransaction().begin();
        Fiador fiadorSalvo = fiadorDao.salvar(fiador);
        em.getTransaction().commit();

        fiadorSalvo.setNome("João Silva Atualizado");
        fiadorSalvo.setRendaMensal(6000.0);

        em.getTransaction().begin();
        Fiador fiadorAtualizado = fiadorDao.salvar(fiadorSalvo);
        em.getTransaction().commit();

        assertEquals("João Silva Atualizado", fiadorAtualizado.getNome());
        assertEquals(6000.0, fiadorAtualizado.getRendaMensal());
        assertEquals(fiadorSalvo.getId(), fiadorAtualizado.getId());
    }

    @Test
    void testBuscarPeloCodigo() {
        Fiador fiador = criarFiadorExemplo();
        em.getTransaction().begin();
        Fiador fiadorSalvo = fiadorDao.salvar(fiador);
        em.getTransaction().commit();

        Fiador fiadorEncontrado = fiadorDao.buscarPeloCodigo(fiadorSalvo.getId());

        assertNotNull(fiadorEncontrado);
        assertEquals(fiadorSalvo.getId(), fiadorEncontrado.getId());
        assertEquals("João Silva", fiadorEncontrado.getNome());
    }

    @Test
    void testBuscarTodos() {
        em.getTransaction().begin();
        fiadorDao.salvar(criarFiadorExemplo());
        fiadorDao.salvar(criarFiadorExemplo());
        em.getTransaction().commit();

        List<Fiador> fiadores = fiadorDao.buscarTodos();

        assertNotNull(fiadores);
        assertEquals(2, fiadores.size());
    }

    @Test
    void testExcluirFiador() {

        Fiador fiador = criarFiadorExemplo();
        em.getTransaction().begin();
        Fiador fiadorSalvo = fiadorDao.salvar(fiador);
        em.getTransaction().commit();

        Long id = fiadorSalvo.getId();

        em.getTransaction().begin();
        fiadorDao.excluir(fiadorSalvo);
        em.getTransaction().commit();

        Fiador fiadorExcluido = em.find(Fiador.class, id);
        assertNull(fiadorExcluido);
    }

    @Test
    void testBuscarPeloCodigoNaoExistente() {

        Fiador fiador = fiadorDao.buscarPeloCodigo(999L);

        assertNull(fiador);
    }

    @Test
    void testSalvarFiadorCompleto() {
        Fiador fiador = new Fiador();
        fiador.setNome("Maria Santos");
        fiador.setCpf("98765432100");
        fiador.setEmail("maria@example.com");
        fiador.setTelefone("11988887777");
        fiador.setCEP("54321-876");
        fiador.setBairro("Jardim das Flores");
        fiador.setEndereco("Avenida Principal");
        fiador.setNumero("456");
        fiador.setRendaMensal(7000.0);
        fiador.setObservacoes("Excelente fiador");
        fiador.setCategoria(TiposCliente.PESSOA_JURIDICA);

        em.getTransaction().begin();
        Fiador fiadorSalvo = fiadorDao.salvar(fiador);
        em.getTransaction().commit();

        assertNotNull(fiadorSalvo.getId());
        assertEquals("Maria Santos", fiadorSalvo.getNome());
        assertEquals("maria@example.com", fiadorSalvo.getEmail());
        assertEquals("11988887777", fiadorSalvo.getTelefone());
        assertEquals("54321-876", fiadorSalvo.getCEP());
        assertEquals("Jardim das Flores", fiadorSalvo.getBairro());
        assertEquals("Avenida Principal", fiadorSalvo.getEndereco());
        assertEquals("456", fiadorSalvo.getNumero());
        assertEquals(7000.0, fiadorSalvo.getRendaMensal());
        assertEquals("Excelente fiador", fiadorSalvo.getObservacoes());
        assertEquals(TiposCliente.PESSOA_JURIDICA, fiadorSalvo.getCategoria());
    }

    private Fiador criarFiadorExemplo() {
        Fiador fiador = new Fiador();
        fiador.setNome("João Silva");
        fiador.setCpf("12345678901");
        fiador.setEmail("joao@example.com");
        fiador.setTelefone("11999999999");
        fiador.setCEP("12345-678");
        fiador.setBairro("Centro");
        fiador.setEndereco("Rua das Flores");
        fiador.setNumero("123");
        fiador.setRendaMensal(5000.0);
        fiador.setObservacoes("Fiador responsável");
        fiador.setCategoria(TiposCliente.PESSOA_FISICA);
        return fiador;
    }
}