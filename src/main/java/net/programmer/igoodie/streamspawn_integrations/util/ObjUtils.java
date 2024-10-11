package net.programmer.igoodie.streamspawn_integrations.util;

import java.util.function.Supplier;

public final class ObjUtils {

    private ObjUtils() {}

    @SafeVarargs
    public static <T> T firstNonNull(Supplier<T>... suppliers) {
        for (Supplier<T> supplier : suppliers) {
            T value = supplier.get();
            if (value != null) return value;
        }
        return null;
    }

}
