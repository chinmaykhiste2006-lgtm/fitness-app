import axios from 'axios';

const API_URL = 'http://localhost:8080/api/v1';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if(token){
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  }
);

export const getActivities = () => api.get('/activities');
export const addActivity = (activity) => api.post('/activities/create', activity);
export const getActivityDetail = (id) => api.get(`/recommendations/activity/${id}`);
export const registerUser = (token) => api.post('/users/register', null, {
  headers: {
    Authorization: `Bearer ${token}`,
  },
});
