import React, { useState } from 'react';
import axios from 'axios';

const RazorpayPayment = ({ companyId }) => {
    const [orderId, setOrderId] = useState('');

    // Step 1: Create Razorpay order via Spring Boot backend
    const createOrder = async () => {
        try {
            const { data } = await axios.post('http://localhost:8080/api/payment/create-order');
            setOrderId(data.orderId);
            openRazorpayCheckout(data.orderId);
        } catch (error) {
            console.error('Error creating Razorpay order:', error);
        }
    };

    // Step 2: Open Razorpay UI
    const openRazorpayCheckout = (orderId) => {
        const options = {
            key: 'rzp_test_w52Rce8bQSsnwr',
            amount: 300 * 100, // Amount in paise (300 INR)
            currency: 'INR',
            name: 'Your Company',
            description: 'Test Transaction',
            order_id: orderId,
            handler: async function (response) {
                try {
                    const companyId = encodeURIComponent('1');
                    const paymentId = encodeURIComponent(response.razorpay_payment_id);
    
                    // Step 3: Verify the payment via Spring Boot backend
                    const { data } = await axios.get(
                        `http://localhost:8080/api/payment/verify-payment/${paymentId}?companyId=${companyId}`
                    );
    
                    alert(`Payment ${data.status}! Payment ID: ${response.razorpay_payment_id}`);
                } catch (error) {
                    console.error('Error verifying payment:', error);
                    alert('Payment verification failed!');
                }
            },
            prefill: {
                name: 'John Doe',
                email: 'john.doe@example.com',
                contact: '9999999999',
            },
            theme: {
                color: '#3399cc',
            },
        };
    
        const rzp = new window.Razorpay(options);
        rzp.open();
    };
    
    return (
        <div>
            <button onClick={createOrder}>Pay ₹300</button>
        </div>
    );
};

export default RazorpayPayment;
