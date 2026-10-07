import { AddRounded, BoltRounded, DeleteOutlineRounded, DirectionsRunRounded, FitnessCenterRounded, PoolRounded, SelfImprovementRounded } from '@mui/icons-material';
import { Alert, Box, Button, IconButton, MenuItem, Paper, Stack, TextField, Typography } from '@mui/material';
import { useState } from 'react';
import { addActivity } from '../services/api.js';

const activityTypes = [
  ['WALKING', 'Walking', DirectionsRunRounded], ['RUNNING', 'Running', DirectionsRunRounded],
  ['CYCLING', 'Cycling', BoltRounded], ['SWIMMING', 'Swimming', PoolRounded],
  ['YOGA', 'Yoga', SelfImprovementRounded], ['STRENGTH_TRAINING', 'Strength training', FitnessCenterRounded],
  ['HIIT', 'HIIT', BoltRounded], ['PILATES', 'Pilates', SelfImprovementRounded], ['DANCING', 'Dancing', BoltRounded],
  ['ROWING', 'Rowing', FitnessCenterRounded], ['ELLIPTICAL', 'Elliptical', DirectionsRunRounded], ['JUMP_ROPE', 'Jump rope', BoltRounded],
  ['HIKING', 'Hiking', DirectionsRunRounded], ['SKIING', 'Skiing', DirectionsRunRounded], ['SNOWBOARDING', 'Snowboarding', DirectionsRunRounded],
];

const emptyActivity = { type: 'WALKING', duration: '', caloriesBurned: '', startTime: '', additionalMetrics: {} };
const emptyMetric = () => ({ key: '', value: '' });
const metricValue = (value) => value.trim() !== '' && Number.isFinite(Number(value)) ? Number(value) : value;

export default function ActivityForm({ onActivityAdded }) {
  const [activity, setActivity] = useState(emptyActivity);
  const [metrics, setMetrics] = useState([emptyMetric()]);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');

  const update = (field) => (event) => setActivity({ ...activity, [field]: event.target.value });
  const updateMetric = (index, field) => (event) => setMetrics((rows) => rows.map((row, rowIndex) => rowIndex === index ? { ...row, [field]: event.target.value } : row));
  const removeMetric = (index) => setMetrics((rows) => rows.length === 1 ? [emptyMetric()] : rows.filter((_, rowIndex) => rowIndex !== index));
  const handleSubmit = async (event) => {
    event.preventDefault();
    setSubmitting(true); setError('');
    try {
      const additionalMetrics = Object.fromEntries(metrics.filter((metric) => metric.key.trim()).map((metric) => [metric.key.trim(), metricValue(metric.value)]));
      await addActivity({ ...activity, duration: Number(activity.duration), caloriesBurned: Number(activity.caloriesBurned), startTime: activity.startTime || new Date().toISOString(), additionalMetrics });
      setActivity(emptyActivity);
      setMetrics([emptyMetric()]);
      onActivityAdded?.();
    } catch (requestError) {
      setError(requestError.response?.data?.message || 'We could not save this activity. Please try again.');
    } finally { setSubmitting(false); }
  };

  return <Paper component="form" onSubmit={handleSubmit} elevation={0} sx={{ p: { xs: 2.5, md: 3 }, border: '1px solid', borderColor: 'divider', borderRadius: 4 }}>
    <Stack spacing={2.25}>
      <Box><Typography variant="h6" fontWeight={800}>Log an activity</Typography><Typography variant="body2" color="text.secondary">Small wins become meaningful momentum.</Typography></Box>
      {error && <Alert severity="error">{error}</Alert>}
      <TextField select fullWidth label="Activity type" value={activity.type} onChange={update('type')}>
        {activityTypes.map(([value, label, Icon]) => <MenuItem key={value} value={value}><Icon fontSize="small" sx={{ mr: 1.25 }} />{label}</MenuItem>)}
      </TextField>
      <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
        <TextField required fullWidth label="Duration" helperText="Minutes" type="number" inputProps={{ min: 1 }} value={activity.duration} onChange={update('duration')} />
        <TextField required fullWidth label="Calories burned" helperText="Estimated calories" type="number" inputProps={{ min: 0 }} value={activity.caloriesBurned} onChange={update('caloriesBurned')} />
      </Stack>
      <TextField fullWidth label="When did you train?" type="datetime-local" slotProps={{ inputLabel: { shrink: true } }} value={activity.startTime} onChange={update('startTime')} />
      <Box sx={{ p: 2, border: '1px solid', borderColor: 'divider', borderRadius: 3, bgcolor: 'action.hover' }}>
        <Stack spacing={1.5}>
          <Box><Typography variant="subtitle2" fontWeight={800}>Additional metrics</Typography><Typography variant="caption" color="text.secondary">Add any details you want to include, such as distance, heart rate, or steps.</Typography></Box>
          {metrics.map((metric, index) => <Stack key={index} direction={{ xs: 'column', sm: 'row' }} spacing={1} alignItems={{ sm: 'center' }}>
            <TextField fullWidth size="small" label="Metric key" placeholder="e.g. distanceKm" value={metric.key} onChange={updateMetric(index, 'key')} />
            <TextField fullWidth size="small" label="Value" placeholder="e.g. 5.2" value={metric.value} onChange={updateMetric(index, 'value')} />
            <IconButton type="button" aria-label="Remove metric" onClick={() => removeMetric(index)} color="default"><DeleteOutlineRounded /></IconButton>
          </Stack>)}
          <Button type="button" size="small" variant="outlined" startIcon={<AddRounded />} onClick={() => setMetrics((rows) => [...rows, emptyMetric()])} sx={{ alignSelf: 'flex-start' }}>Add metric</Button>
        </Stack>
      </Box>
      <Button type="submit" variant="contained" size="large" disabled={submitting} startIcon={<AddRounded />} sx={{ borderRadius: 2.5, py: 1.25, fontWeight: 800 }}>
        {submitting ? 'Saving activity…' : 'Add to my journey'}
      </Button>
    </Stack>
  </Paper>;
}
