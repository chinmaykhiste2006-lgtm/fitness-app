// import './App.css'
import { BrowserRouter as Router, Navigate, Route, Routes } from 'react-router'
import { Button } from '@mui/material'
import { useContext, useEffect, useRef, useState } from 'react'
import { useDispatch } from 'react-redux'
import { AuthContext } from 'react-oauth2-code-pkce'
import { logout as clearCredentials, setCredentials } from './store/authSlice.js'
import { Box } from '@mui/material'
import ActivityForm from './components/ActivityForm.jsx'
import ActivityList from './components/ActivityList.jsx'
import ActivityDetail from './components/ActivityDetail.jsx'
import { registerUser } from './services/api.js'

const ActivitiesPage = () => {
  return (
    <Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>
      <ActivityForm onActivityAdded={() => { window.location.reload(); }} />
      <ActivityList />
    </Box>
  );
} 

function App() {

  const { token, tokenData, logIn, logOut, loginInProgress } = 
                useContext(AuthContext);
  const dispatch = useDispatch();
  const [registration, setRegistration] = useState({ token: null, status: 'idle' });
  const requestedTokenRef = useRef(null);
  const activeTokenRef = useRef(null);
  const canRegisterUser = Boolean(token && tokenData?.sub && !loginInProgress);
  const isRegisteredForCurrentUser =
    localStorage.getItem('registeredUserId') === tokenData?.sub;

  useEffect(() => {
    activeTokenRef.current = token;

    if (!token) {
      requestedTokenRef.current = null;
      Promise.resolve().then(() => {
        if (!activeTokenRef.current) setRegistration({ token: null, status: 'idle' });
      });
      return;
    }

    // A token can be restored or refreshed while the OAuth client is still
    // completing its login flow. Do not call application APIs until Keycloak
    // has completed that flow and supplied a user subject.
    if (!canRegisterUser) return;

    dispatch(setCredentials({token, user: tokenData}));

    // A completed registration belongs to the current authenticated user, not
    // to this page instance. This keeps a reload from registering again.
    if (isRegisteredForCurrentUser) {
      requestedTokenRef.current = token;
      return;
    }

    // Run this once for the token obtained at the beginning of a login session.
    if (requestedTokenRef.current === token) return;

    requestedTokenRef.current = token;
    Promise.resolve()
      .then(() => {
        if (activeTokenRef.current !== token) return false;
        setRegistration({ token, status: 'pending' });
        return registerUser(token).then(() => true);
      })
      .then((registered) => {
        if (registered && activeTokenRef.current === token) {
          localStorage.setItem('registeredUserId', tokenData.sub);
          setRegistration({ token, status: 'ready' });
        }
      })
      .catch((error) => {
        console.error('Unable to register the authenticated user:', error);
        if (activeTokenRef.current === token) setRegistration({ token, status: 'error' });
      });
  }, [token, tokenData, loginInProgress, canRegisterUser, isRegisteredForCurrentUser, dispatch]);

  const handleLogout = () => {
    dispatch(clearCredentials());
    logOut();
  };

  const registrationStatus = isRegisteredForCurrentUser
    ? 'ready'
    : registration.token === token ? registration.status : 'pending';


  return (
    <Router>
      {!token ? (
      <Button variant="contained" 
      onClick={() => {logIn();}}>
          Login
      </Button>
      ) : !canRegisterUser ? (
        <Box component="section" sx={{ p: 2 }}>
          Completing sign-in...
        </Box>
      ) : registrationStatus === 'error' ? (
        <Box component="section" sx={{ p: 2 }}>
          Unable to prepare your account. Please refresh and try again.
        </Box>
      ) : registrationStatus !== 'ready' ? (
        <Box component="section" sx={{ p: 2 }}>
          Preparing your account...
        </Box>
      ) : (
        <div>
          <Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>
            <Button variant="contained" onClick={handleLogout}>
            Logout
            </Button>
            <Routes>
               <Route path="/activities" element={<ActivitiesPage />} />
                <Route path="/activities/:id" element={<ActivityDetail />} />
                <Route path="/" element={token ? <Navigate to="/activities" replace /> :
                    <div>Welcome, login to continue</div>} />
            </Routes>
          </Box>
        </div>
      )}
    </Router>


  )
 
}

export default App
