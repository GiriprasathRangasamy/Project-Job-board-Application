import React, { useState } from 'react';
import axios from 'axios';

const DocverifyuserRegister = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [qrUrl, setQrUrl] = useState('');
    const [secret, setSecret] = useState('');

    const handleRegister = async () => {
        try {
            const response = await axios.post('http://localhost:8080/api1/register', {
                username,
                password
            });
            setQrUrl(response.data.qrUrl);
            setSecret(response.data.secret);
        } catch (error) {
            console.error('Registration error:', error);
        }
    };

    return (
        <div>
            <h2>Register</h2>
            <input
                type="text"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <button onClick={handleRegister}>Register</button>
            {qrUrl && (
                <div>
                    <h3>Scan this QR Code with Google Authenticator:</h3>
                    <img src={qrUrl} alt="QR Code" />
                    <p><strong>Secret:</strong> {secret}</p>
                </div>
            )}
        </div>
    );
};

export default DocverifyuserRegister;
