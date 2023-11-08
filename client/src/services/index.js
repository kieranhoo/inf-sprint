import axios from 'axios';

const API_BASE_URL = 'http://documentManagement-env.eba-a8rti3dw.ap-southeast-1.elasticbeanstalk.com/api/v1';

const axiosJWT = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

export const apiLogin = async (credentials) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/auth/authenticate`, credentials);
        axiosJWT.defaults.headers.common['Authorization'] = `Bearer ${response.data.refresh_token}`;
        const user = await apiGetUser(response.data.access_token)
        let data = {
            data: response.data,
            user: user
        }
        return data;
    } catch (error) {
        throw error;
    }
};

export const apiRefreshToken = async (refreshToken) => {
    try {
        const response = await axios.post(
            `${API_BASE_URL}/auth/refresh-token`,
            null,
            {
                headers: { 'Authorization': `Bearer ${refreshToken}` }
            }
        );
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const apiGetUser = async (accessToken) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/user/info`, {
            'accessToken': accessToken
        });
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const apiLogout = async (accessToken) => {
    try {
        await axios.post(`${API_BASE_URL}/auth/logout`, null, {
            headers: { 'Authorization': `Bearer ${accessToken}` }
        });
    } catch (error) {
        throw error;
    }
};
