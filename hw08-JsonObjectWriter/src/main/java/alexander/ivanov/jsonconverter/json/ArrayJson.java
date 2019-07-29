package alexander.ivanov.jsonconverter.json;

import java.util.List;
import java.util.ListIterator;

/*
* Массив (одномерный) — это упорядоченное множество значений.
* Массив заключается в квадратные скобки «[ ]».
* Значения разделяются запятыми.
* Массив может быть пустым, т.е. не содержать ни одного значения.
* */
public class ArrayJson {
    private List<String> array;

    public ArrayJson(List<String> array) {
        this.array = array;
    }

    public String toJsonArray() {
        StringBuilder arrayString = new StringBuilder();
        arrayString.append("[");
        ListIterator iter = array.listIterator();
        while(iter.hasNext()) {
            arrayString.append("\"").append(iter.next()).append("\"");
            if (iter.nextIndex() != array.size()) {
                arrayString.append(",");
            }
        }
        arrayString.append("]");
        return arrayString.toString();
    }
}
