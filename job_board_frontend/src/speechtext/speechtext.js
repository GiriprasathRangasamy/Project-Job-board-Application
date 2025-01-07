import React, { useState } from 'react';
import axios from 'axios';

const Speech = () => {
  const [generatedText, setGeneratedText] = useState('');
  const [generatedText1, setGeneratedText1] = useState('');
  const [audioUrl, setAudioUrl] = useState(null);
  const [summary, setSummary] = useState('');
  const [validationResult, setValidationResult] = useState('');
  const API_KEY = 'AIzaSyAbBZLvQKCvZYdEwvSrCPJvitb-iaIssSk'; 
  const API_URL = `https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${API_KEY}`; 

  const styles = {
    sectionHeader: {
      fontSize: '20px',
      fontWeight: 'bold',
    },
    boldText: {
      fontWeight: 'bold',
    },
    bulletPoint: {
      listStyleType: 'disc',
      paddingLeft: '20px',
    },
    text: {
      fontSize: '16px',
    },
    analysisWrapper: {
      margin: '20px 0',
    },
  };

  // Placeholder for Gemini AI Flash 1.5 text generation
  const generateTextf = async () => {
    try {
      const response = await axios.post(API_URL, {
        contents: [
          {
            role: 'user',
            parts: [
              {
                text: `Generate a one side random english conversation.only give paragraph atleast 10 lines `,
              },
            ],
          },
        ],
      });
      console.log(response.data.candidates[0].content.parts[0].text);
      setGeneratedText(response.data.candidates[0].content.parts[0].text);
    } catch (error) {
      console.error('Error generating text:', error);
    }
  };
  const convertToAudio = async () => {
    try {
      const response = await axios.post('http://127.0.0.1:5000/text-to-speech', { text: generatedText }, { responseType: 'blob' });

      const url = URL.createObjectURL(new Blob([response.data], { type: 'audio/mpeg' }));
      setAudioUrl(url);
    } catch (error) {
      console.error("Error converting text to speech:", error);
    }
  };

  // Submit Summary for Validation
  const validateSummary = async () => {
    try {
      const response = await axios.post(API_URL, {
        contents: [
          {
            role: 'user',
            parts: [
              {
                text: `${summary} is summary of ${generatedText} check for spelling,grammar error list it down and also give improved content`,
              },
            ],
          },
        ],
      });
      console.log(response.data.candidates[0].content.parts[0].text);
      setGeneratedText1(response.data.candidates[0].content.parts[0].text);
    } catch (error) {
      console.error('Error generating text:', error);
    }
  };

  // Clean the text (removes unnecessary formatting)
  const cleanText = (text) => {
    return text
      .replace(/\*\*/g, '') // Remove all double stars
      .replace(/\*/g, '')   // Remove all single stars
      .replace(/undefined/g, '') // Remove 'undefined'
      .trim();              // Remove extra whitespace
  };

  // Function to render the resume analysis in a formatted manner
  const renderResumeAnalysis = (analysisText) => {
    const lines = analysisText.split('\n');

    const renderBoldUnderlineHeader = (text) => (
      <h2 key={text} style={{ ...styles.sectionHeader, textDecoration: 'underline' }}>
        {cleanText(text)}
      </h2>
    );

    const renderBoldText = (text) => (
      <strong key={text} style={styles.boldText}>
        {cleanText(text)}
      </strong>
    );

    const renderBulletPoint = (text) => (
      <li key={text} style={styles.bulletPoint}>{cleanText(text)}</li>
    );

    const analysisContent = lines.map((line, index) => {
      if (line.startsWith('**') && line.endsWith('**')) {
        return renderBoldUnderlineHeader(line.slice(2, -2));
      }

      if (line.startsWith('**') && line.endsWith('**')) {
        return renderBoldText(line.slice(2, -2));
      }

      if (line.startsWith('* ')) {
        return renderBulletPoint(line.slice(2));
      }

      if (line.trim() !== '') {
        return <p key={index} style={styles.text}>{cleanText(line)}</p>;
      }

      return null;
    });

    return <div style={styles.analysisWrapper}>{analysisContent}</div>;
  };

  return (
    <div style={{ padding: '20px', fontFamily: 'Arial' }}>
      <h1>Text-to-Speech App</h1>
      <button onClick={generateTextf}>Generate Technology Text</button>
      {generatedText && (
        <>
          <p><strong>Generated Text:</strong> {generatedText}</p>
          <button onClick={convertToAudio}>Convert to Audio</button>
        </>
      )}
      {audioUrl && (
        <div>
          <audio controls>
            <source src={audioUrl} type="audio/mpeg" />
            Your browser does not support the audio element.
          </audio>
          <div>
            <textarea
              placeholder="Summarize the audio content"
              value={summary}
              onChange={(e) => setSummary(e.target.value)}
              rows="4"
              cols="50"
            />
            <button onClick={validateSummary}>Validate Summary</button>
          </div>
        </div>
      )}
      {validationResult && <p><strong>Validation Result:</strong> {validationResult}</p>}

      {generatedText1 && (
        <div>
          <h2>Improved Content:</h2>
          {renderResumeAnalysis(generatedText1)}
        </div>
      )}
    </div>
  );
};

export default Speech;
