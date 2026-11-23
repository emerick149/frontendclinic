import React, { useState } from 'react';
import './index.css';

import Office from './components/Office';
import OfficeDisplay from './components/OfficeDisplay';
import LibraryButton from './components/LibraryButton';

function App() {
  const [page, setPage] = useState('home');

  if (page === 'save') {
    return (
      <div className="container">
        <Office mode="save" />

        <LibraryButton
          buttonLabel="Back to Home"
          onClickButton={() => setPage('home')}
        />
      </div>
    );
  }

  if (page === 'delete') {
    return (
      <div className="container">
        <Office mode="delete" />

        <LibraryButton
          buttonLabel="Back to Home"
          onClickButton={() => setPage('home')}
        />
      </div>
    );
  }

  if (page === 'display') {
    return (
      <div className="container">
        <OfficeDisplay />

        <LibraryButton
          buttonLabel="Back to Home"
          onClickButton={() => setPage('home')}
        />
      </div>
    );
  }

  return (
    <div className="home">
      <h1>Clinic Management System</h1>
      <h2>Home</h2>

      <div className="buttons">
        <LibraryButton
          buttonLabel="Save Office"
          onClickButton={() => setPage('save')}
        />

        <LibraryButton
          buttonLabel="Delete Office"
          onClickButton={() => setPage('delete')}
        />

        <LibraryButton
          buttonLabel="Display Offices"
          onClickButton={() => setPage('display')}
        />
      </div>
    </div>
  );
}

export default App;