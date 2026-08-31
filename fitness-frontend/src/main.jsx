import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'

import { Provider } from 'react-redux'
import { store } from './store/store.js'
// import './index.css'
import App from './App.jsx'
import { authConfig } from './authConfig.js'
import { AuthProvider } from 'react-oauth2-code-pkce'


createRoot(document.getElementById('root')).render(
  <AuthProvider authConfig={authConfig}>
  <Provider store={store}>
    <App />
  </Provider>
  </AuthProvider>
)
