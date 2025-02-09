import { useState } from "react";
import "./App.css";

function App() {
  const [code, setCode] = useState("");
  const [problemId, setProblemId] = useState("");
  const [token, setToken] = useState("");
  // Handle textarea changes
  const handleCodeChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setCode(e.target.value);
  };

  const handleProblemIdChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setProblemId(e.target.value);
  };

  const handleTokenChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setToken(e.target.value);
  };

  // Handle form submission
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const formattedCode = code
      .replace(/(\n)/g, "\n") // Replace newlines with '\n'
      .replace(/"/g, '"');

    const data = {
      code: formattedCode,
    };

    try {
      const response = await fetch(
        "http://localhost:8080/api/v1/solution/problem/" + problemId,
        {
          method: "POST",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify(data),
        }
      );

      if (!response.ok) {
        throw new Error("Failed to submit the form");
      }

      // Handle success
      alert("Submission successful!");
    } catch (error) {
      console.error(error);
      alert("An error occurred while submitting.");
    }
  };

  return (
    <>
      <form className="form" onSubmit={handleSubmit}>
        <input
          type="number"
          placeholder="ProblemID"
          value={problemId}
          onChange={handleProblemIdChange}
        />
        <br />
        <textarea
          id="message"
          name="message"
          rows={4}
          cols={50}
          placeholder="Enter your C++ code here..."
          value={code}
          onChange={handleCodeChange}
        />
        <br />
        <button type="submit">Submit</button>
      </form>
    </>
  );
}

export default App;
