package ar.edu.itba.paw.model.clan;

import ar.edu.itba.paw.model.User;

import java.util.Iterator;

/**
 * Created by juanfra on 17/05/17.
 */
public class NoDeleteUserIterator implements Iterator<User>{
    private Iterator<User> it;
    NoDeleteUserIterator(Iterator<User> it){
        this.it = it;
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public User next() {
        return it.next();
    }
}
