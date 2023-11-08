import { createSlice } from '@reduxjs/toolkit';

const authSlice = createSlice({
    name: 'auth',
    initialState: {
        isLoading: false,
        isAuthenticated: false,
        accessToken: null,
        refreshToken: null,
        user: null,
        error: ""
    },
    reducers: {
        loginRequest: (state, action) => {
            state.isLoading = true;
            state.error = ""
        },
        loginSuccess: (state, action) => {
            state.accessToken = action.payload.data.access_token;
            state.refreshToken = action.payload.data.refresh_token;
            state.user = action.payload.user
            state.isAuthenticated = true
            state.isLoading = false;
        },
        loginFailure: (state, action) => {
            state.isLoading = false;
            state.error = action.payload.message
        },
        setUser: (state, action) => {
            state.user = action.payload;
        },
        logoutRequest: (state, action) => {
            state.isLoading = true;
        },
        logoutSuccess: (state) => {
            state.isAuthenticated = false;
            state.accessToken = null;
            state.refreshToken = null;
            state.user = null;
            state.isLoading = false;
        },
        logoutFailure: (state, action) => {
            state.isLoading = false;
            state.error = action.payload
        },
    },
});

export const { loginRequest, loginSuccess, loginFailure, setUser, logoutRequest, logoutSuccess, logoutFailure } = authSlice.actions;
export default authSlice.reducer;
