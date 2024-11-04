const axios = require('axios');
require('dotenv').config();
const baseUrl = process.env.BASE_URL;
const strapiToken = process.env.STRAPI_TOKEN; // Replace with your actual Strapi token

const axiosInstance = axios.create({
  baseURL: baseUrl,
  headers: {
    'Content-Type': 'application/json',
    // Authorization: `Bearer ${strapiToken}`,
  },
  timeout: 10000,
});



axiosInstance.interceptors.response.use(
    response => response,
    error => {
        if (error.response) {
            // Request made and server responded
            console.error('Error response data:', error.response.data);
            // console.error('Error response status:', error.response.status);
            // console.error('Error response headers:', error.response.headers);
            return Promise.resolve({ error: error.response.data, status: error.response.status });
        } else if (error.request) {
            // Request made but no response received
            // console.error('Error request data:', error.request);
            return Promise.resolve({ error: 'No response received', request: error.request });
        } else {
            // Something happened in setting up the request that triggered an Error
            console.error('Error message:', error.message);
            return Promise.resolve({ error: error.message });
        }
    }
);

module.exports = axiosInstance;