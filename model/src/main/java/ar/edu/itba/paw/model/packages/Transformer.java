package ar.edu.itba.paw.model.packages;

/**
 * Created by juanfra on 10/04/17.
 */
public interface Transformer<I, O> {
    O transform(I i);
}
