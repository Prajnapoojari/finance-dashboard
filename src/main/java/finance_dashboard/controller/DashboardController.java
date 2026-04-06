package finance_dashboard.controller;

import finance_dashboard.model.Record;
import finance_dashboard.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private RecordService service;

    // GET /api/dashboard/summary
    // Shows total income, expense, balance (Only ADMIN and ANALYST)
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Double>> getSummary(
            @RequestAttribute String userRole) {  // CHANGED: from @RequestParam to @RequestAttribute

        if (userRole.equals("VIEWER")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access Denied: VIEWER cannot access dashboard summary");
        }
        return ResponseEntity.ok(service.getSummary());
    }

    // GET /api/dashboard/recent
    // Shows last 5 transactions (Only ADMIN and ANALYST)
    @GetMapping("/recent")
    public ResponseEntity<List<Record>> getRecentRecords(
            @RequestAttribute String userRole) {  // CHANGED: from @RequestParam to @RequestAttribute

        if (userRole.equals("VIEWER")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access Denied: VIEWER cannot access recent records");
        }

        List<Record> all = service.getAllRecords();

        // get last 5 records only
        int size = all.size();
        List<Record> recent = all.subList(
                Math.max(0, size - 5), size);

        return ResponseEntity.ok(recent);
    }

    // GET /api/dashboard/category
    // Shows total per category (Only ADMIN and ANALYST)
    @GetMapping("/category")
    public ResponseEntity<Map<String, Double>> getCategoryTotals(
            @RequestAttribute String userRole) {  // CHANGED: from @RequestParam to @RequestAttribute

        if (userRole.equals("VIEWER")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access Denied: VIEWER cannot access category data");
        }
        return ResponseEntity.ok(service.getCategoryTotals());
    }
}