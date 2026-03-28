package dev.aj.design_patterns.behavioural.memento;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.query.SortDirection;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class QueryState {

    private String query;
    private final Map<String, Set<String>> filters = new LinkedHashMap<>();
    private String sortField;
    private SortDirection sortDirection;


    @Getter(value = AccessLevel.PRIVATE)
    @Setter(value = AccessLevel.PRIVATE)
    private QuerySnapshot snapshot;

    public void setQuery(String query) {
        this.query = Objects.isNull(query)
                ? ""
                : query;
    }

    public void addFilter(String field, String value) {
        filters.computeIfAbsent(field, _ -> new java.util.HashSet<>())
                .add(value);
    }

    public void removeFilter(String field, String value) {
        filters.computeIfPresent(field,
                (_key, values) -> {
                    values.remove(value);
                    return values.isEmpty() ? null : values;
                });
    }

    public void setFilters(Map<String, Set<String>> filters) {
        this.filters.clear();
        this.filters.putAll(filters);
    }

    public void setSort(String theSortField, SortDirection theSortDirection) {
        this.sortField = theSortField;
        this.sortDirection = theSortDirection;
    }

    private static Map<String, Set<String>> deepCopyFilters(Map<String, Set<String>> filters) {
        Map<String, Set<String>> filterCopy = new LinkedHashMap<>();
        filters.forEach((key, values) -> filterCopy.put(key, new java.util.HashSet<>(values)));
        return filterCopy;
    }

    public static QuerySnapshot snapshot(QueryState state) {
        return new QuerySnapshot(state.getQuery(), deepCopyFilters(state.getFilters()), state.getSortField(), state.getSortDirection());
    }

    @Override
    public String toString() {
        return "CatalogQueryState{" +
                "filters=" + filters +
                ", query='" + query + '\'' +
                ", sortField='" + sortField + '\'' +
                ", sortDirection=" + sortDirection +
                '}';
    }

    public void restore(QuerySnapshot snapshot) {
        this.query = snapshot.query();
        this.setFilters(snapshot.filters);
        this.sortField = snapshot.sortField();
        this.sortDirection = snapshot.sortDirection();
    }

    public record QuerySnapshot(String query, Map<String, Set<String>> filters, String sortField,
                                SortDirection sortDirection) {
    }
}
