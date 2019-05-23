package eu.keystruck.customconsole;

import java.util.Optional;

public interface Callback {
    public abstract <T>  Optional<T> execute(String... arguments);
}
