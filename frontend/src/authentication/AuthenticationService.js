class AuthenticationService {
    constructor() {
        this.authenticatedFetch = this.authenticatedFetch.bind(this) // React binding stuff
        this.login = this.login.bind(this)
        this.getProfile = this.getProfile.bind(this)
    }

    login(email, password, loginSuccess) {
        this.authenticatedFetch('/api/authentication/login', {
            method: 'post',
            body: JSON.stringify({
                email,
                password
            })
        })
        .then(response => Promise.all([response, response.json()]))
        .then(([response, json]) => {
            if(response.ok) {
                this.saveToken(response.token);
                loginSuccess(json);
            } else {
                loginSuccess(json);
            }
        })
        .catch(console.error);
    }

	isLoggedIn() {
        const token = this.fetchToken() 
        return !!token;
    }

    saveToken(authToken) {
        // Saves user token to localStorage
        localStorage.setItem('authToken', authToken)
    }

    fetchToken() {
        // Retrieves the user token from localStorage
        return localStorage.getItem('authToken')
    }

    logout() {
        // Clear user token and profile data from localStorage
        localStorage.removeItem('authToken');
    }

    getProfile() {
        return "TBD";
    }

	authenticatedFetch(url, options) {
        // performs api calls sending the required authentication headers
        const headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }

        // Setting Authorization header
        // Authorization: Bearer xxxxxxx.xxxxxxxx.xxxxxx
        if (this.isLoggedIn()) {
            headers['Authorization'] = 'Bearer ' + this.getToken()
        }

        return fetch(url, {
            headers,
            credentials: 'same-origin',
            ...options
        })
        //.then(this._checkStatus)
        //.then(response => response.json())
    }   
};

export default AuthenticationService;
