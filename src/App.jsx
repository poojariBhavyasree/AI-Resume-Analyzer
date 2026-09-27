import { useState } from "react";
import jsPDF from "jspdf";
import {
  Upload,
  FileText,
  Sparkles,
  CheckCircle,
  Target,
  Award,
  Lightbulb,
} from "lucide-react";

import { analyzeResume } from "./services/resumeApi";
import "./App.css";

function App() {
  const [resume, setResume] = useState(null);
  const [jobDescription, setJobDescription] = useState("");
  const [result, setResult] = useState(null);

  // =========================
  // PDF REPORT
  // =========================

  const downloadReport = () => {
    if (!result) return;

    const doc = new jsPDF();

    // HEADER
    doc.setFillColor(79, 70, 229);
    doc.rect(0, 0, 210, 35, "F");

    doc.setTextColor(255, 255, 255);
    doc.setFontSize(22);
    doc.setFont("helvetica", "bold");
    doc.text("AI Resume Analyzer", 20, 18);

    doc.setFontSize(11);
    doc.setFont("helvetica", "normal");
    doc.text("ATS Analysis Report", 20, 27);

    // ATS SCORE
    doc.setTextColor(40, 40, 50);
    doc.setFontSize(16);
    doc.setFont("helvetica", "bold");
    doc.text("ATS SCORE", 20, 52);

    doc.setFontSize(32);
    doc.setTextColor(79, 70, 229);
    doc.text(`${result.score}%`, 20, 70);

    const status =
      result.score >= 80
        ? "Excellent Match"
        : result.score >= 60
        ? "Good Match"
        : result.score >= 40
        ? "Moderate Match"
        : "Low Match";

    doc.setFontSize(12);
    doc.setTextColor(90, 90, 100);
    doc.setFont("helvetica", "normal");
    doc.text(status, 20, 80);

    // SCORE BREAKDOWN
    doc.setFontSize(16);
    doc.setFont("helvetica", "bold");
    doc.setTextColor(40, 40, 50);
    doc.text("Score Breakdown", 20, 105);

    const scores = [
      ["Skills", result.skillScore],
      ["Experience", result.experienceScore],
      ["Projects", result.projectScore],
      ["Education", result.educationScore],
      ["Keywords", result.keywordScore],
      ["Certifications", result.certificationScore],
    ];

    let y = 118;

    scores.forEach(([label, score]) => {
      doc.setFontSize(11);
      doc.setFont("helvetica", "normal");
      doc.setTextColor(70, 70, 80);

      doc.text(label, 20, y);
      doc.text(`${score}%`, 175, y);

      doc.setFillColor(235, 237, 245);
      doc.roundedRect(20, y + 3, 160, 5, 2, 2, "F");

      doc.setFillColor(79, 70, 229);
      doc.roundedRect(
        20,
        y + 3,
        (160 * score) / 100,
        5,
        2,
        2,
        "F"
      );

      y += 16;
    });

    // SKILLS ANALYSIS
    y += 8;

    if (y > 235) {
      doc.addPage();
      y = 20;
    }

    doc.setFontSize(16);
    doc.setFont("helvetica", "bold");
    doc.setTextColor(40, 40, 50);
    doc.text("Skills Analysis", 20, y);

    y += 12;

    doc.setFontSize(12);
    doc.setFont("helvetica", "bold");
    doc.setTextColor(21, 128, 61);
    doc.text("Matched Skills", 20, y);

    y += 8;

    doc.setFont("helvetica", "normal");
    doc.setTextColor(70, 70, 80);

    if (result.matchedSkills?.length) {
      const matchedText = result.matchedSkills.join(" • ");

      const matchedLines = doc.splitTextToSize(
        matchedText,
        170
      );

      doc.text(matchedLines, 20, y);

      y += matchedLines.length * 7;
    } else {
      doc.text("No matched skills found.", 20, y);
      y += 7;
    }

    y += 6;

    doc.setFont("helvetica", "bold");
    doc.setTextColor(194, 65, 12);
    doc.text("Missing Skills", 20, y);

    y += 8;

    doc.setFont("helvetica", "normal");
    doc.setTextColor(70, 70, 80);

    if (result.missingSkills?.length) {
      const missingText = result.missingSkills.join(" • ");

      const missingLines = doc.splitTextToSize(
        missingText,
        170
      );

      doc.text(missingLines, 20, y);

      y += missingLines.length * 7;
    } else {
      doc.text("No missing skills found.", 20, y);
      y += 7;
    }

    // RESUME IMPROVEMENTS
    y += 12;

    if (y > 235) {
      doc.addPage();
      y = 20;
    }

    doc.setFontSize(16);
    doc.setFont("helvetica", "bold");
    doc.setTextColor(40, 40, 50);

    doc.text("Resume Improvements", 20, y);

    y += 12;

    doc.setFontSize(11);
    doc.setFont("helvetica", "normal");
    doc.setTextColor(70, 70, 80);

    if (result.recommendations?.length) {
      result.recommendations.forEach(
        (recommendation, index) => {
          const lines = doc.splitTextToSize(
            `${index + 1}. ${recommendation}`,
            165
          );

          if (y + lines.length * 7 > 275) {
            doc.addPage();
            y = 20;
          }

          doc.text(lines, 20, y);
          y += lines.length * 7 + 5;
        }
      );
    } else {
      doc.text(
        "No additional recommendations.",
        20,
        y
      );
    }

    // FOOTER
    const pageCount =
      doc.internal.getNumberOfPages();

    for (let i = 1; i <= pageCount; i++) {
      doc.setPage(i);

      doc.setFontSize(9);
      doc.setTextColor(140, 140, 150);

      doc.text(
        `AI Resume Analyzer • Page ${i} of ${pageCount}`,
        20,
        290
      );
    }

    doc.save("AI-Resume-Analysis-Report.pdf");
  };

  // =========================
  // RESUME UPLOAD
  // =========================

  const handleResumeUpload = (event) => {
    const file = event.target.files[0];

    if (file) {
      setResume(file);
      setResult(null);
    }
  };

  // =========================
  // ANALYZE RESUME
  // =========================

  const handleAnalyze = async () => {
    if (!resume) {
      alert("Please upload your resume PDF.");
      return;
    }

    if (!jobDescription.trim()) {
      alert("Please enter the job description.");
      return;
    }

    try {
      const data = await analyzeResume(
        resume,
        jobDescription
      );

      setResult(data);
    } catch (error) {
      console.error("Analysis failed:", error);

      alert(
        "Unable to analyze resume. Please check whether the backend is running."
      );
    }
  };

  // =========================
  // CLEAR / RESET
  // =========================

  const handleClear = () => {
    setResume(null);
    setJobDescription("");
    setResult(null);
  };

  // =========================
  // STATUS
  // =========================

  const getStatus = () => {
    if (result.score >= 80) return "Excellent Match";
    if (result.score >= 60) return "Good Match";
    if (result.score >= 40) return "Moderate Match";
    return "Low Match";
  };

  const getStatusClass = () => {
    if (result.score >= 80) return "status-excellent";
    if (result.score >= 60) return "status-good";
    if (result.score >= 40) return "status-moderate";
    return "status-low";
  };

  const skillMatchPercentage =
    result?.matchedSkills &&
    result?.missingSkills &&
    result.matchedSkills.length +
      result.missingSkills.length >
      0
      ? Math.round(
          (result.matchedSkills.length /
            (result.matchedSkills.length +
              result.missingSkills.length)) *
            100
        )
      : 0;

  const scoreItems = [
    {
      label: "Skills",
      value: result?.skillScore || 0,
    },
    {
      label: "Experience",
      value: result?.experienceScore || 0,
    },
    {
      label: "Projects",
      value: result?.projectScore || 0,
    },
    {
      label: "Education",
      value: result?.educationScore || 0,
    },
    {
      label: "Keywords",
      value: result?.keywordScore || 0,
    },
    {
      label: "Certifications",
      value: result?.certificationScore || 0,
    },
  ];

  return (
    <div className="app">

      {/* =========================
          NAVBAR
      ========================= */}

      <nav className="navbar">

        <div className="logo">
          <Sparkles size={23} />
          <span>ResumeAI</span>
        </div>

        <div className="nav-links">
          <a href="#home">Home</a>
          <a href="#analyzer">Analyzer</a>
          <a href="#about">About</a>
        </div>

      </nav>


      {/* =========================
          HERO
      ========================= */}

      <section
        className="hero"
        id="home"
      >

        <div className="hero-content">

          <div className="badge">
            <Sparkles size={15} />
            AI-Powered Resume Analysis
          </div>

          <h1>
            Analyze Your Resume
            <span>
              Against Any Job Description
            </span>
          </h1>

          <p>
            Upload your resume, paste the job
            description, and discover your
            skill match, missing skills, and
            resume compatibility.
          </p>

          <a
            href="#analyzer"
            className="hero-button"
          >
            Start Analysis
          </a>

        </div>

      </section>


      {/* =========================
          ANALYZER
      ========================= */}

      <section
        className="analyzer-section"
        id="analyzer"
      >

        <div className="section-heading">

          <p className="section-label">
            RESUME ANALYZER
          </p>

          <h2>
            Check Your Resume Compatibility
          </h2>

          <p>
            Provide your resume and the target
            job description to analyze how well
            they match.
          </p>

        </div>


        <div className="analyzer-grid">

          {/* RESUME */}

          <div className="card">

            <div className="card-header">

              <div className="icon-box">
                <FileText size={21} />
              </div>

              <div>
                <h3>Upload Resume</h3>
                <p>PDF files only</p>
              </div>

            </div>

            <label className="upload-area">

              <input
                type="file"
                accept=".pdf,application/pdf"
                onChange={handleResumeUpload}
              />

              <Upload size={38} />

              <strong>
                {resume
                  ? resume.name
                  : "Upload your resume"}
              </strong>

              <span>
                {resume
                  ? "✓ Resume selected successfully"
                  : "Click here or drag and drop your PDF"}
              </span>

            </label>

          </div>


          {/* JOB DESCRIPTION */}

          <div className="card">

            <div className="card-header">

              <div className="icon-box">
                <FileText size={21} />
              </div>

              <div>
                <h3>Job Description</h3>
                <p>Paste the target job description</p>
              </div>

            </div>

            <textarea
              className="job-input"
              placeholder="Paste the complete job description here..."
              value={jobDescription}
              onChange={(event) =>
                setJobDescription(
                  event.target.value
                )
              }
            />

          </div>

        </div>


        {/* ACTION BUTTONS */}

        <div className="analyze-container">

          <button
            className="analyze-button"
            onClick={handleAnalyze}
          >
            <Sparkles size={17} />
            Analyze Resume
          </button>

          <button
            className="clear-button"
            onClick={handleClear}
          >
            Clear All
          </button>

        </div>


        {/* =========================
            RESULTS
        ========================= */}

        {result && (

          <div className="results-section">

            <div className="result-heading">

              <p className="section-label">
                ANALYSIS RESULT
              </p>

              <h2>
                Your Resume Analysis
              </h2>

              <p className="result-subtitle">
                A breakdown of how your resume
                aligns with the target role.
              </p>

            </div>


            <div className="result-grid">

              {/* =========================
                  ATS SCORE
              ========================= */}

              <div className="result-card score-card">

                <div className="result-card-label">
                  OVERALL COMPATIBILITY
                </div>

                <h3 className="score-title">
                  Resume Match Score
                </h3>

                <div
                  className="score-circle"
                  style={{
                    "--score": result.score,
                  }}
                >

                  <div className="score-circle-inner">

                    <strong>
                      {result.score}%
                    </strong>

                    <span>
                      ATS Score
                    </span>

                  </div>

                </div>

                <div
                  className={`status ${getStatusClass()}`}
                >
                  <CheckCircle size={17} />
                  {getStatus()}
                </div>

                <div className="score-card-actions">

                  <button
                    className="download-report-btn"
                    onClick={downloadReport}
                  >
                    📄 Download Report
                  </button>

                  <button
                    className="another-resume-btn"
                    onClick={handleClear}
                  >
                    ↻ Analyze Another Resume
                  </button>

                </div>

              </div>


              {/* =========================
                  SCORE BREAKDOWN
              ========================= */}

              <div className="result-card score-breakdown">

                <div className="card-title-row">

                  <div className="small-title-icon">
                    <Target size={18} />
                  </div>

                  <div>
                    <h3>Score Breakdown</h3>

                    <p>
                      Performance across key resume areas
                    </p>
                  </div>

                </div>

                <div className="breakdown-list">

                  {scoreItems.map(
                    (item) => (

                      <div
                        className="breakdown-item"
                        key={item.label}
                      >

                        <div className="breakdown-label">

                          <span>
                            {item.label}
                          </span>

                          <strong>
                            {item.value}%
                          </strong>

                        </div>

                        <div className="progress-bar">

                          <div
                            className="progress-fill"
                            style={{
                              width: `${item.value}%`,
                            }}
                          />

                        </div>

                      </div>

                    )
                  )}

                </div>

              </div>


              {/* =========================
                  QUICK SUMMARY
              ========================= */}

              <div className="result-card quick-summary">

                <div className="card-title-row">

                  <div className="small-title-icon">
                    <Award size={18} />
                  </div>

                  <div>
                    <h3>Analysis Summary</h3>

                    <p>
                      Key results at a glance
                    </p>
                  </div>

                </div>

                <div className="summary-stat">

                  <span>Skills Matched</span>

                  <strong className="summary-green">
                    {result.matchedSkills?.length || 0}
                  </strong>

                </div>

                <div className="summary-stat">

                  <span>Skills Missing</span>

                  <strong className="summary-orange">
                    {result.missingSkills?.length || 0}
                  </strong>

                </div>

                <div className="summary-stat">

                  <span>Skill Match</span>

                  <strong className="summary-purple">
                    {skillMatchPercentage}%
                  </strong>

                </div>

                <div className="summary-stat">

                  <span>Recommendations</span>

                  <strong>
                    {result.recommendations?.length || 0}
                  </strong>

                </div>

              </div>


              {/* =========================
                  RECOMMENDATIONS
              ========================= */}

              <div className="result-card recommendations-card">

                <div className="card-title-row">

                  <div className="small-title-icon">
                    <Lightbulb size={18} />
                  </div>

                  <div>
                    <h3>
                      Resume Improvements
                    </h3>

                    <p>
                      Practical suggestions based
                      on this job description
                    </p>
                  </div>

                </div>

                <div className="recommendations-list">

                  {result.recommendations?.length ? (

                    result.recommendations.map(
                      (recommendation, index) => (

                        <div
                          className="recommendation-item"
                          key={index}
                        >

                          <div className="recommendation-icon">
                            {index === 0
                              ? "🛠"
                              : index === 1
                              ? "💼"
                              : index === 2
                              ? "📁"
                              : index === 3
                              ? "🔑"
                              : "📌"}
                          </div>

                          <div className="recommendation-content">

                            <span>
                              {recommendation
                                .toLowerCase()
                                .includes("certification")
                                ? "Certifications"
                                : recommendation
                                    .toLowerCase()
                                    .includes("experience")
                                ? "Experience"
                                : recommendation
                                    .toLowerCase()
                                    .includes("project")
                                ? "Projects"
                                : recommendation
                                    .toLowerCase()
                                    .includes("keyword")
                                ? "Keywords"
                                : recommendation
                                    .toLowerCase()
                                    .includes("skill")
                                ? "Skills"
                                : "Improvement"}
                            </span>

                            <p>
                              {recommendation}
                            </p>

                          </div>

                        </div>

                      )
                    )

                  ) : (

                    <div className="empty-state">
                      No additional recommendations.
                    </div>

                  )}

                </div>

              </div>


              {/* =========================
                  SKILLS ANALYSIS
              ========================= */}

              <div className="result-card skills-analysis-card">

                <div className="skills-analysis-header">

                  <div>

                    <div className="skills-title-wrapper">

                      <div className="skills-title-icon">
                        <Target size={19} />
                      </div>

                      <div>
                        <h3>
                          Skills Analysis
                        </h3>

                        <p>
                          Required skills found in
                          your resume
                        </p>
                      </div>

                    </div>

                  </div>

                  <div className="skill-match-percentage">
                    {skillMatchPercentage}%
                  </div>

                </div>


                {/* SKILL CATEGORIES */}

                {result.categorizedSkills &&
                Object.entries(
                  result.categorizedSkills
                ).length > 0 ? (

                  <div className="skills-categories">

                    {Object.entries(
                      result.categorizedSkills
                    ).map(
                      ([category, skills]) => (

                        <div
                          className="skill-category"
                          key={category}
                        >

                          <h4>
                            {category}
                          </h4>

                          <div className="skills-list">

                            {skills.map(
                              (skill, index) => (

                                <span
                                  key={`${category}-${skill}-${index}`}
                                  className="skill matched"
                                >
                                  ✓ {skill}
                                </span>

                              )
                            )}

                          </div>

                        </div>

                      )
                    )}

                  </div>

                ) : (

                  <div className="skill-category">

                    <h4>
                      Matched Skills
                    </h4>

                    <div className="skills-list">

                      {result.matchedSkills?.map(
                        (skill, index) => (

                          <span
                            className="skill matched"
                            key={`${skill}-${index}`}
                          >
                            ✓ {skill}
                          </span>

                        )
                      )}

                    </div>

                  </div>

                )}


                {/* MISSING SKILLS */}

                <div className="missing-skills-section">

                  <div className="missing-header">

                    <div>
                      <h4>
                        Missing Skills
                      </h4>

                      <p>
                        Skills from the job description
                        that were not detected.
                      </p>
                    </div>

                    <span className="missing-count-badge">
                      {result.missingSkills?.length || 0}
                    </span>

                  </div>

                  <div className="skills-list missing-skills">

                    {result.missingSkills?.length ? (

                      result.missingSkills.map(
                        (skill, index) => (

                          <span
                            className="skill missing"
                            key={`${skill}-${index}`}
                          >
                            ⚠ {skill}
                          </span>

                        )
                      )

                    ) : (

                      <span className="no-missing">
                        ✓ No missing skills detected
                      </span>

                    )}

                  </div>

                </div>


                {/* SKILLS SUMMARY */}

                <div className="skills-summary">

                  <div className="skills-summary-item">

                    <span>Matched</span>

                    <strong className="matched-count">
                      {result.matchedSkills?.length || 0}
                    </strong>

                  </div>

                  <div className="skills-summary-item">

                    <span>Missing</span>

                    <strong className="missing-count">
                      {result.missingSkills?.length || 0}
                    </strong>

                  </div>

                </div>

              </div>

            </div>

          </div>

        )}

      </section>


      {/* =========================
          ABOUT
      ========================= */}

      <section
        className="about-section"
        id="about"
      >

        <div className="about-icon">
          <Sparkles size={22} />
        </div>

        <p className="section-label">
          ABOUT RESUMEAI
        </p>

        <h2>
          Smarter Resume Screening
        </h2>

        <p>
          ResumeAI helps candidates understand
          how closely their resume matches a
          specific job description by analyzing
          relevant skills, experience, projects,
          education, keywords, and certifications.
        </p>

      </section>


      {/* =========================
          FOOTER
      ========================= */}

      <footer>

        <div className="footer-logo">
          <Sparkles size={16} />
          ResumeAI
        </div>

        <p>
          © 2026 ResumeAI • AI Resume Analyzer
        </p>

      </footer>

    </div>
  );
}

export default App;