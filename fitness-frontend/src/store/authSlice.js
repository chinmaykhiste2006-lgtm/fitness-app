import { createSlice } from '@reduxjs/toolkit'
import { authConfig } from '../authConfig.js'
import { AuthProvider } from 'react-oauth2-code-pkce'
import { useContext } from 'react'
import { AuthContext } from 'react-oauth2-code-pkce'

const authSlice = createSlice({
  name: 'auth',
  initialState: {
    user: JSON.parse(localStorage.getItem('user')) || null,
    token: localStorage.getItem('token') || null,
    userId: localStorage.getItem('userId') || null,

  },
  reducers: {
    setCredentials:  (state, action) => {
     state.user = action.payload.user;
     state.token = action.payload.token;
     state.userId = action.payload.user.sub;


     localStorage.setItem('token', action.payload.token);
     localStorage.setItem('user', JSON.stringify(action.payload.user));
     localStorage.setItem('userId', action.payload.user.sub); 
    },
    logout: (state) => {
    
        state.user = null;
        state.token = null;
        state.userId = null;
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        localStorage.removeItem('userId');
        localStorage.removeItem('registeredUserId');
    }
  },
})

export const { setCredentials, logout } = authSlice.actions
export default authSlice.reducer
