import React, { useState } from 'react';
import axios from 'axios';
import LibraryButton from './LibraryButton';

function Office({ mode }) {
  const [officeName, setOfficeName] = useState('');
  const [officeNumber, setOfficeNumber] = useState('');
  const [message, setMessage] = useState('');

  const getErrorMessage = (error) => {
    if (error.response) {
      const data = error.response.data;

      if (typeof data === 'string') {
        return `Error ${error.response.status}: ${data}`;
      }

      return `Error ${error.response.status}: ${
        data?.message || data?.error || JSON.stringify(data)
      }`;
    }

    if (error.request) {
      return 'No response from the backend. Make sure Spring Boot is running on port 1014.';
    }

    return error.message;
  };

  const SaveOffice = async () => {
    if (!officeName.trim() || !officeNumber) {
      setMessage('Please enter the office name and number.');
      return;
    }

    try {
      const response = await axios.post(
        'http://localhost:1014/api/offices/save',
        {
          name: officeName.trim(),
          officeNumber: parseInt(officeNumber, 10)
        }
      );

      setMessage(
        typeof response.data === 'string'
          ? response.data
          : 'Office saved successfully.'
      );

      setOfficeName('');
      setOfficeNumber('');
    } catch (error) {
      console.error('Save office error:', error);
      setMessage(getErrorMessage(error));
    }
  };

  const DeleteOffice = async () => {
    if (!officeNumber) {
      setMessage('Please enter the office number.');
      return;
    }

    try {
      const response = await axios.get(
        'http://localhost:1014/api/offices'
      );

      const office = response.data.find(
        (item) => item.officeNumber === parseInt(officeNumber, 10)
      );

      if (!office) {
        setMessage('Office not found.');
        return;
      }

      await axios.delete(
        `http://localhost:1014/api/offices/${office.id}`
      );

      setMessage('Office deleted successfully.');
      setOfficeNumber('');
    } catch (error) {
      console.error('Delete office error:', error);
      setMessage(getErrorMessage(error));
    }
  };

  return (
    <div className="section">
      {mode === 'save' ? (
        <>
          <h2>Save Office</h2>

          <label>Office Name</label>
          <input
            type="text"
            value={officeName}
            onChange={(e) => setOfficeName(e.target.value)}
            placeholder="Enter office name"
          />

          <label>Office Number</label>
          <input
            type="number"
            value={officeNumber}
            onChange={(e) => setOfficeNumber(e.target.value)}
            placeholder="Enter office number"
          />

          <LibraryButton
            buttonLabel="Save Office"
            onClickButton={SaveOffice}
          />
        </>
      ) : (
        <>
          <h2>Delete Office</h2>

          <label>Office Number</label>
          <input
            type="number"
            value={officeNumber}
            onChange={(e) => setOfficeNumber(e.target.value)}
            placeholder="Enter office number to delete"
          />

          <LibraryButton
            buttonLabel="Delete Office"
            onClickButton={DeleteOffice}
          />
        </>
      )}

      {message && <p className="message">{message}</p>}
    </div>
  );
}

export default Office;