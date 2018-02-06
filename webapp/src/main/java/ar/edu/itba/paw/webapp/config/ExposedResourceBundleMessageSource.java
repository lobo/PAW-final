package ar.edu.itba.paw.webapp.config;

import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

public class ExposedResourceBundleMessageSource extends
        ResourceBundleMessageSource {
    private String basename;
    public Set<String> getKeys(Locale locale) {
        ResourceBundle bundle = getResourceBundle(basename, locale);
        return bundle.keySet();
    }

    @Override
    public void setBasename(String basename) {
        this.basename = basename;
        super.setBasename(basename);
    }
}