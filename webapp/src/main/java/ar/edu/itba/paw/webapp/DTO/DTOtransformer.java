package ar.edu.itba.paw.webapp.DTO;

/**
 * Created by juanfra on 22/08/17.
 */
public interface DTOtransformer<K,V> {
    V transform(K value);
}
