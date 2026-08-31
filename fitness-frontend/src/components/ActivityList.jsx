import React from 'react'
import { useState } from 'react';
import { Grid, Typography } from '@mui/material';
import getActivities from '../api/ActivityApi';

export const ActivityList = () => {

  const [activities, setActivities] = useState([]);
  const navigate = useNavigate();



    const fetchActivities = async () => {
      try {

      const response = await getActivities();
      setActivities(response.data);
      } catch (error) {
        console.error('Error fetching activities:', error);
      }
      
    };
    

  useEffect(() => {
    
    fetchActivities();

  }, []);

  return (
    <Grid container spacing={2}>
      {
        activities.map((activity) => (
          <Grid item xs={12} sm={6} md={4} key={activity.id}>
            <Card>
              <CardContent>
                <Typography variant="h6" component="div">
                  {activity.type}
                </Typography>
                <Typography component="div">
                  {activity.duration} minutes
                </Typography>
                <Typography component="div">
                  {activity.caloriesBurned} calories burned
                </Typography>
              </CardContent>
            </Card>
          </Grid>
        )) 
      }
    </Grid>
  )
}

export default ActivityList;
