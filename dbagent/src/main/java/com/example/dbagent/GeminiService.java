package com.example.dbagent;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Store API Key in environment variable or application properties for better security
    private final String API_KEY = "AIzaSyClRxfTd_l2qmYndkmPN7jDpwmKm9R4KEE";  
    private final String GEMINI_URL =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=" + API_KEY;

    /**
     * Generate SQL query from natural language using Google Gemini API.
     */
    public String generateSQLQuery(String question) {
        RestTemplate restTemplate = new RestTemplate();

        // Construct the request payload
        JSONObject requestBody = new JSONObject();
        JSONObject prompt = new JSONObject();
        prompt.put("text", 
            "Convert the following natural language to a MySQL query. " +
            "Here are my tables and columns:\n\n" +
            "Table: job\n" +
            "Columns: id (int, AI, PK), about_company (tinytext), about_job (tinytext), " +
            "address (varchar(255)), apply_by (date), apply_link (varchar(255)), city (varchar(255)), " +
            "company_name (varchar(255)), email (varchar(255)), experience (varchar(255)), " +
            "job_type (varchar(255)), role (varchar(255)), salary (double), start_date (date), " +
            "vacancies (varchar(255)).\n\n" +
            "Table: job_eligibility\n" +
            "Columns: job_id (int), eligibility (varchar(255)).\n\n" +
            "Table: job_skills\n" +
            "Columns: job_id (int), skills (varchar(255)).\n\n" +
            "Natural language input: " + question
        );
        requestBody.put("prompt", prompt);

        // Set HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_URL, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                return jsonResponse.getJSONArray("candidates").getJSONObject(0).getString("output");
            } else {
                throw new RuntimeException("Failed to generate SQL query. HTTP Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error generating SQL query.", e);
        }
    }

    /**
     * Execute the generated SQL query and return the results.
     */
    public List<Map<String, Object>> executeSQLQuery(String sqlQuery) {
        try {
            // Use parameterized query if any user input is involved to prevent SQL injection
            return jdbcTemplate.queryForList(sqlQuery);
        } catch (Exception e) {
            throw new RuntimeException("Error executing SQL query.", e);
        }
    }
}
