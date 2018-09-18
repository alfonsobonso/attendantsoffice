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
                    this.saveUser(json.userId, json.email, json.firstName, json.lastName, json.role)
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

    // Before displaying the re-authentication form delete the token. On a succesful auth, this will be rewritten
    // on a failed auth, the user will have the chance to try again
    // if they refresh the page after a failed auth it will take them to the login screen, with all the
    // forgotten password options, etc.
    prepareReauthenticate() {
        localStorage.removeItem('authToken');
    }

    // Called when the user thinks they are logged in but the authentication has failed at the back end - possibly expired or a restart
    // We do this rather than redirect them to the login page to try to preserve any changes they have (and because forcing a page
    // redirect breaks the data flow)
    // In this case we expect to already have the email address. In some edge cases, such as when the local storage has been partially 
    // deleted or some email address has been changed under the feet of the authenticated user, this might go wrong.
    // to minimise that, we should have already deleted the token using the prepareReauthenticate function, meaning that a page refresh
    // will redirect them back to the login page
    reauthenticate(password, loginSuccess, loginError) {
        // fetch the current email
        let email = localStorage.getItem('email')     

        this.login(email, password, loginSuccess, loginError);
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

    saveUser(userId, email, firstName, lastName, role) {
        localStorage.setItem('userId', userId);
        localStorage.setItem('email', email);
        localStorage.setItem('firstName', firstName);
        localStorage.setItem('lastName', lastName);
        localStorage.setItem('role', role);
    }

    logout() {
        // Clear user token and profile data from localStorage
        localStorage.removeItem('authToken');
        localStorage.removeItem('userId');
        localStorage.removeItem('email');
        localStorage.removeItem('firstName');
        localStorage.removeItem('lastName');
        localStorage.removeItem('role');
    }

    getProfile() {
        return localStorage.getItem('firstName') + " " + localStorage.getItem('lastName');
    }

    // basic role checking. Currently we only support 2 - ROLE_USER, ROLE_ADMIN. ROLE_ADMIN roles are unrestricted
    isValidRole(requiredRole) {
        let currentRole = localStorage.getItem('role');
        if(!currentRole) {
            // not logged in
            return false;   
        }
        if(currentRole === 'ROLE_ADMIN') {
            return true;
        }
        return currentRole === requiredRole;
    }

    isAdmin() {
        return this.isValidRole("ROLE_ADMIN");
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
