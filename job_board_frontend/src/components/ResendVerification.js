import React, { useState } from 'react';
import axios from 'axios';

function ResendVerification() {
    const [email, setEmail] = useState('');
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:8081/security/resend-verification', null, {
                params: { email }
            });
            setMessage(response.data);
            setError('');
        } catch (error) {
            setMessage('');
            setError(error.response?.data || 'Something went wrong');
        }
    };

    return (
        <div>
            <h2>Resend Verification Email</h2>
            <form onSubmit={handleSubmit}>
                <label>Email:
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                </label>
                <button type="submit">Resend Email</button>
            </form>
            {message && <p style={{ color: 'green' }}>{message}</p>}
            {error && <p style={{ color: 'red' }}>{error}</p>}
        </div>
    );
}

export default ResendVerification;
