import React from 'react';
import { Box, Button } from '@mui/material';
import { FormControl } from '@mui/material';
import { InputLabel } from '@mui/material';
import { Select } from '@mui/material';
import { MenuItem } from '@mui/material'
import { TextField } from '@mui/material';
import DirectionsRunIcon from '@mui/icons-material/DirectionsRun';
import DirectionsBikeIcon from '@mui/icons-material/DirectionsBike';
import PoolIcon from '@mui/icons-material/Pool';
import DirectionsWalkIcon from '@mui/icons-material/DirectionsWalk';
import SelfImprovementIcon from '@mui/icons-material/SelfImprovement';
import FitnessCenterIcon from '@mui/icons-material/FitnessCenter';
import BoltIcon from '@mui/icons-material/Bolt';
import SpaIcon from '@mui/icons-material/Spa';
import MusicNoteIcon from '@mui/icons-material/MusicNote';
import RowingIcon from '@mui/icons-material/Rowing';
import LoopIcon from '@mui/icons-material/Loop';
import SportsGymnasticsIcon from '@mui/icons-material/SportsGymnastics';
import TerrainIcon from '@mui/icons-material/Terrain';
import DownhillSkiingIcon from '@mui/icons-material/DownhillSkiing';
import SnowboardingIcon from '@mui/icons-material/Snowboarding';
import { addActivity } from '../services/api.js';



const ActivityForm = ({onActivityAdded}) => {

    const [activity, setActivity] = React.useState({
        type: "WALKING", duration: '', caloriesBurned: '', 
        additionalMetrix: {}
    });

    const handleSubmit = async (e) => {
        e.preventDefault();
        try{

            await addActivity(activity);
            onActivityAdded();
            setActivity({type: "WALKING", duration: '', caloriesBurned: '', additionalMetrix: {}});

        } catch(err) {
            console.error("Error adding activity:", err);
        }
    }

  return (
    <Box component="form" sx={{ mb: 2 }} onSubmit={handleSubmit}>
      <FormControl fullWidth sx={{ mb: 2 }}>
  <InputLabel>Activity Type</InputLabel>
  <Select
    value={activity.type}
    label="Activity Type"
    onChange={(e) => {setActivity({...activity, type: e.target.value})}}
  >
    <MenuItem value="RUNNING">
  <DirectionsRunIcon fontSize="small" sx={{ mr: 1 }} /> Running
</MenuItem>
<MenuItem value="CYCLING">
  <DirectionsBikeIcon fontSize="small" sx={{ mr: 1 }} /> Cycling
</MenuItem>
<MenuItem value="SWIMMING">
  <PoolIcon fontSize="small" sx={{ mr: 1 }} /> Swimming
</MenuItem>
<MenuItem value="WALKING">
  <DirectionsWalkIcon fontSize="small" sx={{ mr: 1 }} /> Walking
</MenuItem>
<MenuItem value="YOGA">
  <SelfImprovementIcon fontSize="small" sx={{ mr: 1 }} /> Yoga
</MenuItem>
<MenuItem value="STRENGTH_TRAINING">
  <FitnessCenterIcon fontSize="small" sx={{ mr: 1 }} /> Strength Training
</MenuItem>
<MenuItem value="HIIT">
  <BoltIcon fontSize="small" sx={{ mr: 1 }} /> HIIT
</MenuItem>
<MenuItem value="PILATES">
  <SpaIcon fontSize="small" sx={{ mr: 1 }} /> Pilates
</MenuItem>
<MenuItem value="DANCING">
  <MusicNoteIcon fontSize="small" sx={{ mr: 1 }} /> Dancing
</MenuItem>
<MenuItem value="ROWING">
  <RowingIcon fontSize="small" sx={{ mr: 1 }} /> Rowing
</MenuItem>
<MenuItem value="ELLIPTICAL">
  <LoopIcon fontSize="small" sx={{ mr: 1 }} /> Elliptical
</MenuItem>
<MenuItem value="JUMP_ROPE">
  <SportsGymnasticsIcon fontSize="small" sx={{ mr: 1 }} /> Jump Rope
</MenuItem>
<MenuItem value="HIKING">
  <TerrainIcon fontSize="small" sx={{ mr: 1 }} /> Hiking
</MenuItem>
<MenuItem value="SKIING">
  <DownhillSkiingIcon fontSize="small" sx={{ mr: 1 }} /> Skiing
</MenuItem>
<MenuItem value="SNOWBOARDING">
  <SnowboardingIcon fontSize="small" sx={{ mr: 1 }} /> Snowboarding
</MenuItem>
   
  </Select>
</FormControl>

<TextField fullWidth
label="Duration (minutes)" 
type="number"
sx={{ mb: 2 }}
value={activity.duration}
onChange={(e) => {setActivity({...activity, duration: e.target.value})}}
/>
<TextField fullWidth
label="Calories Burned" 
type="number"
sx={{ mb: 2 }}
value={activity.caloriesBurned}
onChange={(e) => {setActivity({...activity, caloriesBurned: e.target.value})}}
/>

<Button type="submit" variant="contained" color="primary">
  Add Activity
</Button>
    </Box>
  );
}

export default ActivityForm;
