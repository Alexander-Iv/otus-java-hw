package alexander.ivanov.jsonconverter.json;

import java.util.Map;

/*
* Запись — это неупорядоченное множество пар ключ:значение, заключённое в фигурные скобки «{ }».
* Ключ описывается строкой, между ним и значением стоит символ «:».
* Пары ключ-значение отделяются друг от друга запятыми
* */
public class RecordJson {
    Map<String, String> record;

    public RecordJson(Map<String, String> record) {
        this.record = record;
    }

    public String toJsonRecord() {
        StringBuilder recordString = new StringBuilder();
        recordString.append("{");
        record.forEach((s, s2) -> {
            recordString.append("\"").append(s).append("\"")
                    .append(":")
                    .append("\"").append(s2).append("\"");
        });
        recordString.append("}");
        return recordString.toString();
    }
}
