import { AccessTimeRounded, ChevronRightRounded, LocalFireDepartmentRounded, SearchRounded } from '@mui/icons-material';
import { Alert, Box, Button, CircularProgress, InputAdornment, Paper, Stack, TextField, Typography } from '@mui/material';
import { useState } from 'react';
import { useNavigate } from 'react-router';
import { searchActivities } from '../services/api.js';

const prettyType = (type) => (type || 'Activity').replaceAll('_', ' ').toLowerCase().replace(/\b\w/g, (letter) => letter.toUpperCase());

export default function ActivityList() {
  const navigate = useNavigate();
  const [keyword, setKeyword] = useState('');
  const [activities, setActivities] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const search = async (event) => {
    event?.preventDefault();
    if (!keyword.trim()) return;
    setLoading(true); setError('');
    try { setActivities((await searchActivities(keyword.trim())).data || []); }
    catch (requestError) { setError(requestError.response?.data?.message || 'Could not find activities right now.'); }
    finally { setLoading(false); }
  };

  return <Paper elevation={0} sx={{ p: { xs: 2.5, md: 3 }, border: '1px solid', borderColor: 'divider', borderRadius: 4 }}>
    <Stack spacing={2.25}>
      <Box><Typography variant="h6" fontWeight={800}>Find an activity</Typography><Typography variant="body2" color="text.secondary">Search your history by an activity type or keyword.</Typography></Box>
      <Box component="form" onSubmit={search} sx={{ display: 'flex', gap: 1 }}><TextField fullWidth value={keyword} onChange={(event) => setKeyword(event.target.value)} placeholder="Try running, yoga, or cycling" size="small" InputProps={{ startAdornment: <InputAdornment position="start"><SearchRounded /></InputAdornment> }} /><Button type="submit" variant="outlined" disabled={loading}>Search</Button></Box>
      {error && <Alert severity="error">{error}</Alert>}
      {loading && <Box sx={{ textAlign: 'center', py: 3 }}><CircularProgress size={26} /></Box>}
      {!loading && keyword && !error && activities.length === 0 && <Typography variant="body2" color="text.secondary" sx={{ py: 2 }}>No matching activities yet. Try another keyword or log one above.</Typography>}
      {!loading && activities.map((activity) => <Paper key={activity.id} variant="outlined" sx={{ p: 2, borderRadius: 3, transition: '0.2s', '&:hover': { borderColor: 'primary.main', transform: 'translateY(-2px)' } }}>
        <Stack direction="row" alignItems="center" justifyContent="space-between" spacing={1}><Box><Typography fontWeight={800}>{prettyType(activity.type)}</Typography><Stack direction="row" spacing={1.5} color="text.secondary" mt={.5}><Typography variant="caption" sx={{ display: 'flex', alignItems: 'center', gap: .4 }}><AccessTimeRounded sx={{ fontSize: 15 }} />{activity.duration} min</Typography><Typography variant="caption" sx={{ display: 'flex', alignItems: 'center', gap: .4 }}><LocalFireDepartmentRounded sx={{ fontSize: 15 }} />{activity.caloriesBurned} cal</Typography></Stack></Box><Button aria-label="View activity" onClick={() => navigate(`/activities/${activity.id}`)}><ChevronRightRounded /></Button></Stack>
      </Paper>)}
    </Stack>
  </Paper>;
}
