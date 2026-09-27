package com.resumeanalyzer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisResult {

    // Overall ATS score
    private int score;

    // ATS status
    private String status;

    // Skill matching
    private List<String> matchedSkills;
    private List<String> missingSkills;

    // Explainable score breakdown
    private int skillScore;
    private int experienceScore;
    private int projectScore;
    private int educationScore;
    private int keywordScore;
    private int certificationScore;
    private List<String> recommendations;
    private Map<String, List<String>> categorizedSkills;
}