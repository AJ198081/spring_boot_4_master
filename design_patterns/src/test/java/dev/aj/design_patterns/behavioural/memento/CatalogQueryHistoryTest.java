package dev.aj.design_patterns.behavioural.memento;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.hibernate.query.SortDirection;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@Slf4j
class CatalogQueryHistoryTest {

    @Nested
    class testMementoDesignPattern {

        @Test
        void test() {

            QueryHistory queryHistory = new QueryHistory();

            QueryState currentQueryState = QueryState.builder().build();

            queryHistory.snapshotQueryState(currentQueryState);

            currentQueryState.setQuery("java");
            queryHistory.snapshotQueryState(currentQueryState);

            currentQueryState.addFilter("level", "beginner");
            queryHistory.snapshotQueryState(currentQueryState);

            currentQueryState.addFilter("language", "java");
            queryHistory.snapshotQueryState(currentQueryState);

            currentQueryState.setSort("language", SortDirection.interpret("asc"));
            queryHistory.snapshotQueryState(currentQueryState);

            currentQueryState.setQuery("python");
            Assertions.assertThat(currentQueryState)
                    .extracting(QueryState::getQuery)
                    .isEqualTo("python");

            queryHistory.undo(currentQueryState);
            Assertions.assertThat(currentQueryState)
                    .isNotNull()
                    .extracting(QueryState::getQuery)
                    .isEqualTo("java");

            queryHistory.redo(currentQueryState);
            Assertions.assertThat(currentQueryState)
                    .isNotNull()
                    .extracting(QueryState::getQuery)
                    .isEqualTo("python");

            queryHistory.undo(currentQueryState);
            queryHistory.undo(currentQueryState);

            Assertions.assertThat(currentQueryState)
                    .isNotNull()
                    .extracting(QueryState::getSortField)
                    .isNull();

            currentQueryState.removeFilter("language", "java");
            queryHistory.snapshotQueryState(currentQueryState);

            Assertions.assertThat(currentQueryState)
                    .isNotNull()
                    .extracting(QueryState::getFilters)
                    .satisfies(filters -> Assertions.assertThat(filters)
                            .isNotEmpty()
                    .hasSize(1));
        }

    }
}