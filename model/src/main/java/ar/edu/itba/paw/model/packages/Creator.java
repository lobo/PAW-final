package ar.edu.itba.paw.model.packages;

/**
 * Created by juanfra on 08/04/17.
 */
public interface Creator<T extends ResourcePackage> {
    T create(PackageBuilder<T> packageBuilder);
}
