package utils;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class UnorderedPair<T> {
    private final Set<T> set = new LinkedHashSet<>();

    public UnorderedPair(T a, T b){
        set.add(a);
        if (!set.add(b)){
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object object){
        if (object == this){
            return true;
        }
        if (!(object instanceof UnorderedPair<?>)){
            return false;
        }
        return set.equals(((UnorderedPair<?>)object).set);
    }

    @Override
    public int hashCode(){
        return set.hashCode();
    }


}
