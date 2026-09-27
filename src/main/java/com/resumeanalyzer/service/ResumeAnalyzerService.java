package com.resumeanalyzer.service;

import com.resumeanalyzer.model.AnalysisResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class ResumeAnalyzerService {

    // ======================================================
    // SKILLS DATABASE
    // ======================================================

    private final List<String> skills = Arrays.asList(

            // Programming Languages
            "java",
            "python",
            "javascript",
            "typescript",
            "c",
            "c++",
            "c#",
            "go",
            "rust",
            "php",
            "ruby",
            "kotlin",
            "swift",

            // Frontend
            "html",
            "css",
            "react",
            "angular",
            "vue",
            "next.js",
            "tailwind",
            "bootstrap",

            // Backend
            "node.js",
            "express",
            "spring boot",
            "spring",
            "django",
            "flask",
            "fastapi",
            "asp.net",
            "rest api",
            "graphql",

            // Databases
            "sql",
            "mysql",
            "postgresql",
            "oracle",
            "mongodb",
            "redis",
            "sqlite",

            // Cloud & DevOps
            "aws",
            "azure",
            "google cloud",
            "docker",
            "kubernetes",
            "jenkins",
            "git",
            "github",
            "gitlab",
            "ci/cd",

            // Data Analytics
            "pandas",
            "numpy",
            "matplotlib",
            "seaborn",
            "power bi",
            "tableau",
            "excel",
            "data visualization",
            "statistics",
            "data analysis",
            "data cleaning",

            // AI & Machine Learning
            "machine learning",
            "deep learning",
            "data science",
            "artificial intelligence",
            "natural language processing",
            "nlp",
            "tensorflow",
            "pytorch",
            "scikit-learn",

            // Testing
            "junit",
            "selenium",
            "postman",
            "api testing",
            "unit testing",

            // Other
            "microservices",
            "agile",
            "scrum",
            "problem solving",
            "communication"
    );


    // ======================================================
    // SKILL CATEGORIES
    // ======================================================

    private final Map<String, List<String>> skillCategories =
            new LinkedHashMap<>();

    {
        skillCategories.put(
                "Programming Languages",
                Arrays.asList(
                        "java",
                        "python",
                        "javascript",
                        "typescript",
                        "c",
                        "c++",
                        "c#",
                        "go",
                        "rust",
                        "php",
                        "ruby",
                        "kotlin",
                        "swift"
                )
        );

        skillCategories.put(
                "Frontend",
                Arrays.asList(
                        "html",
                        "css",
                        "react",
                        "angular",
                        "vue",
                        "next.js",
                        "tailwind",
                        "bootstrap"
                )
        );

        skillCategories.put(
                "Backend",
                Arrays.asList(
                        "node.js",
                        "express",
                        "spring boot",
                        "spring",
                        "django",
                        "flask",
                        "fastapi",
                        "asp.net",
                        "rest api",
                        "graphql"
                )
        );

        skillCategories.put(
                "Databases",
                Arrays.asList(
                        "sql",
                        "mysql",
                        "postgresql",
                        "oracle",
                        "mongodb",
                        "redis",
                        "sqlite"
                )
        );

        skillCategories.put(
                "Cloud & DevOps",
                Arrays.asList(
                        "aws",
                        "azure",
                        "google cloud",
                        "docker",
                        "kubernetes",
                        "jenkins",
                        "git",
                        "github",
                        "gitlab",
                        "ci/cd"
                )
        );

        skillCategories.put(
                "Data Analytics",
                Arrays.asList(
                        "pandas",
                        "numpy",
                        "matplotlib",
                        "seaborn",
                        "power bi",
                        "tableau",
                        "excel",
                        "data visualization",
                        "statistics",
                        "data analysis",
                        "data cleaning"
                )
        );

        skillCategories.put(
                "AI & Machine Learning",
                Arrays.asList(
                        "machine learning",
                        "deep learning",
                        "data science",
                        "artificial intelligence",
                        "natural language processing",
                        "nlp",
                        "tensorflow",
                        "pytorch",
                        "scikit-learn"
                )
        );

        skillCategories.put(
                "Testing",
                Arrays.asList(
                        "junit",
                        "selenium",
                        "postman",
                        "api testing",
                        "unit testing"
                )
        );

        skillCategories.put(
                "Other",
                Arrays.asList(
                        "microservices",
                        "agile",
                        "scrum"
                )
        );
    }


    // ======================================================
    // MAIN ANALYSIS
    // ======================================================

    public AnalysisResult analyze(
            String resumeText,
            String jobDescription
    ) {

        String resume = normalizeText(resumeText);
        String job = normalizeText(jobDescription);

        List<String> matchedSkills = new ArrayList<>();
        List<String> missingSkills = new ArrayList<>();


        // ==================================================
        // 1. SKILLS - 40%
        // ==================================================

        for (String skill : skills) {

            boolean requiredByJob =
                    containsSkill(job, skill);

            boolean presentInResume =
                    containsSkill(resume, skill);

            if (requiredByJob && presentInResume) {

                matchedSkills.add(
                        formatSkillName(skill)
                );
            }

            if (requiredByJob && !presentInResume) {

                missingSkills.add(
                        formatSkillName(skill)
                );
            }
        }


        int skillScore;

        int totalRequiredSkills =
                matchedSkills.size()
                        + missingSkills.size();

        if (totalRequiredSkills == 0) {

            skillScore = 0;

        } else {

            skillScore =
                    (matchedSkills.size() * 100)
                            / totalRequiredSkills;
        }


        // ==================================================
        // 2. EXPERIENCE - 20%
        // ==================================================

        int experienceScore =
                calculateExperienceScore(
                        resume,
                        job
                );


        // ==================================================
        // 3. PROJECTS - 15%
        // ==================================================

        int projectScore =
                calculateProjectScore(
                        resume,
                        job
                );


        // ==================================================
        // 4. EDUCATION - 10%
        // ==================================================

        int educationScore =
                calculateEducationScore(resume);


        // ==================================================
        // 5. KEYWORDS - 10%
        // ==================================================

        int keywordScore =
                calculateKeywordScore(
                        resume,
                        job
                );


        // ==================================================
        // 6. CERTIFICATIONS - 5%
        // ==================================================

        int certificationScore =
                calculateCertificationScore(resume);


        // ==================================================
        // RECOMMENDATIONS
        // ==================================================

        List<String> recommendations =
                generateRecommendations(
                        skillScore,
                        experienceScore,
                        projectScore,
                        educationScore,
                        keywordScore,
                        certificationScore,
                        missingSkills
                );


        // ==================================================
        // CATEGORIZED SKILLS
        // ==================================================

        Map<String, List<String>> categorizedSkills =
                categorizeMatchedSkills(matchedSkills);


        // ==================================================
        // FINAL SCORE
        // ==================================================

        double finalScore =
                (skillScore * 0.40)
                        + (experienceScore * 0.20)
                        + (projectScore * 0.15)
                        + (educationScore * 0.10)
                        + (keywordScore * 0.10)
                        + (certificationScore * 0.05);


        int score =
                (int) Math.round(finalScore);


        // ==================================================
        // STATUS
        // ==================================================

        String status;

        if (score >= 70) {

            status = "SHORTLISTED";

        } else if (score >= 50) {

            status = "REVIEW";

        } else {

            status = "NOT SHORTLISTED";
        }


        // ==================================================
        // RESULT
        // ==================================================

        return new AnalysisResult(
                score,
                status,
                matchedSkills,
                missingSkills,
                skillScore,
                experienceScore,
                projectScore,
                educationScore,
                keywordScore,
                certificationScore,
                recommendations,
                categorizedSkills
        );
    }


    // ======================================================
    // NORMALIZE TEXT
    // ======================================================

    private String normalizeText(String text) {

        if (text == null) {
            return "";
        }

        return text
                .toLowerCase()
                .replaceAll("\\s+", " ")
                .trim();
    }


    // ======================================================
    // ACCURATE SKILL MATCHING
    // ======================================================

    private boolean containsSkill(
            String text,
            String skill
    ) {

        if (text == null || skill == null) {
            return false;
        }

        String normalizedText =
                normalizeText(text);

        String normalizedSkill =
                normalizeText(skill);

        String escapedSkill =
                Pattern.quote(normalizedSkill);

        String regex =
                "(?<![a-zA-Z0-9])"
                        + escapedSkill
                        + "(?![a-zA-Z0-9])";

        return Pattern
                .compile(regex)
                .matcher(normalizedText)
                .find();
    }


    // ======================================================
    // EXPERIENCE SCORE
    // ======================================================

    private int calculateExperienceScore(
            String resume,
            String job
    ) {

        String[] experienceKeywords = {

                "work experience",
                "professional experience",
                "employment",
                "internship",
                "intern",
                "developer",
                "software engineer"
        };

        int resumeMatches =
                countMatches(
                        resume,
                        experienceKeywords
                );

        boolean requiresExperience =
                job.contains("years of experience")
                        || job.contains("year of experience")
                        || job.contains("work experience")
                        || job.contains("professional experience")
                        || job.contains("prior experience");


        if (!requiresExperience) {

            if (resumeMatches >= 3) {
                return 80;

            } else if (resumeMatches >= 2) {
                return 70;

            } else if (resumeMatches >= 1) {
                return 60;

            } else {
                return 50;
            }
        }


        if (resumeMatches >= 5) {
            return 100;

        } else if (resumeMatches >= 4) {
            return 90;

        } else if (resumeMatches >= 3) {
            return 80;

        } else if (resumeMatches >= 2) {
            return 70;

        } else if (resumeMatches == 1) {
            return 50;
        }

        return 0;
    }


    // ======================================================
    // PROJECT SCORE
    // ======================================================

    private int calculateProjectScore(
            String resume,
            String job
    ) {

        String[] projectIndicators = {

                "projects",
                "project",
                "developed",
                "built",
                "implemented",
                "designed",
                "created"
        };

        int projectMatches =
                countMatches(
                        resume,
                        projectIndicators
                );


        String[] technicalKeywords = {

                "java",
                "python",
                "javascript",
                "react",
                "node.js",
                "express",
                "sql",
                "mysql",
                "mongodb",
                "api",
                "rest api",
                "spring boot",
                "django"
        };

        int technicalMatches =
                countMatches(
                        resume,
                        technicalKeywords
                );


        if (projectMatches >= 5
                && technicalMatches >= 6) {

            return 100;
        }

        if (projectMatches >= 4
                && technicalMatches >= 5) {

            return 90;
        }

        if (projectMatches >= 3
                && technicalMatches >= 4) {

            return 80;
        }

        if (projectMatches >= 2
                && technicalMatches >= 2) {

            return 70;
        }

        if (projectMatches >= 1
                && technicalMatches >= 1) {

            return 50;
        }

        return 0;
    }


    // ======================================================
    // EDUCATION SCORE
    // ======================================================

    private int calculateEducationScore(
            String resume
    ) {

        String[] educationKeywords = {

                "education",
                "b.tech",
                "btech",
                "b.e",
                "bachelor",
                "computer science",
                "engineering",
                "degree",
                "university",
                "college"
        };

        int educationMatches =
                countMatches(
                        resume,
                        educationKeywords
                );


        boolean hasBachelorDegree =
                resume.contains("b.tech")
                        || resume.contains("btech")
                        || resume.contains("b.e")
                        || resume.contains("bachelor")
                        || resume.contains("bachelor's");


        boolean hasComputerScience =
                resume.contains("computer science")
                        || resume.contains("cse");


        boolean hasInstitution =
                resume.contains("university")
                        || resume.contains("college")
                        || resume.contains("institute");


        if (hasBachelorDegree
                && hasComputerScience
                && hasInstitution) {

            return 100;
        }

        if (hasBachelorDegree
                && hasComputerScience) {

            return 90;
        }

        if (hasBachelorDegree) {

            return 80;
        }

        if (educationMatches >= 3) {

            return 70;
        }

        if (educationMatches >= 2) {

            return 60;
        }

        if (educationMatches == 1) {

            return 40;
        }

        return 0;
    }


    // ======================================================
    // KEYWORD SCORE
    // ======================================================

    private int calculateKeywordScore(
            String resume,
            String job
    ) {

        String[] importantKeywords = {

                "developer",
                "software",
                "application",
                "api",
                "database",
                "frontend",
                "backend",
                "full stack",
                "testing",
                "deployment",
                "problem solving",
                "communication",
                "team",
                "debug",
                "performance",
                "version control",
                "data structures",
                "algorithms",
                "object oriented programming",
                "responsive",
                "scalable",
                "maintainable"
        };

        int requiredKeywords = 0;
        int matchedKeywords = 0;


        for (String keyword :
                importantKeywords) {

            if (job.contains(keyword)) {

                requiredKeywords++;

                if (resume.contains(keyword)) {

                    matchedKeywords++;
                }
            }
        }


        if (requiredKeywords == 0) {
            return 50;
        }


        return (matchedKeywords * 100)
                / requiredKeywords;
    }


    // ======================================================
    // CERTIFICATION SCORE
    // ======================================================

    private int calculateCertificationScore(
            String resume
    ) {

        if (resume == null || resume.isBlank()) {
            return 0;
        }

        String text = normalizeText(resume);

        String[] certificationIndicators = {

                // Cloud certifications
                "aws",
                "amazon web services",
                "azure",
                "microsoft azure",
                "google cloud",
                "gcp",

                // AI / Data certifications
                "generative ai",
                "artificial intelligence",
                "machine learning",
                "data science",

                // Technology certifications
                "mern stack",
                "java certification",
                "python certification",
                "javascript certification",
                "react certification",
                "spring boot certification",

                // DevOps
                "devops",
                "docker",
                "kubernetes",

                // Certification providers
                "nasscom",
                "futureskills",
                "future skills prime",
                "blackbucks",
                "coursera",
                "udemy",
                "ibm",
                "oracle",
                "microsoft certification",
                "google certification"
        };

        int certificationCount = 0;

        for (String keyword : certificationIndicators) {

            if (containsSkill(text, keyword)) {
                certificationCount++;
            }
        }


        /*
         * Certification score
         */

        if (certificationCount >= 6) {
            return 100;
        }

        if (certificationCount >= 4) {
            return 90;
        }

        if (certificationCount >= 3) {
            return 85;
        }

        if (certificationCount >= 2) {
            return 70;
        }

        if (certificationCount >= 1) {
            return 50;
        }

        return 0;
    }

    // ======================================================
    // RECOMMENDATIONS
    // ======================================================

    private List<String> generateRecommendations(
            int skillScore,
            int experienceScore,
            int projectScore,
            int educationScore,
            int keywordScore,
            int certificationScore,
            List<String> missingSkills
    ) {

        List<String> recommendations =
                new ArrayList<>();


        if (skillScore < 70
                && !missingSkills.isEmpty()) {

            recommendations.add(
                    "Consider adding relevant skills: "
                            + String.join(
                            ", ",
                            missingSkills
                    )
            );
        }


        if (experienceScore < 70) {

            recommendations.add(
                    "Strengthen your experience section "
                            + "with internships and measurable achievements."
            );
        }


        if (projectScore < 70) {

            recommendations.add(
                    "Add more relevant projects and clearly "
                            + "mention the technologies used."
            );
        }


        if (keywordScore < 70) {

            recommendations.add(
                    "Add important keywords from the job "
                            + "description that match your actual experience."
            );
        }


        if (certificationScore == 0) {

            recommendations.add(
                    "Consider adding relevant technical "
                            + "certifications to strengthen your profile."
            );
        }


        if (educationScore < 70) {

            recommendations.add(
                    "Provide complete education details "
                            + "including degree, specialization, "
                            + "institution, and graduation year."
            );
        }


        if (recommendations.isEmpty()) {

            recommendations.add(
                    "Your resume has good alignment with "
                            + "the job description."
            );
        }


        if (recommendations.size() > 5) {

            return new ArrayList<>(
                    recommendations.subList(0, 5)
            );
        }


        return recommendations;
    }


    // ======================================================
    // CATEGORIZE MATCHED SKILLS
    // ======================================================

    private Map<String, List<String>> categorizeMatchedSkills(
            List<String> matchedSkills
    ) {

        Map<String, List<String>> categorized =
                new LinkedHashMap<>();


        for (Map.Entry<String, List<String>> entry :
                skillCategories.entrySet()) {

            List<String> categoryMatches =
                    new ArrayList<>();


            for (String matched :
                    matchedSkills) {

                String normalized =
                        matched.toLowerCase();


                if (entry.getValue()
                        .contains(normalized)) {

                    categoryMatches.add(matched);
                }
            }


            if (!categoryMatches.isEmpty()) {

                categorized.put(
                        entry.getKey(),
                        categoryMatches
                );
            }
        }


        return categorized;
    }


    // ======================================================
    // COMMON KEYWORD COUNTER
    // ======================================================

    private int countMatches(
            String text,
            String[] keywords
    ) {

        int count = 0;


        for (String keyword :
                keywords) {

            if (text.contains(keyword)) {

                count++;
            }
        }


        return count;
    }


    // ======================================================
    // SKILL DISPLAY FORMATTER
    // ======================================================

    private String formatSkillName(
            String skill
    ) {

        switch (skill) {

            case "java":
                return "Java";

            case "python":
                return "Python";

            case "javascript":
                return "JavaScript";

            case "typescript":
                return "TypeScript";

            case "c":
                return "C";

            case "c++":
                return "C++";

            case "c#":
                return "C#";

            case "go":
                return "Go";

            case "rust":
                return "Rust";

            case "php":
                return "PHP";

            case "ruby":
                return "Ruby";

            case "kotlin":
                return "Kotlin";

            case "swift":
                return "Swift";

            case "html":
                return "HTML";

            case "css":
                return "CSS";

            case "react":
                return "React";

            case "angular":
                return "Angular";

            case "vue":
                return "Vue";

            case "next.js":
                return "Next.js";

            case "tailwind":
                return "Tailwind";

            case "bootstrap":
                return "Bootstrap";

            case "node.js":
                return "Node.js";

            case "express":
                return "Express";

            case "spring boot":
                return "Spring Boot";

            case "spring":
                return "Spring";

            case "django":
                return "Django";

            case "flask":
                return "Flask";

            case "fastapi":
                return "FastAPI";

            case "asp.net":
                return "ASP.NET";

            case "rest api":
                return "REST API";

            case "graphql":
                return "GraphQL";

            case "sql":
                return "SQL";

            case "mysql":
                return "MySQL";

            case "postgresql":
                return "PostgreSQL";

            case "oracle":
                return "Oracle";

            case "mongodb":
                return "MongoDB";

            case "redis":
                return "Redis";

            case "sqlite":
                return "SQLite";

            case "aws":
                return "AWS";

            case "azure":
                return "Azure";

            case "google cloud":
                return "Google Cloud";

            case "docker":
                return "Docker";

            case "kubernetes":
                return "Kubernetes";

            case "jenkins":
                return "Jenkins";

            case "git":
                return "Git";

            case "github":
                return "GitHub";

            case "gitlab":
                return "GitLab";

            case "ci/cd":
                return "CI/CD";

            case "pandas":
                return "Pandas";

            case "numpy":
                return "NumPy";

            case "matplotlib":
                return "Matplotlib";

            case "seaborn":
                return "Seaborn";

            case "power bi":
                return "Power BI";

            case "tableau":
                return "Tableau";

            case "excel":
                return "Excel";

            case "data visualization":
                return "Data Visualization";

            case "statistics":
                return "Statistics";

            case "data analysis":
                return "Data Analysis";

            case "data cleaning":
                return "Data Cleaning";

            case "machine learning":
                return "Machine Learning";

            case "deep learning":
                return "Deep Learning";

            case "data science":
                return "Data Science";

            case "artificial intelligence":
                return "Artificial Intelligence";

            case "natural language processing":
                return "Natural Language Processing";

            case "nlp":
                return "NLP";

            case "tensorflow":
                return "TensorFlow";

            case "pytorch":
                return "PyTorch";

            case "scikit-learn":
                return "Scikit-learn";

            case "junit":
                return "JUnit";

            case "selenium":
                return "Selenium";

            case "postman":
                return "Postman";

            case "api testing":
                return "API Testing";

            case "unit testing":
                return "Unit Testing";

            case "microservices":
                return "Microservices";

            case "agile":
                return "Agile";

            case "scrum":
                return "Scrum";

            case "problem solving":
                return "Problem Solving";

            case "communication":
                return "Communication";

            default:
                return skill;
        }
    }
}