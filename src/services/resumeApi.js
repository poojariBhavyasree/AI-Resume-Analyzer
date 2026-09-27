import axios from "axios";

const API_URL = "http://localhost:8080/api";

export const analyzeResume = async (resume, jobDescription) => {
  const formData = new FormData();

  formData.append("resume", resume);
  formData.append("jobDescription", jobDescription);

  console.log("Resume file:", resume);
  console.log("File name:", resume?.name);
  console.log("File type:", resume?.type);
  console.log("File size:", resume?.size);

  const response = await axios.post(
    `${API_URL}/resume/analyze`,
    formData
  );

  return response.data;
};