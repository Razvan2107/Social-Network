package service;

import domain.Friendship;
import domain.Utilizator;
import repository.Repository;
import utils.*;
import utils.Observable;
import utils.Observer;
import utils.events.ChangeEventType;
import utils.events.UserChangeEvent;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.lang.Integer;
import java.util.concurrent.atomic.AtomicInteger;


public class Service implements Observable<UserChangeEvent> {
    private Repository<Long,Utilizator> repository;
    public Vector<Friendship> friends=new Vector<>();

    public Service(Repository<Long,Utilizator> repository) {
        this.repository = repository;
    }

    //@Override
    public Optional<Utilizator> add_user(String firstName,String lastName){
        Utilizator u1=new Utilizator(firstName, lastName);
        //u1.setId(id);
        var user =  repository.save(u1);
        if(user.isEmpty()){
            notifyObservers(new UserChangeEvent(ChangeEventType.ADD,u1));
        }
        return user;
    }

    //@Override
    public Optional<Utilizator> delete_user(Long id){
        Utilizator u1=new Utilizator("default","default");
        u1.setId(id);
        var user= repository.delete(id);
        if(user.isEmpty())
            notifyObservers(new UserChangeEvent(ChangeEventType.DELETE,u1));
        return user;
    }

    public Optional<Utilizator> update_user(Long id,String firstName, String lastName){
        Utilizator u=new Utilizator(firstName, lastName);
        u.setId(id);
        var user=repository.update(u);
        if(user.isEmpty())
            notifyObservers(new UserChangeEvent(ChangeEventType.UPDATE,u));
        return user;
    }

    public Iterable<Utilizator> getAll(){return repository.findAll();}

