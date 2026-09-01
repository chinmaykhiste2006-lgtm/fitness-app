import { configureStore } from '@reduxjs/toolkit'
import authReducer from './authSlice.js'
import assistantReducer from './assistantSlice.js'

export const store = configureStore({

    reducer: {
        auth: authReducer,
        assistant: assistantReducer,
    },


})
