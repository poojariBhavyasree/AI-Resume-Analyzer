package com.resumeanalyzer.controller;

import com.resumeanalyzer.model.AnalysisResult;
import com.resumeanalyzer.parser.ResumeParser;
import com.resumeanalyzer.service.ResumeAnalyzerService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeParser resumeParser;
    private final ResumeAnalyzerService resumeAnalyzerService;

    public ResumeController(
            ResumeParser resumeParser,
            ResumeAnalyzerService resumeAnalyzerService) {

        this.resumeParser = resumeParser;
        this.resumeAnalyzerService = resumeAnalyzerService;
    }

    @PostMapping(
            value = "/analyze",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<AnalysisResult> analyzeResume(

            @RequestParam("resume") MultipartFile resume,

            @RequestParam("jobDescription") String jobDescription) {
        if (resume == null || resume.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        if (!resume.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            return ResponseEntity.badRequest().build();
        }
        try {

            String resumeText = resumeParser.extractText(resume);

            AnalysisResult result =
                    resumeAnalyzerService.analyze(
                            resumeText,
                            jobDescription
                    );

            return ResponseEntity.ok(result);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .build();
        }
    }
}