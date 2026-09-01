import { createSlice } from '@reduxjs/toolkit'

const defaultMessages = [{ role: 'coach', text: 'Hi! I’m your FitFlow coach. Ask me about training, recovery, nutrition, or your next workout.' }]
const read = (key, fallback) => {
  try { return JSON.parse(localStorage.getItem(key)) || fallback } catch { return fallback }
}
const save = (key, value) => value === null ? localStorage.removeItem(key) : localStorage.setItem(key, JSON.stringify(value))

const assistantSlice = createSlice({
  name: 'assistant',
  initialState: { messages: read('fitflow_chat_messages', defaultMessages), plan: read('fitflow_generated_plan', null) },
  reducers: {
    appendChatMessage: (state, action) => { state.messages.push(action.payload); save('fitflow_chat_messages', state.messages) },
    setGeneratedPlan: (state, action) => { state.plan = action.payload; save('fitflow_generated_plan', state.plan) },
    clearAssistantState: (state) => { state.messages = defaultMessages; state.plan = null; save('fitflow_chat_messages', state.messages); save('fitflow_generated_plan', null) },
  },
})

export const { appendChatMessage, setGeneratedPlan, clearAssistantState } = assistantSlice.actions
export default assistantSlice.reducer