   // @Override
//    public Friendship add_friend(String id1, String id2){
//        Optional<Utilizator> u1 = repository.findOne(Long.parseLong(id1));
//        Optional<Utilizator> u2 = repository.findOne(Long.parseLong(id2));
//
//        if(u1==u2 || u1.isEmpty() || u2.isEmpty()){
//            return null;
//        }
//
//        Friendship f = new Friendship(u1.get(), u2.get());
//        ArrayList<Friendship> f1 = u1.get().getFriends();
//        ArrayList<Friendship> f2 = u2.get().getFriends();
//        if(f1.contains(f) || f2.contains(f)){
//            return null;
//        }else{
//            f1.add(f);
//            f2.add(f);
//            friends.add(f);
//        }
//
//        return f;
//    }
    public Optional<Friendship> add_friend(String id1, String id2){
        try{
            Connection connection= DriverManager.getConnection("jdbc:postgresql://localhost:5432/socialnetwork","postgres","sabinarazvan");
            PreparedStatement statement=connection.prepareStatement("INSERT INTO Friends(id1,id2,date_time) VALUES (?,?,?)");
            statement.setLong(1,Long.parseLong(id1));
            statement.setLong(2,Long.parseLong(id2));
            Optional<Utilizator> u1=repository.findOne(Long.parseLong(id1));
            Optional<Utilizator> u2=repository.findOne(Long.parseLong(id2));
            if(u1==u2 || u1.isEmpty() || u2.isEmpty())
                return Optional.empty();
            Friendship f=new Friendship(u1.get(),u2.get());
            ArrayList<Friendship> f1 = u1.get().getFriends();
            ArrayList<Friendship> f2 = u2.get().getFriends();
            if(f1.contains(f) || f2.contains(f)){
                return Optional.empty();
            }else{
                f1.add(f);
                f2.add(f);
                friends.add(f);
            }
            LocalDateTime data=f.getFriendsFrom();
            DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedDateTime = data.format(dateTimeFormatter);
            statement.setString(3,formattedDateTime);
            statement.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

//    @Override
//    public boolean delete_friend(String id1,String id2){
//        Optional<Utilizator> u1 = repository.findOne(Long.parseLong(id1));
//        Optional<Utilizator> u2 = repository.findOne(Long.parseLong(id2));
//
//        if(u1==u2 || u1.isEmpty() || u2.isEmpty()){
//            return false;
//        }
//
//        Friendship f = new Friendship(u1.get(), u2.get());
//        ArrayList<Friendship> f1 = u1.get().getFriends();
//        ArrayList<Friendship> f2 = u2.get().getFriends();
//        return f1.remove(f) && f2.remove(f) && friends.remove(f);
//    }

    //@Override
    public boolean delete_friend(String id1,String id2){
        try{
            Connection connection= DriverManager.getConnection("jdbc:postgresql://localhost:5432/socialnetwork","postgres","sabinarazvan");
            PreparedStatement statement=connection.prepareStatement("DELETE FROM Friends WHERE id1=? and id2=?");
            statement.setLong(1,Long.parseLong(id1));
            statement.setLong(2,Long.parseLong(id2));
            Optional<Utilizator> u1 = repository.findOne(Long.parseLong(id1));
            Optional<Utilizator> u2 = repository.findOne(Long.parseLong(id2));

            if(u1==u2 || u1.isEmpty() || u2.isEmpty()){
                return false;
            }

            Friendship f = new Friendship(u1.get(), u2.get());
            ArrayList<Friendship> f1 = u1.get().getFriends();
            ArrayList<Friendship> f2 = u2.get().getFriends();
            boolean ok=f1.remove(f) && f2.remove(f) && friends.remove(f);
            statement.executeUpdate();
            //return ok;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Set<Friendship> getFriends() {
        Set<Friendship> friends = new HashSet<>();

        try (Connection connection= DriverManager.getConnection("jdbc:postgresql://localhost:5432/socialnetwork","postgres","sabinarazvan");
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friends");
             ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next())
            {
                Long id1= resultSet.getLong("id1");
                Long id2=resultSet.getLong("id2");
                String str=resultSet.getString("date_time");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
                Friendship f=new Friendship(repository.findOne(id1).get(),repository.findOne(id2).get());
                f.setFriendsFrom(dateTime);
                friends.add(f);
            }
            return friends;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    private int DFSDist(Map<Long, Boolean> viz, Utilizator u){
        viz.remove(u.getId());
        viz.put(u.getId(), true);

        for (Friendship f: u.getFriends()) {
            if(f.getU1() == u && !viz.get(f.getU2().getId())){
                return 1+DFSDist(viz, f.getU2());
            }else if(f.getU2() == u && !viz.get(f.getU1().getId())){
                return 1+DFSDist(viz, f.getU1());
            }
        }

        return 1;
    }

    private void DFSList(Map<Long, Boolean> viz, Utilizator u, ArrayList<Utilizator> list){
        viz.remove(u.getId());
        viz.put(u.getId(), true);
        list.add(u);
        for (Friendship f: u.getFriends()) {
            if(f.getU1() == u && !viz.get(f.getU2().getId())){
                DFSList(viz, f.getU2(), list);
            }else if(f.getU2() == u && !viz.get(f.getU1().getId())){
                DFSList(viz, f.getU1(), list);
            }
        }
    }

    public AtomicInteger no_Communities() {
        Map<Long, Boolean> viz = new HashMap<>();
        repository.findAll().forEach(x -> {
            viz.put(x.getId(), false);
        });
        AtomicInteger componentCount = new AtomicInteger();
        repository.findAll().forEach(
                (x) -> {
                    if (!viz.get(x.getId())) {
                        Optional<Utilizator> crt = repository.findOne(x.getId());
                        crt.ifPresent(user -> DFSDist(viz, user));
                        componentCount.getAndIncrement();
                    }
                }
        );
        return componentCount;
    }

    public ArrayList<Utilizator> mostSociable() {
        Map<Long, Boolean> viz = new HashMap<>();
        repository.findAll().forEach((x) -> {
            viz.put(x.getId(), false);
        });
        int maxL = 0;
        Long idNode = 0L;
        for (Utilizator x : repository.findAll()) {
            Optional<Utilizator> crt = repository.findOne(x.getId());
            if (crt.isPresent()) {
                int dist = DFSDist(viz, crt.get());
                if (maxL < dist) {
                    maxL = dist;
                    idNode = crt.get().getId();
                }
            }
        }
        Map<Long, Boolean> viz2 = new HashMap<>();
        repository.findAll().forEach((x) -> {
            viz2.put(x.getId(), false);
        });

        ArrayList<Utilizator> list = new ArrayList<>();
        DFSList(viz2, repository.findOne(idNode).get(), list);
        return list;
    }

    public Map<Utilizator,LocalDateTime> getFriends_sameMonth(Long id,Integer month){
        Map<Utilizator,LocalDateTime> list=new HashMap<>();
        for(Friendship el : getFriends()) {
            if (el.getFriendsFrom().getMonthValue() == month){
                if (el.getU1().getId() != repository.findOne(id).get().getId() && el.getU2().getId()==id)
                    list.put(el.getU1(),el.getFriendsFrom());
                else if(el.getU2().getId() != repository.findOne(id).get().getId() && el.getU1().getId()==id)
                    list.put(el.getU2(),el.getFriendsFrom());
            }
        }
        return list;
    }

    private List<Observer<UserChangeEvent>> observers=new ArrayList<>();

    @Override
    public void addObserver(Observer<UserChangeEvent> u){
        observers.add(u);
    }

    @Override
    public void removeObserver(Observer<UserChangeEvent> u){
        //observers.remove(u);
    }

    @Override
    public void notifyObservers(UserChangeEvent u){
        observers.stream().forEach(x->x.update(u));
    }
}
