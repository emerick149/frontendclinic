import React, { useState, useEffect, Component } from 'react';
import axios from 'axios';
import './index.css';

const BASE_URL = 'http://localhost:8082/api/offices';

// Prevents the whole page going blank if any render error occurs
class ErrorBoundary extends Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false, error: null };
  }
  static getDerivedStateFromError(error) {
    return { hasError: true, error };
  }
  render() {
    if (this.state.hasError) {
      return (
        <div style={{ padding: 32, color: '#b91c1c' }}>
          <strong>Something went wrong:</strong> {this.state.error?.message}
        </div>
      );
    }
    return this.props.children;
  }
}

function App() {
  const [officeName, setOfficeName] = useState('');
  const [officeNumber, setOfficeNumber] = useState('');
  const [saveMessage, setSaveMessage] = useState('');

  const [offices, setOffices] = useState([]);
  const [search, setSearch] = useState('');
  const [tableMessage, setTableMessage] = useState('');

  // Edit state
  const [editId, setEditId] = useState(null);
  const [editName, setEditName] = useState('');
  const [editNumber, setEditNumber] = useState('');

  const fetchOffices = async () => {
    try {
      const res = await axios.get(`${BASE_URL}/all`);
      const data = Array.isArray(res.data) ? res.data : [];
      setOffices(data);
      setTableMessage('');
    } catch (error) {
      console.error('fetchOffices error:', error);
      if (error.response) {
        setTableMessage(`Server error ${error.response.status}: could not load offices.`);
      } else if (error.request) {
        setTableMessage('Backend is not reachable. Start Spring Boot on port 8082.');
      } else {
        setTableMessage(error.message);
      }
    }
  };

  useEffect(() => {
    fetchOffices();
  }, []);

  const handleSave = async () => {
    if (!officeName.trim() || !officeNumber) {
      setSaveMessage('Please enter both office name and number.');
      return;
    }

    try {
      await axios.post(`${BASE_URL}/save`, {
        name: officeName.trim(),
        officeNumber: parseInt(officeNumber, 10),
      });
      setSaveMessage('Office saved successfully.');
      setOfficeName('');
      setOfficeNumber('');
      setSearch('');
      fetchOffices();
    } catch (error) {
      if (error.response) {
        const d = error.response.data;
        setSaveMessage(typeof d === 'string' ? d : d?.message || 'Save failed.');
      } else if (error.request) {
        setSaveMessage('No response from backend. Is Spring Boot running?');
      } else {
        setSaveMessage(error.message);
      }
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this office?')) return;
    try {
      await axios.delete(`${BASE_URL}/delete/${id}`);
      setTableMessage('Office deleted.');
      fetchOffices();
    } catch {
      setTableMessage('Delete failed.');
    }
  };

  const startEdit = (office) => {
    setEditId(office.id);
    setEditName(office.name);
    setEditNumber(office.officeNumber);
    setTableMessage('');
  };

  const cancelEdit = () => {
    setEditId(null);
    setEditName('');
    setEditNumber('');
  };

  const handleUpdate = async (id) => {
    if (!editName.trim() || !editNumber) {
      setTableMessage('Please fill in both fields to update.');
      return;
    }
    try {
      await axios.put(`${BASE_URL}/${id}/updateOffice`, {
        name: editName.trim(),
        officeNumber: parseInt(editNumber, 10),
      });
      setTableMessage('Office updated successfully.');
      cancelEdit();
      fetchOffices();
    } catch (error) {
      if (error.response) {
        const d = error.response.data;
        setTableMessage(typeof d === 'string' ? d : d?.message || 'Update failed.');
      } else if (error.request) {
        setTableMessage('No response from backend.');
      } else {
        setTableMessage(error.message);
      }
    }
  };

  const filteredOffices = offices.filter(
    (o) =>
      o.name.toLowerCase().includes(search.toLowerCase()) ||
      o.officeNumber.toString().includes(search)
  );

  const handleClear = () => {
    setSearch('');
  };

  return (
    <ErrorBoundary>
    <div className="page">
      {/* ── Save Office Form ── */}
      <section className="card">
        <h2 className="section-title">Save Office</h2>

        <div className="form-row">
          <label>Office Name:</label>
          <input
            type="text"
            value={officeName}
            onChange={(e) => setOfficeName(e.target.value)}
          />
        </div>

        <div className="form-row">
          <label>Office Number:</label>
          <input
            type="number"
            value={officeNumber}
            onChange={(e) => setOfficeNumber(e.target.value)}
          />
        </div>

        <div className="form-actions">
          <button className="btn btn-primary" onClick={handleSave}>
            SAVE OFFICE
          </button>
        </div>

        {saveMessage && <p className="feedback">{saveMessage}</p>}
      </section>

      {/* ── All Offices Table ── */}
      <section className="card">
        <h2 className="section-title">All Offices</h2>

        <div className="search-row">
          <input
            type="text"
            placeholder="Search by name or number"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
          <button className="btn btn-secondary" onClick={fetchOffices}>
            Search
          </button>
          <button className="btn btn-secondary" onClick={handleClear}>
            Clear
          </button>
        </div>

        {tableMessage && <p className="feedback">{tableMessage}</p>}

        <table>
          <thead>
            <tr>
              <th>#</th>
              <th>Office Name</th>
              <th>Office Number</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filteredOffices.length > 0 ? (
              filteredOffices.map((office, index) =>
                editId === office.id ? (
                  <tr key={office.id} className="edit-row">
                    <td>{index + 1}</td>
                    <td>
                      <input
                        className="inline-input"
                        type="text"
                        value={editName}
                        onChange={(e) => setEditName(e.target.value)}
                      />
                    </td>
                    <td>
                      <input
                        className="inline-input"
                        type="number"
                        value={editNumber}
                        onChange={(e) => setEditNumber(e.target.value)}
                      />
                    </td>
                    <td className="actions-cell">
                      <button
                        className="btn btn-save-inline"
                        onClick={() => handleUpdate(office.id)}
                      >
                        Save
                      </button>
                      <button className="btn btn-cancel" onClick={cancelEdit}>
                        Cancel
                      </button>
                    </td>
                  </tr>
                ) : (
                  <tr key={office.id}>
                    <td>{index + 1}</td>
                    <td>{office.name}</td>
                    <td>{office.officeNumber}</td>
                    <td className="actions-cell">
                      <button
                        className="btn btn-edit"
                        onClick={() => startEdit(office)}
                      >
                        Edit
                      </button>
                      <button
                        className="btn btn-delete"
                        onClick={() => handleDelete(office.id)}
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                )
              )
            ) : (
              <tr>
                <td colSpan="4" className="empty-row">
                  No offices found.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </section>
    </div>
    </ErrorBoundary>
  );
}

export default App;
