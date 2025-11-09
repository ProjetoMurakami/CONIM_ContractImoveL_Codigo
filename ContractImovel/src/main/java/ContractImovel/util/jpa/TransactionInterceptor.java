package ContractImovel.util.jpa;

import java.io.Serializable;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

@Interceptor
@Transactional
public class TransactionInterceptor implements Serializable {

    @Inject
    private EntityManager manager;

    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception {
        EntityTransaction trx = manager.getTransaction();
        boolean criador = false;

        try {
            if (!trx.isActive()) {
                trx.begin();
                criador = true;
            }

            Object resultado = context.proceed();

            if (criador) {
                trx.commit();
            }

            return resultado;
        } catch (Exception e) {
            if (trx != null && trx.isActive()) {
                trx.rollback();
            }
            throw e;
        }
    }
}
