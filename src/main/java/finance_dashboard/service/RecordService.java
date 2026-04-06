package finance_dashboard.service;

import finance_dashboard.model.Record;
import finance_dashboard.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class RecordService {

    @Autowired
    private RecordRepository repository;

    // ✅ CREATE - auto set ACTIVE
    public Record createRecord(Record record) {
        record.setStatus("ACTIVE");
        return repository.save(record);
    }

    // ✅ GET ALL - only show ACTIVE records
    public List<Record> getAllRecords() {
        return repository.findByStatus("ACTIVE");
    }

    // ✅ GET BY ID
    public Record getRecordById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Record not found with id: " + id));
    }

    // ✅ UPDATE
    public Record updateRecord(Long id, Record newRecord) {
        Record old = getRecordById(id);
        old.setAmount(newRecord.getAmount());
        old.setType(newRecord.getType());
        old.setCategory(newRecord.getCategory());
        old.setDate(newRecord.getDate());
        old.setNotes(newRecord.getNotes());
        old.setDescription(newRecord.getDescription());
        return repository.save(old);
    }

    // ✅ SOFT DELETE - mark as DELETED, don't remove from DB
    public void deleteRecord(Long id) {
        Record record = getRecordById(id);
        record.setStatus("DELETED");
        repository.save(record);
    }

    // ✅ SUMMARY
    public Map<String, Double> getSummary() {
        List<Record> records = getAllRecords(); // only ACTIVE records

        double income = 0;
        double expense = 0;

        for (Record r : records) {
            if ("INCOME".equalsIgnoreCase(r.getType())) {
                income += r.getAmount();
            } else if ("EXPENSE".equalsIgnoreCase(r.getType())) {
                expense += r.getAmount();
            }
        }

        Map<String, Double> summary = new HashMap<>();
        summary.put("totalIncome", income);
        summary.put("totalExpense", expense);
        summary.put("netBalance", income - expense);

        return summary;
    }
    // ✅ Category wise totals
    public Map<String, Double> getCategoryTotals() {
        List<Record> records = getAllRecords();
        Map<String, Double> categoryMap = new HashMap<>();

        for (Record r : records) {
            String category = r.getCategory();
            double amount = r.getAmount();

            // if category already exists, add to it
            // if not, start from 0
            categoryMap.put(category,
                    categoryMap.getOrDefault(category, 0.0) + amount);
        }

        return categoryMap;
    }
    // filter by type (INCOME or EXPENSE)
    public List<Record> getRecordsByType(String type) {
        return repository.findByTypeAndStatus(type, "ACTIVE");
    }

    // filter by category (Salary, Food etc)
    public List<Record> getRecordsByCategory(String category) {
        return repository.findByCategoryAndStatus(category, "ACTIVE");
    }
}