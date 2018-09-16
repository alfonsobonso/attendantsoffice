class AuthenticationService {
    constructor() {
        this.fetch = this.fetch.bind(this) // React binding stuff
        this.login = this.login.bind(this)
        this.getProfile = this.getProfile.bind(this)
    }

    login(email, password, loginSuccess, loginError) {
        this.fetch('/api/authentication/login', {
            method: 'post',
            body: JSON.stringify({
                email,
                password
            })
        })
        .then((response) => {
            if(response.ok) {
                response.json().then((json) => {
                    this.saveToken(json.token);
                    this.saveUser(json.userId, json.firstName, json.lastName, json.role)
                    loginSuccess(json);
                })
            } else if (response.status < 500) {
                response.json().then((json) => {
                    loginError(json);
                })
            } else {
                // something has gone wrong
                loginError({ "code": "Failed to submit request"});
            }
        });
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

    saveUser(userId, firstName, lastName, role) {
        localStorage.setItem('userId', userId);
        localStorage.setItem('firstName', firstName);
        localStorage.setItem('lastName', lastName);
        localStorage.setItem('role', role);
    }

    logout() {
        // Clear user token and profile data from localStorage
        localStorage.removeItem('authToken');
        localStorage.removeItem('userId');
        localStorage.removeItem('firstName');
        localStorage.removeItem('lastName');
        localStorage.removeItem('role');
    }

    getProfile() {
        return localStorage.getItem('firstName') + " " + localStorage.getItem('lastName');
    }

    // basic role checking. Currently we only support 2 - USER, ADMIN. ADMIN roles are unrestricted
    isValidRole(requiredRole) {
        let currentRole = localStorage.getItem('role');
        if(!currentRole) {
            // not logged in
            return false;   
        }
        if(currentRole === 'ADMIN') {
            return true;
        }
        return currentRole === requiredRole;
    }

    isAdmin() {
        return this.isValidRole("ADMIN");
    }


	fetch(url, options) {
        // performs api calls sending the required authentication headers
        const headers = {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }

        if (this.isLoggedIn()) {
            headers['Authorization'] = 'Bearer ' + this.fetchToken()
        }

        return fetch(url, {
            headers,
            credentials: 'same-origin',
            ...options
        })
    }   
};

export default AuthenticationService;
