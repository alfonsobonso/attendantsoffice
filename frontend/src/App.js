import React, {Component} from 'react';
import {
    BrowserRouter as Router,
    Route,
    Link,
    Redirect,
    withRouter
} from "react-router-dom";
import LoginForm  from './login/Login.js';
import logo from './logo.svg';
import './App.css';

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {email: '', password: ''};

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({username: event.target.username, password: event.target.password});
    }


    handleSubmit(event) {
        alert('A name was submitted: ' + this.state.username);
        event.preventDefault();
        authentication.authenticate(this.state.username, this.state.password);
    }
    login = () => {
        authentication.authenticate(() => {
            this.setState({ redirectToReferrer: true });
        });
    };

    render() {
// <p>Please log in</p>
// <form onSubmit={this.handleSubmit}>
// <label>Email: <input type="text" name="email" /></label>
// <label>Password: <input type="password" name="password" /></label>
// <input type="submit" value="Submit" />
// </form>
// <button onClick={this.login}>Log in</button>

        return (
                <div>
                <LoginForm />
                </div>
        );
    }
}

const authentication = {
        isAuthenticated: false,
        authenticate(email, password) {

            var params = {
                    email: email,
                    password: password
            }

            const searchParams = Object.keys(params).map((key) => {
                return encodeURIComponent(key) + '=' + encodeURIComponent(params[key]);
            }).join('&');


            fetch('/api/login', {
                credentials: 'same-origin',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8',
                    'Accept': 'application/json',
                },
                method: 'post',
                body: searchParams
            })
            .then(response => Promise.all([response, response.json()]))
            .then(([response, json]) => {
                if(response.ok) {
                    return json;
                } else {
                    throw new Error(json);
                }
            })
            .catch(console.error);

            this.isAuthenticated = true;
        },
        signout(cb) {
            this.isAuthenticated = false;
        }
};

class App extends Component {

    state = {};

    componentDidMount() {
        setInterval(this.hello, 250000);
    }

    hello = () => {
        fetch('/api/hello', {
            credentials: 'same-origin'
        })
        .then(response => {
            if(response.ok) {
                return response.text();
            }
            return `Request rejected with status ${response.status}`;
        })
        .then(message => {
            this.setState({message: message});
        });
    };

    render() {
        return (
                <div className="App">
                <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                    <h1 className="App-title">{this.state.message}</h1>
                    </header>
                    <p className="App-intro">To get started, edit <code>src/App.js</code> and save to reload.....</p>
                    <Router>
                    <Route path="/login" component={Login} />
                    </Router>

                    </div>
        );
    }
}

export default App;
