package finance_dashboard.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "records")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private String type; // INCOME or EXPENSE
    private String category;
    private LocalDate date;
    private String description;
    private String notes;
    private String status;

    // ✅ ID
    public Long getId() { return id; }

    // ✅ Amount
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    // ✅ Type
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    // ✅ Category
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    // ✅ Date
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    // ✅ Description
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // ✅ NOTES (THIS WAS MISSING)
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}