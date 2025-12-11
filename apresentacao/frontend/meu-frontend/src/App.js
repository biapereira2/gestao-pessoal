// frontend/meu-frontend/src/App.js

import React from 'react';
import AppRouter from './routes/Router'; // Ou Router, dependendo de como você exportou

function App() {
  return (
    <div className="App">
      <AppRouter />
    </div>
  );
}

export default App;