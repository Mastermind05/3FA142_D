import axiosInstance from "../service/axiosinstance";

class User {
    constructor() {
        this.userid = null;
        this.username = null;
        this.email = null;
        this.token = null;
    }

    async login(email, password) {
        try {
            const response = await axiosInstance.post('/auth/login', {
                email,
                password
            });

            if (response.data.error) {
                return { error: response.data.error };
            }

            this.email = email;
            this.token = response.data.token;
            this.userid = response.data.user.id;
            this.username = response.data.user.username;

            localStorage.setItem('authToken', this.token);
            localStorage.setItem('userData', JSON.stringify({
                id: this.userid,
                email: this.email,
                username: this.username
            }));

            return { success: true };
        } catch (error) {
            return { error: 'Login failed. Please try again.' };
        }
    }

    async register(username, email, password) {
        try {
            const response = await axiosInstance.post('/auth/register', {
                username,
                email,
                password
            });

            if (response.data.error) {
                return { error: response.data.error };
            }

            return { success: true };
        } catch (error) {
            return { error: 'Registration failed. Please try again.' };
        }
    }

    logout() {
        localStorage.removeItem('authToken');
        localStorage.removeItem('userData');
        this.userid = null;
        this.username = null;
        this.email = null;
        this.token = null;
        return { success: true };
    }

    isAuthenticated() {
        return !!localStorage.getItem('authToken');
    }

    getCurrentUser() {
        const userData = localStorage.getItem('userData');
        return userData ? JSON.parse(userData) : null;
    }
}

export default User;


