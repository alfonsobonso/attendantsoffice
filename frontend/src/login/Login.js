import React, { Component } from 'react';
import { Formik } from 'formik';
import AuthenticationService from '../authentication/AuthenticationService.js'

// When the user already has a password set they go to the basic email + password form to authenticate
const LoginForm = ({AuthService, loginSuccess, loginFailure}) => (
    <div>
    <h1>Login</h1>
    <Formik
        initialValues = {{ email: '', password: '', }}
        validate = {
            values => {
                let errors = {};
                if (!values.email) {
                    errors.email = 'Required';
                } else if (
                        !/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.email)
                ) {
                    errors.email = 'Invalid email address';
                }
                return errors;
            }
        }
        onSubmit = {
            (values, { setSubmitting, setErrors }) => {
                const submitFailure = (json) => {
                    let errors = {};
                    if(json.code === 'UserNameNotFound') {
                        errors.email = 'Unrecognised email address';
                    } else if(json.code === 'WrongPassword') {
                        errors.password = 'Wrong password';
                    }
                    setErrors(errors);
                    loginFailure(json);
                }

                AuthService.login(values.email, values.password, loginSuccess, submitFailure)
                setSubmitting(false);
            }
        }
        render = {
            ({values, errors, touched, handleChange, handleBlur, handleSubmit, isSubmitting, }) => (
                <form onSubmit={handleSubmit}>
                <input type="email" name="email" onChange={handleChange} onBlur={handleBlur} value={values.email} />
                {touched.email && errors.email && <div>{errors.email}</div>}
                <input type="password" name="password" onChange={handleChange} onBlur={handleBlur} value={values.password} />
                {touched.password && errors.password && <div>{errors.password}</div>}
                <button type="submit" disabled={isSubmitting}>
                Submit
                </button>
                </form>
            )
        }
    />
    </div>
);


class Login extends Component {
    constructor() {
        super();
        this.AuthService = new AuthenticationService();
    }

    state = {};

    componentWillMount(){
        if(this.AuthService.isLoggedIn()) {
            this.props.history.replace('/');
        }
    }

    loginSuccess = (json) => {
        this.props.history.replace('/');
    }

    loginFailure = (json) => {
        // do nothing just now
    }

    render() {
        return (
            <div>
                <LoginForm AuthService={ this.AuthService } loginSuccess={ this.loginSuccess } loginFailure ={ this.loginFailure } />
            </div>
        );
    }
}

export default Login;
