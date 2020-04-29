import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Person implements Serializable {
    private String id;
    private String name;
    private int birthYear;
    private int deathYear;
    private List<String> profession;
    private List<String> films;

    public Person(String id, String name, int birthYear, int deathYear, List<String> profession, List<String> films){
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
        this.profession = profession;
        this.films = films;
    }

    @Override
    public String toString(){
        return "ID: " + id + "\nName: " + name + "\nbirthYear: " + birthYear + "\ndeathYear: " + deathYear + "\nprofessions: " + profession.toString() + "\nfilms: " + films.toString();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public int getDeathYear() {
        return deathYear;
    }

    public List<String> getFilms() {
        return films;
    }

    public List<String> getProfession() {
        return profession;
    }

    private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
    {
        this.id = aInputStream.readUTF();
        this.name = aInputStream.readUTF();
        this.birthYear = aInputStream.readInt();
        this.deathYear = aInputStream.readInt();
        this.profession = (List<String>)aInputStream.readObject();
        this.films = (List<String>)aInputStream.readObject();
    }

    private void writeObject(ObjectOutputStream aOutputStream) throws IOException
    {
        aOutputStream.writeUTF(this.id);
        aOutputStream.writeUTF(this.name);
        aOutputStream.writeInt(this.birthYear);
        aOutputStream.writeInt(this.deathYear);
        aOutputStream.writeObject(this.profession);
        aOutputStream.writeObject(this.films);
    }
}
