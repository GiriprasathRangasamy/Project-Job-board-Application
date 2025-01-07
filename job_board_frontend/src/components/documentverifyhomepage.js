import React, { useState, useEffect } from 'react';
import axios from 'axios';

const DocumentTable = () => {
    const [companies, setCompanies] = useState([]);
    const [selectedCompany, setSelectedCompany] = useState(null);
    const [documentData, setDocumentData] = useState(null);

    // Fetch all companies when the component mounts
    useEffect(() => {
        axios
            .get("http://localhost:8080/company/documents")
            .then(response => setCompanies(response.data))
            .catch(error => console.error('Error fetching companies:', error));
    }, []);

    // Handle the selection of a company to fetch its documents
    const handleCompanyClick = async (companyId) => {
        try {
            const response = await axios.get(`http://localhost:8080/company/${companyId}`);
            const company = response.data;
            
            const{gstRegistrationCertificate}=company;
            setSelectedCompany(company);
            setDocumentData(gstRegistrationCertificate); // Assuming gstRegistrationCertificate is a Base64 string
            console.log(documentData);
        } catch (error) {
            console.error('Error fetching company documents:', error);
        }
    };

    return (
        <div>
            <h1>Company Document Table</h1>

            {/* Table displaying all companies */}
            <table>
                <thead>
                    <tr>
                        <th>Company Name</th>
                        <th>GST Number</th>
                        <th>Number of Employees</th>
                    </tr>
                </thead>
                <tbody>
                    {companies.map(company => (
                        <tr
                            key={company.companyId}
                            onClick={() => handleCompanyClick(company.companyId)}
                            style={{ cursor: 'pointer' }}
                        >
                            <td>{company.companyName}</td>
                            <td>{company.gstNumber}</td>
                            <td>{company.numEmployees}</td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {/* Display selected company's documents */}
            {selectedCompany && (
                <div>
                    <h2>Documents for {selectedCompany.companyName}</h2>
                    <p><strong>Address:</strong> {selectedCompany.address}</p>

                    {documentData ? (
                        <>
                        {console.log(documentData)}
                            <iframe
                                src={`data:application/pdf;base64,${documentData}`}
                                width="100%"
                                height="600px"
                                title="PDF Viewer"
                                
                                style={{ overflow: 'auto' }}
                                onError={() => {
                                    alert("Failed to load the PDF document. Please download it instead.");
                                }}
                            ></iframe>
                            <a href={`data:application/pdf;base64,${documentData}`} download="document.pdf">
                                Download PDF
                            </a>
                        </>
                    ) : (
                        <p>No document available.</p>
                    )}
                </div>
            )}
        </div>
    );
};

export default DocumentTable;