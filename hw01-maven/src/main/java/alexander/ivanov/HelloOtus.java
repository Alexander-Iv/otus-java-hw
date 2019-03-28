package alexander.ivanov;

import com.google.common.base.Joiner;

public class HelloOtus {
    public static void main(String[] args) {
        String res = Joiner.on(" ").skipNulls().join("Hello", null, "Otus", null, "!!!", null);
        System.out.println(res);
    }
}
