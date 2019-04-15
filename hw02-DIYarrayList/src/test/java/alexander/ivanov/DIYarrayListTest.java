package alexander.ivanov;

import org.junit.jupiter.api.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DIYarrayListTest {
    private int i = 0;

    private static final int[] DEFAULT_INT_LIST =
            new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24};
    private static final Integer[] DEFAULT_INTEGER_LIST =
            {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25};
    private static final Integer[] DEFAULT_INTEGER_LIST_INVERSE =
            {25,24,23,22,21,20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,0};
    private static final Integer[] DEFAULT_INTEGER_LIST_EQUAL = {333,333,111,111,222,222};

    private static List<Integer> emptyDIYarrayList = new DIYarrayList<>();
    private static List<Integer> emptyArrayList = new ArrayList<>();

    private static List<Integer> integerDIYarrayList = new DIYarrayList<>();
    private static List<Integer> integerArrayList = new ArrayList<>();

    private static List<Integer> equalDIYarrayList = new DIYarrayList<>();
    private static List<Integer> equalArrayList = new ArrayList<>();

    private static List<Integer> inverseDIYarrayList = new DIYarrayList<>();
    private static List<Integer> inverseArrayList = new ArrayList<>();

    private static void addDefault(Class<?> type, Object[] source, List target) {
        for (int i = 0; i < source.length; i++) {
            target.add(source[i]);
            /*System.out.println("[" + type.getSimpleName() + "]" + " = " + target);*/
        }
    }

    @BeforeAll
    public static void init() {
        addDefault(Integer.class, DEFAULT_INTEGER_LIST, integerDIYarrayList);
        addDefault(Integer.class, DEFAULT_INTEGER_LIST, integerArrayList);

        addDefault(Integer.class, DEFAULT_INTEGER_LIST_EQUAL, equalDIYarrayList);
        addDefault(Integer.class, DEFAULT_INTEGER_LIST_EQUAL, equalArrayList);

        addDefault(Integer.class, DEFAULT_INTEGER_LIST_INVERSE, inverseDIYarrayList);
        addDefault(Integer.class, DEFAULT_INTEGER_LIST_INVERSE, inverseArrayList);
    }

    @Test @Order(1)
    public void isEmptyDIY() {
        assertTrue(emptyDIYarrayList.isEmpty());
    }
    @Test @Order(2)
    public void isNotEmptyDIY() {
        assertFalse(integerDIYarrayList.isEmpty());
    }
    @Test @Order(3)
    public void isEmpty() {
        assertTrue(emptyArrayList.isEmpty());
    }
    @Test @Order(4)
    public void isNotEmpty() {
        assertFalse(integerArrayList.isEmpty());
    }

    @Test @Order(5)
    public void toArray1() {
        assertArrayEquals(DEFAULT_INTEGER_LIST, integerArrayList.toArray());
    }

    @Test @Order(6)
    public void toArray2() {
        assertArrayEquals(DEFAULT_INTEGER_LIST, integerArrayList.toArray(DEFAULT_INTEGER_LIST_INVERSE));
    }

    @Test @Order(100)
    public void addAll() {
        System.out.println("~~~addAll~~~");
        System.out.println("emptyArrayList = " + emptyArrayList);

        Collections.addAll(emptyArrayList, DEFAULT_INTEGER_LIST);

        System.out.println("emptyArrayList = " + emptyArrayList);
        System.out.println("integerArrayList = " + integerArrayList);
        System.out.println("~~~");
        System.out.println();

        assertArrayEquals(emptyArrayList.toArray(), integerArrayList.toArray());
    }

    @Test @Order(101)
    public void addAllDIY() {
        System.out.println("~~~addAll with DIY");
        System.out.println("emptyDIYarrayList = " + emptyDIYarrayList);

        Collections.addAll(emptyDIYarrayList, DEFAULT_INTEGER_LIST);

        System.out.println("emptyDIYarrayList = " + emptyDIYarrayList);
        System.out.println("integerDIYarrayList = " + integerDIYarrayList);
        System.out.println("~~~");
        System.out.println();

        assertArrayEquals(emptyDIYarrayList.toArray(), integerDIYarrayList.toArray());
    }

    @Test @Order(200)
    public void copy() {
        System.out.println("~~~copy~~~");

        List<Integer> dest = new ArrayList<>();
        for (int i = 0; i < integerArrayList.size(); i++) {
            dest.add(0);
        }
        System.out.println("dest = " + dest + " dest.size() = " + dest.size());

        Collections.copy(dest, integerArrayList);

        System.out.println("dest = " + dest);
        System.out.println("~~~");
        System.out.println();

        assertArrayEquals(dest.toArray(), integerArrayList.toArray());
    }

    @Test @Order(201)
    public void copyDIY() {
        System.out.println("~~~copy with DIY~~~");

        List<Integer> dest = new DIYarrayList<>();
        for (int i = 0; i < integerDIYarrayList.size(); i++) {
            dest.add(0);
        }
        System.out.println("dest = " + dest + " dest.size() = " + dest.size());

        Collections.copy(dest, integerDIYarrayList);
        /*integerDIYarrayList.forEach(integer -> System.out.println("integer = " + integer));*/

        System.out.println("dest = " + dest);
        System.out.println("~~~");
        System.out.println();

        assertArrayEquals(dest.toArray(), integerDIYarrayList.toArray());
    }

    @Test @Order(300)
    public void sort() {
        System.out.println("~~~sort~~~");
        System.out.println("inverseArrayList = " + inverseArrayList);

        Collections.sort(inverseArrayList, Integer::compareTo);

        System.out.println("inverseArrayList = " + inverseArrayList);
        System.out.println("~~~");
        System.out.println();

        assertArrayEquals(integerDIYarrayList.toArray(), inverseArrayList.toArray());
    }

    @Test @Order(301)
    public void sortDIY() {
        System.out.println("~~~sort with DIY~~~");
        System.out.println("inverseDIYarrayList = " + inverseDIYarrayList);

        Collections.sort(inverseDIYarrayList, Integer::compareTo);
        /*Collections.sort(inverseDIYarrayList, null);*/

        System.out.println("inverseArrayList = " + inverseDIYarrayList);
        System.out.println("~~~");
        System.out.println();

        assertArrayEquals(integerDIYarrayList.toArray(), inverseDIYarrayList.toArray());
    }
}