package ar.edu.itba.paw.model.clan;

import ar.edu.itba.paw.model.User;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

/**
 * Created by juanfra on 17/05/17.
 */
public class ClanBuilder implements Iterable<User> {
    private Set<User> users = new TreeSet<User>((u1, u2) ->  u1.getScore().compareTo(u2.getScore())>0?1:-1 );
    private int id;
    private String name;

    public static ClanBuilder construct(int id,@NotNull String name) {
        return new ClanBuilder(id,name);
    }

    private ClanBuilder(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Iterator<User> iterator() {
        return new NoDeleteUserIterator(users.iterator());
    }

    @Override
    public void forEach(Consumer<? super User> action) {
        users.forEach(action);
    }

    public boolean add(User user) {
        if( user.getClanId() != id ) {
            return false;
        }

        return users.add(user);
    }

    public Clan buildClan() {
        return new Clan(users,id,name);
    }
}
