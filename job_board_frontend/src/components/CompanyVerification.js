import React, { useState } from 'react';
import axios from 'axios';
import RazorpayPayment from './paymentpage';

function CompanyVerification() {
    const [companyId, setCompanyId] = useState(1);
    const [companyName, setCompanyName] = useState('');
    const [address, setAddress] = useState('');
    const [numEmployees, setNumEmployees] = useState('');
    const [gstNumber, setGstNumber] = useState('');
    const [files, setFiles] = useState({
        incorporation: null,
        gstRegistration: null,
        officeProof: null,
        shopLicense: null
    });
    
    const [paymentStatus, setPaymentStatus] = useState(''); // Payment status state

    const handleFileChange = (e) => {
        const { name, files } = e.target;
        setFiles((prevState) => ({
            ...prevState,
            [name]: files[0] // Store the selected file under the correct category
        }));
    };

    // Helper function to convert a PDF file to Base64 string
    const convertToBase64 = (file) => {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onload = () => resolve(reader.result.split(',')[1]); // Extract Base64 content
            reader.onerror = (error) => reject(error);
            reader.readAsDataURL(file); // Convert to Base64
        });
    };

    const handleUpload = async () => {
        if (!companyName || !address || !numEmployees || !gstNumber) {
            alert('Please fill all company details.');
            return;
        }

        try {
            const base64Files = {};
            for (const [key, file] of Object.entries(files)) {
                if (file) {
                    base64Files[key] = await convertToBase64(file);
                }
            }

            // Prepare the payload
            const payload = {
                companyId,
                companyName,
                address,
                numEmployees,
                gstNumber,
                certificateOfIncorporation: base64Files.incorporation,
                gstRegistrationCertificate: base64Files.gstRegistration,
                registeredOfficeProof: base64Files.officeProof,
                shopAndEstablishmentLicense: base64Files.shopLicense
            };

            console.log('Payload:', payload); // For debugging

            // Send the data to the backend
            await axios.post('http://localhost:8080/company/upload-documents', payload, {
                headers: { 'Content-Type': 'application/json' }
            });

            alert('Documents uploaded successfully!');
        } catch (error) {
            console.error('Error uploading documents:', error);
            alert('Failed to upload documents. Please try again.');
        }
    };
    const checkPaymentStatus = async () => {
        try {
            const { data } = await axios.get(
                `http://localhost:8080/company/payment-status/${companyId}`
            );
            setPaymentStatus(data.paymentStatus);
        } catch (error) {
            console.error('Error fetching payment status:', error);
            setPaymentStatus('Error retrieving payment status');
        }
    };

    return (
        <div>
            <h2>Company Verification</h2>
            <input
                type="text"
                placeholder="Company ID"
                value={companyId}
                onChange={(e) => setCompanyId(e.target.value)}
            />
            <input
                type="text"
                placeholder="Company Name"
                value={companyName}
                onChange={(e) => setCompanyName(e.target.value)}
            />
            <input
                type="text"
                placeholder="Company Address"
                value={address}
                onChange={(e) => setAddress(e.target.value)}
            />
            <input
                type="number"
                placeholder="Number of Employees"
                value={numEmployees}
                onChange={(e) => setNumEmployees(e.target.value)}
            />
            <input
                type="text"
                placeholder="GST Number"
                value={gstNumber}
                onChange={(e) => setGstNumber(e.target.value)}
            />

            <h3>Documents</h3>
            <div>
                <label>Certificate of Incorporation:</label>
                <input type="file" name="incorporation" onChange={handleFileChange} />
            </div>
            <div>
                <label>GST Registration Certificate:</label>
                <input type="file" name="gstRegistration" onChange={handleFileChange} />
            </div>
            <div>
                <label>Registered Office Proof:</label>
                <input type="file" name="officeProof" onChange={handleFileChange} />
            </div>
            <div>
                <label>Shop and Establishment License:</label>
                <input type="file" name="shopLicense" onChange={handleFileChange} />
            </div>

            <button onClick={handleUpload}>Upload Documents</button>
            <RazorpayPayment/>
            <div>
            <button onClick={checkPaymentStatus} style={{ padding: '5px 10px' }}>
                Check Payment Status
            </button>

            {paymentStatus && (
                <div style={{ marginTop: '20px' }}>
                    <h3>Payment Status: {paymentStatus}</h3>
                </div>
            )}
            </div>
        </div>
    );
}

export default CompanyVerification;