package ContractImovel.model.dao;

import ContractImovel.model.*;
import ContractImovel.enums.StatusImovel;
import ContractImovel.enums.TiposCliente;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class contratoLocacaoDaoTest {

    private EntityManager em;
    private contratoLocacaoDao contratoDao;
    private EntityManagerFactory emf;

    @BeforeAll
    void setUpAll() {
        emf = Persistence.createEntityManagerFactory("test-persistence-unit");
    }

    @BeforeEach
    void setUp() {
        em = emf.createEntityManager();
        contratoDao = new contratoLocacaoDao();

        try {
            Field field = contratoDao.getClass().getDeclaredField("manager");
            field.setAccessible(true);
            field.set(contratoDao, em);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao injetar EntityManager", e);
        }

        em.getTransaction().begin();
        em.createQuery("DELETE FROM Pagamento").executeUpdate();
        em.createQuery("DELETE FROM ContratoLocacao").executeUpdate();
        em.createQuery("DELETE FROM Fiador").executeUpdate();
        em.createQuery("DELETE FROM Inquilino").executeUpdate();
        em.createQuery("DELETE FROM Imovel").executeUpdate();
        em.createQuery("DELETE FROM Usuario").executeUpdate();
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
    void testSalvarContratoLocacao() {
        ContratoLocacao contrato = criarContratoCompleto();

        em.getTransaction().begin();
        ContratoLocacao contratoSalvo = contratoDao.salvar(contrato);
        em.getTransaction().commit();

        assertNotNull(contratoSalvo.getId());
        assertEquals(1500.0, contratoSalvo.getValorAluguel());
        assertNotNull(contratoSalvo.getImovel());
        assertNotNull(contratoSalvo.getInquilino());
    }

    @Test
    void testBuscarPeloCodigo() {
        ContratoLocacao contrato = criarContratoCompleto();
        
        em.getTransaction().begin();
        ContratoLocacao contratoSalvo = contratoDao.salvar(contrato);
        em.getTransaction().commit();

        ContratoLocacao contratoEncontrado = contratoDao.buscarPeloCodigo(contratoSalvo.getId());

        assertNotNull(contratoEncontrado);
        assertEquals(contratoSalvo.getId(), contratoEncontrado.getId());
        assertEquals(1500.0, contratoEncontrado.getValorAluguel());
        assertNotNull(contratoEncontrado.getImovel());
        assertNotNull(contratoEncontrado.getInquilino());
    }

    @Test
    void testExcluirContratoLocacao() {
        ContratoLocacao contrato = criarContratoCompleto();
        
        em.getTransaction().begin();
        ContratoLocacao contratoSalvo = contratoDao.salvar(contrato);
        em.getTransaction().commit();

        Long id = contratoSalvo.getId();

        em.getTransaction().begin();
        contratoDao.excluir(contratoSalvo);
        em.getTransaction().commit();

        ContratoLocacao contratoExcluido = em.find(ContratoLocacao.class, id);
        assertNull(contratoExcluido);
    }

    @Test
    void testBuscarTodos() {
        ContratoLocacao contrato1 = criarContratoCompleto();
        ContratoLocacao contrato2 = criarContratoCompleto();
        contrato2.setValorAluguel(2000.0);
        
        em.getTransaction().begin();
        contratoDao.salvar(contrato1);
        contratoDao.salvar(contrato2);
        em.getTransaction().commit();

        List<ContratoLocacao> contratos = contratoDao.buscarTodos();

        assertNotNull(contratos);
        assertEquals(2, contratos.size());
    }

    private ContratoLocacao criarContratoCompleto() {
        Imovel imovel = criarImovelExemplo();
        Inquilino inquilino = criarInquilinoExemplo();
        Fiador fiador = criarFiadorExemplo();

        em.getTransaction().begin();
        em.persist(imovel);
        em.persist(inquilino);
        em.persist(fiador);
        em.getTransaction().commit();

        ContratoLocacao contrato = new ContratoLocacao();
        contrato.setDataInicio(LocalDate.now());
        contrato.setDataFinal(LocalDate.now().plusYears(1));
        contrato.setValorAluguel(1500.0);
        contrato.setImovel(imovel);
        contrato.setInquilino(inquilino);
        contrato.setFiador(fiador);

        return contrato;
    }

    private Imovel criarImovelExemplo() {
        Imovel imovel = new Imovel();
        imovel.setEndereco("Rua Exemplo, 123");
        imovel.setNumero("123");
        imovel.setBairro("Centro");
        imovel.setCidade("São Paulo");
        imovel.setCEP("01234-567");
        imovel.setArea(80.0);
        imovel.setQuartos(3);
        imovel.setBanheiros(2);
        imovel.setValorAluguel(1500.0);
        imovel.setStatusImovel(StatusImovel.DISPONIVEL);
        imovel.setObservacoes("Apartamento bem localizado");
        return imovel;
    }

    private Inquilino criarInquilinoExemplo() {
        Inquilino inquilino = new Inquilino();
        inquilino.setNome("João Silva");
        inquilino.setCpf("123.456.789-00");
        inquilino.setEmail("joao@email.com");
        inquilino.setTelefone("(11) 99999-9999");
        inquilino.setRendaMensal(5000.0);
        inquilino.setCategoria(TiposCliente.PESSOA_FISICA);
        return inquilino;
    }

    private Fiador criarFiadorExemplo() {
        Fiador fiador = new Fiador();
        fiador.setNome("Maria Santos");
        fiador.setCpf("987.654.321-00");
        fiador.setEmail("maria@email.com");
        fiador.setTelefone("(11) 88888-8888");
        fiador.setRendaMensal(8000.0);
        fiador.setCategoria(TiposCliente.PESSOA_FISICA);
        fiador.setEndereco("Av. Garantia, 456");
        fiador.setNumero("456");
        fiador.setBairro("Jardins");
        fiador.setCEP("04567-890");
        fiador.setObservacoes("Fiador sólido");
        return fiador;
    }
}