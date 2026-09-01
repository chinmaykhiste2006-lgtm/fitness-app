import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1',
  headers: { 'Content-Type': 'application/json' },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export const addActivity = (activity) => api.post('/activities/create', activity);
export const getActivity = (id) => api.get(`/activities/${id}`);
export const searchActivities = (keyword) => api.get('/activities/search', { params: { keyword } });
export const getActivityRecommendation = (id) => api.get(`/recommendations/activity/${id}`);
export const getUserRecommendations = (userId) => api.get(`/recommendations/user/${userId}`);
export const getCustomRecommendation = () => api.get('/recommendations/recommend');
export const askCoach = (question) => api.get(`/recommendations/${encodeURIComponent(question)}`);
export const getUser = (userId) => api.get(`/users/${userId}`);
export const registerUser = (token) => api.post('/users/register', null, {
  headers: { Authorization: `Bearer ${token}` },
});
