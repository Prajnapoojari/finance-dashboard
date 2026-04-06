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
@RequestMapping("/api/records")
public class RecordController {

    @Autowired
    private RecordService service;

    // GET all records - All roles can access (role comes from token)
    @GetMapping
    public ResponseEntity<List<Record>> getAll(
            @RequestAttribute String userRole,  // CHANGED: from @RequestParam to @RequestAttribute
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category) {

        // validate role (using userRole from token instead of URL parameter)
        if (!userRole.equals("ADMIN") &&
                !userRole.equals("ANALYST") &&
                !userRole.equals("VIEWER")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Invalid role: " + userRole);
        }

        // filter by type if given
        if (type != null) {
            return ResponseEntity.ok(service.getRecordsByType(type));
        }

        // filter by category if given
        if (category != null) {
            return ResponseEntity.ok(service.getRecordsByCategory(category));
        }

        // no filter - return all active records
        return ResponseEntity.ok(service.getAllRecords());
    }

    // GET summary - Only ADMIN and ANALYST
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Double>> getSummary(
            @RequestAttribute String userRole) {  // CHANGED: from @RequestParam to @RequestAttribute

        if (userRole.equals("VIEWER")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access Denied: VIEWER cannot see summary");
        }
        return ResponseEntity.ok(service.getSummary());
    }

    // POST create record - Only ADMIN
    @PostMapping
    public ResponseEntity<Record> create(
            @RequestBody Record record,
            @RequestAttribute String userRole) {  // CHANGED: from @RequestParam to @RequestAttribute

        if (!userRole.equals("ADMIN")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access Denied: Only ADMIN can create records");
        }
        return ResponseEntity.ok(service.createRecord(record));
    }

    // PUT update record - Only ADMIN
    @PutMapping("/{id}")
    public ResponseEntity<Record> update(
            @PathVariable Long id,
            @RequestBody Record record,
            @RequestAttribute String userRole) {  // CHANGED: from @RequestParam to @RequestAttribute

        if (!userRole.equals("ADMIN")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access Denied: Only ADMIN can update records");
        }
        return ResponseEntity.ok(service.updateRecord(id, record));
    }

    // DELETE record - Only ADMIN
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id,
            @RequestAttribute String userRole) {  // CHANGED: from @RequestParam to @RequestAttribute

        if (!userRole.equals("ADMIN")) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access Denied: Only ADMIN can delete records");
        }
        service.deleteRecord(id);
        return ResponseEntity.ok("Record deleted successfully");
    }
}