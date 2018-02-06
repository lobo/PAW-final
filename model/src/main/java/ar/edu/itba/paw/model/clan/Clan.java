package ar.edu.itba.paw.model.clan;

import ar.edu.itba.paw.model.User;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.math.BigDecimal.ZERO;

/**
 * Created by juanfra on 17/05/17.
 */
@Entity
@Table(name = "clans")
public class Clan implements Iterable<User> {
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "clanid")
    private final Set<User> users = new TreeSet<User>((u1, u2) ->  u1.getScore().compareTo(u2.getScore())<0?1:-1 );

    @Id
    @Column(name = "clanid")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clans_clanid_seq")
    @SequenceGenerator(sequenceName = "clans_clanid_seq",name = "clans_clanid_seq",allocationSize = 1)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "score", nullable = false)
    private BigDecimal score;

    @Column(name = "wins", nullable = false)
    private int wins = 0;

    @Column(name = "battles",nullable = false)
    private int battles = 0;

    @Column(name = "image",length = 100, nullable = false)
    private String image = null;

    public String getName() {
        return name;
    }

    Clan(){}


    @PostLoad
    private void postLoad() {
        this.score = users.stream().map(User::getScore).reduce(BigDecimal::add).orElse(ZERO);
        if (image.equals("null")) {
            Random r = new Random();
            image = (Math.abs(r.nextInt()%7) + 1) + ".jpg";
        }
    }

    Clan(@NotNull Collection<User> users, int id,@NotNull String name) {
        this.name = name;
        this.id = id;
        this.users.addAll(users);
        this.score = users.stream().map(User::getScore).reduce(BigDecimal::add).orElse(ZERO);
        this.image = (Math.abs(new Random().nextInt()%7) + 1) + ".jpg";;
    }

    public User getUser(long userId) {
        return users.stream().filter((u) -> u.getId() == userId).findAny().orElse(null);
    }

    public boolean isInClan(long userId){
        return users.stream().filter((u)->u.getId() == userId).map((u)->true).findAny().orElse(false);
    }

    public boolean isInClan(User user) {
        return isInClan(user.getId());
    }

    public BigDecimal getClanScore() {
        return users.stream().map(User::getScore).reduce(BigDecimal::add).orElse(ZERO);
    }

    @Override
    public Iterator<User> iterator() {
        return new Iterator<User>() {
            private Iterator<User> it = users.iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public User next() {
                return it.next();
            }
        };
    }

    @Override
    public void forEach(Consumer<? super User> action) {
        users.forEach(action);
    }

    @Override
    public Spliterator<User> spliterator() {
        return users.spliterator();
    }

    public Stream<User> stream(){
        return users.stream();
    }

    public Set<User> getUsers() {
        return users;
    }

    public boolean containsUser(long userId) {
        return stream().filter((u)->u.getId()==userId).map((u)->true).findAny().orElse(false);
    }

    public int getId() {
        return id;
    }

    public BigDecimal getScore() {
        return score;
    }

    public int getWins() {
        return wins;
    }

    public int getBattles() {
        return battles;
    }

    public void addBattle() {
        battles++;
    }

    public void addWin(){
        wins++;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
