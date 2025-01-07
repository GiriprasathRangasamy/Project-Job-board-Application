import React from 'react';
import ReactDOM from 'react-dom/client';

import App from './App';
import reportWebVitals from './reportWebVitals';
import MapView from './components/Mapview';
import ChatBot from './components/chatbot';
import Resumeanalyser from './components/resumeanalyser';
import Docverifyuserregester from './components/docverifyuserregester';
import DocumentTable from './components/documentverifyhomepage';
import Speech from './speechtext/speechtext';


const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    {/* <App/>
    <ChatBot />
    <DocumentTable/> */}
    <Speech/>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
