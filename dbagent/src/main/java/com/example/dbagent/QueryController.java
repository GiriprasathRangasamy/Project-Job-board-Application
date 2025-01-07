package com.example.dbagent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class QueryController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/execute-sql")
    public ResponseEntity<?> executeSQL(@RequestBody Map<String, String> payload) {
        String query = payload.get("query");

        try {
            // Execute the SQL query and retrieve results
            List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
            return ResponseEntity.ok(result); // Return the results
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Error executing query: " + e.getMessage());
        }
    }
}
