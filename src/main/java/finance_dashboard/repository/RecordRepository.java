package finance_dashboard.repository;

import finance_dashboard.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {

    // get only ACTIVE or DELETED records
    List<Record> findByStatus(String status);

    // filter by type (INCOME/EXPENSE) and status
    List<Record> findByTypeAndStatus(String type, String status);

    // filter by category and status
    List<Record> findByCategoryAndStatus(String category, String status);
}