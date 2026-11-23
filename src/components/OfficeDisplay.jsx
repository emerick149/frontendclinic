
import React, { useEffect, useState } from 'react';
import axios from 'axios';

function OfficeDisplay() {
  const [offices, setOffices] = useState([]);
  const [search, setSearch] = useState('');
  const [message, setMessage] = useState('');

  // Get all offices from the backend
  const DisplayOffices = async () => {
    try {
      const response = await axios.get('http://localhost:1014/api/offices');
      setOffices(response.data);
      setMessage('');
    } catch (error) {
      console.error('Display offices error:', error);
      setMessage('Unable to display offices. Check the backend.');
    }
  };

  // Delete an office using its ID
  const DeleteOffice = async (id) => {
    const confirmDelete = window.confirm(
      'Are you sure you want to delete this office?'
    );

    if (!confirmDelete) {
      return;
    }

    try {
      await axios.delete(`http://localhost:1014/api/offices/${id}`);
      setMessage('Office deleted successfully.');
      DisplayOffices();
    } catch (error) {
      console.error('Delete office error:', error);
      setMessage('Unable to delete office.');
    }
  };

  // Search by office name or office number
  const filteredOffices = offices.filter((office) =>
    office.name.toLowerCase().includes(search.toLowerCase()) ||
    office.officeNumber.toString().includes(search)
  );

  useEffect(() => {
    DisplayOffices();
  }, []);

  return (
    <div className="section">
      <h2>Display All Offices</h2>

      <input
        type="text"
        placeholder="Search by office name or number"
        value={search}
        onChange={(e) => setSearch(e.target.value)}
      />

      <button onClick={DisplayOffices}>Refresh</button>

      {message && <p className="message">{message}</p>}

      <table>
        <thead>
          <tr>
            <th>Office Name</th>
            <th>Office Number</th>
            <th>Action</th>
          </tr>
        </thead>

        <tbody>
          {filteredOffices.length > 0 ? (
            filteredOffices.map((office) => (
              <tr key={office.id}>
                <td>{office.name}</td>
                <td>{office.officeNumber}</td>
                <td>
                  <button onClick={() => DeleteOffice(office.id)}>
                    Delete
                  </button>

                  <button disabled>
                    Update
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="3">No offices found.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default OfficeDisplay;
